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

    private Label endLabel;
    public void setEndLabel(Label l) { this.endLabel = l; }


    public Return(AbstractExpr expression) {
        this.expression = expression;
    }

    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass, Type returnType) throws ContextualError {
    //retuen interdi so returntype est null ou void
        if(returnType == null) {
            throw new ContextualError("'return' interdite en dehors d'une méthode (règle 3.24)", getLocation());
        }

        if (returnType.isVoid()) {
            throw new ContextualError("'return' non autorisée dans une méthode void (règle 3.24)", getLocation());
        }

        Type exprType = expression.verifyExpr(compiler, localEnv, currentClass);

        expression = expression.verifyRValue(compiler, localEnv, currentClass, returnType);
    }

    protected void codeGenInst(DecacCompiler compiler) {
        expression.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new LOAD(R_target, Register.R0));
        compiler.getRegisterAllocater().freeRegister();
        Label target;
        if (endLabel != null) {
            target = endLabel;
        } else {
            target = compiler.getCurrentMethodEndLabel();
        }
        compiler.addInstruction(new BRA(target));
    }

    public void decompile(IndentPrintStream s) {
        s.print("return ");
        expression.decompile(s);
        s.print(";");
    }

    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}