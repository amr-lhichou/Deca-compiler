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

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null; // À remplir en Partie B (houssam_amr)
    }

    @Override
    public void decompile(IndentPrintStream s) {
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {}

}