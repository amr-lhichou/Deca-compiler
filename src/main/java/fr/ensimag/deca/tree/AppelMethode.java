package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class AppelMethode extends AbstractExpr {
    private final AbstractExpr instance;
    private final AbstractIdentifier methodeCible;
    private final ListExpr parametres;

    public AppelMethode(AbstractExpr instance, AbstractIdentifier methodeCible, ListExpr parametres) {
        this.instance = instance;
        this.methodeCible = methodeCible;
        this.parametres = parametres;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null; // À remplir en Partie B
    }

    public void decompile(IndentPrintStream s) {

    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.instance.prettyPrint(s, prefix, false);
        this.methodeCible.prettyPrint(s, prefix, false);
        this.parametres.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        this.instance.iter(f);
        this.methodeCible.iter(f);
        this.parametres.iter(f);
    }
}