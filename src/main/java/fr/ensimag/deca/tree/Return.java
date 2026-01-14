package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class Return extends AbstractInst {
    private AbstractExpr expression;

    public Return(AbstractExpr expression) {
        this.expression = expression;
    }

    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass, Type returnType) throws ContextualError {
    }

    protected void codeGenInst(DecacCompiler compiler) {
        expression.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new LOAD(R_target, Register.R0));
        compiler.addInstruction(new BRA(new Label("fin." )));
    }

    public void decompile(IndentPrintStream s) {
        s.print("return");
        expression.decompile(s);
    }

    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}