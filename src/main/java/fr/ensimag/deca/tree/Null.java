package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.DecacCompiler;

public class Null extends AbstractExpr {

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) { }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type nullType = compiler.environmentType.NULL;
        setType(nullType);
        return nullType;
    }


    @Override
    protected void iterChildren(TreeFunction f) { }
}