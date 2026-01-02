package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class Conversion extends AbstractExpr {
    AbstractExpr cible;
    AbstractIdentifier typeCible;

    public Conversion(AbstractIdentifier typeCible,AbstractExpr cible) {
        this.cible = cible;
        this.typeCible = typeCible;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null; // À remplir en Partie B (houssam_amr)
    }
    public void decompile(IndentPrintStream s) {
    }

    protected void iterChildren(TreeFunction f) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {}

}