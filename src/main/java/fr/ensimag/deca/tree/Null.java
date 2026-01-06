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

    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) { }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type nullType = compiler.environmentType.NULL;
        setType(nullType);
        return nullType;
    }


    protected void iterChildren(TreeFunction f) { }
}