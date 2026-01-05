package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class Return extends AbstractInst {
    private AbstractExpr expression;

    public Return(AbstractExpr expression) {
        this.expression = expression;
    }

    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass, Type returnType) throws ContextualError {
    }

    protected void codeGenInst(DecacCompiler compiler) {
    }

    public void decompile(IndentPrintStream s) {
        s.print("retrn");
        expression.decompile(s);
    }

    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}