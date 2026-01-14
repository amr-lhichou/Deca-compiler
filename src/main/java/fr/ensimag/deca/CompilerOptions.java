package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl53
 * @date 01/01/2026
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    private Set<File> sourceFiles = new LinkedHashSet<>();
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parseOnly = false;        // Option -p
    private boolean warnings = false; // Option -w
    private boolean verificationOnly= false; // Option -v
    private boolean noCheck = false;      // Option -n
    private int registers = 15;           // Option -r
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    // New getters
    public boolean getParse() {
        return parseOnly;
    }
    public boolean getWarnings() {
        return warnings;
    }

    public boolean getVerification() {
        return verificationOnly;
    }

    public boolean getNoCheck() {
        return noCheck;
    }

    public int getRegisters() {
        return registers;
    }
    public List<File> getSourceFiles() {
        return new ArrayList<>(sourceFiles);
    }


    private int debug = 0;
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-b": printBanner = true; break;
                case "-p": parseOnly = true; break;
                case "-v": verificationOnly = true; break;
                case "-n": noCheck = true; break;
                case "-P": parallel = true; break;
                case "-w": warnings = true; break; // Ajout warning
                case "-d": debug++; break;
                case "-r":
                    if (i + 1 >= args.length) {
                        throw new CLIException("check decac -r usage in the doc.");
                    }
                    try {
                        i++;
                        int r = Integer.parseInt(args[i]);

                        if (r < 4 || r > 16) {
                            throw new CLIException("number entre 4 et 16.");
                        }
                        this.registers = r-1;

                    } catch (NumberFormatException e) {
                        throw new CLIException(" doit être un entier.");
                    }
                    break;
                default:
                    // options verification
                    if (arg.startsWith("-")) {
                        throw new CLIException("Option inconnue : " + arg);
                    }
                    // input file verification
                    if (!arg.endsWith(".deca")) {
                        throw new CLIException("Le fichier d'entrée finir par .deca : " + arg);
                    }
                    sourceFiles.add(new File(arg));
                    break;
            }
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");
    }

    }

    public void displayUsage() {
        System.out.println("Usage: decac [[-p | -v] [-n] [-r X] [-d]* [-P] <fichier deca>...] | [-b]");
        System.out.println("Options:");
        System.out.println("  -b       (banner)       : affiche une bannière indiquant le nom de l'équipe");
        System.out.println("  -p       (parse)        : Arrête decac après l'étape de construction de l'arbre et affiche la décompilation.");
        System.out.println("  -v       (verification) : arrête decac après l'étape de vérifications\n" +
                "(ne produit aucune sortie en l'absence d'erreur)");
        System.out.println("  -n       (no check)     : Supprime les tests de débordement à l'exécution (stack, div/0, ...).");
        System.out.println("  -r X     (registers)    : Limite le nombre de registres disponibles à X (4 <= X <= 16).");
        System.out.println("  -d       (debug)        : Active les traces de debug (à répéter).");
        System.out.println("  -P       (parallel)     : Active la compilation parallèle des fichiers.");
    }
}
