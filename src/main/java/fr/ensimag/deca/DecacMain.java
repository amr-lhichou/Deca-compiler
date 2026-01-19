package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl53
 * @date 01/01/2026
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            //throw new UnsupportedOperationException("decac -b not yet implemented");
            System.out.println("Groupe53 : les Misérables");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            // we dispaly the usages
            options.displayUsage();
            //throw new UnsupportedOperationException("decac without argument not yet implemented");
        }


        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            //throw new UnsupportedOperationException("Parallel build not yet implemented");
                //on va ici implementer la possibilité de lancer la compilation parallèle , on utilison la notion de thread

                List<File> fichiersSources = options.getSourceFiles();
                LOG.info("demarrage comp parallele :  " + fichiersSources.size() + " fichier(s)");

                // on determinele nombre de thread qu on peu lancer :pas plus que le nombre de fichiers
                int nombreFichiers = fichiersSources.size();

                // Créer un pool de threads fixed-size: plus efficace qu'un nouveau thread par tâche, et évite les débordements de ressources
                ExecutorService executeur = Executors.newFixedThreadPool(nombreFichiers);
                List<Future<Boolean>> compilationsEnCours = new ArrayList<>();

                // Lancer chaque compilation dans le pool: chaque fichier est compilé
                // de manière indépendante et potentiellement en parallèle
                for (File fichier : fichiersSources) {
                    Future<Boolean> resultatCompilation = executeur.submit(() -> {
                        // Chaque thread reçoit sa propre instance DecacCompiler pour éviter
                        // les conditions de course (race conditions) sur les ressources partagées
                        DecacCompiler compilateur = new DecacCompiler(options, fichier);
                        LOG.debug("Thread " + Thread.currentThread().getName() +
                                " : compilation de " + fichier.getName());
                        return compilateur.compile();
                    });
                    compilationsEnCours.add(resultatCompilation);
                }

                // Attendre que toutes les compilations se terminent et collecter les erreurs éventuelles
                try {
                    int fichierTraite = 0;
                    for (Future<Boolean> resultat : compilationsEnCours) {
                        fichierTraite++;
                        // .get() bloque jusqu'à ce que le résultat soit disponible
                        // Si une exception s'est produite, elle est re-levée ici
                        if (resultat.get()) {
                            error = true;  // Au moins une erreur de compilation s'est produite
                        }
                        LOG.debug("Fichier " + fichierTraite + "/" + fichiersSources.size() +
                                " compilé");
                    }
                    LOG.info("Compilation parallèle terminée");

                } catch (Exception e) {
                    // Gestion des erreur (interruption ...)
                    error = true;
                    LOG.error("Erreur lors de la compilation parallèle: " + e.getMessage(), e);

                } finally {
                    // on arrete ensuite les threads
                    executeur.shutdown();
                    LOG.debug("threads arrêté");
                }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
