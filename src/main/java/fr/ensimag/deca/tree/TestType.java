package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class TestType extends AbstractExpr {
    AbstractExpr object;
    AbstractIdentifier typeVise;

    public TestType(AbstractExpr object, AbstractIdentifier typeVise) {
        this.object = object;
        this.typeVise = typeVise;
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