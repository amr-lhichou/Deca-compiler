package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterAllocater;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegisterAllocater allocator = compiler.getRegisterAllocater();

        getLeftOperand().codeGenInst(compiler);
        GPRegister leftReg = allocator.getCurrentRegister();

        GPRegister rightReg = allocator.allocateRegister();

        if (rightReg != null) {
            // we calculate the right
            getRightOperand().codeGenInst(compiler);
            // we keep the register where the right side was calculated
            rightReg = allocator.getCurrentRegister();
            // we calculate (ADD,Minus..)
            codeGenOp(compiler, rightReg, leftReg);
            //we free the right register
            //since we keep the result on the left register
            allocator.freeRegister();

        } else {
            // all the registers are full
            // we use the stack
            // PUSH left
            compiler.addInstruction(new PUSH(leftReg));
            getRightOperand().codeGenInst(compiler);
            //PUSH right
            compiler.addInstruction(new PUSH(leftReg));


            // swap from the stack
            compiler.addInstruction(new LOAD(new RegisterOffset(-1, Register.SP), leftReg));
            compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP),Register.R0));
            // we calculate (ADD,Minus..)
            codeGenOp(compiler, Register.R0, leftReg);

            // Clean Stack
            // We prefer SUBSP than POP because we don't need another register
            compiler.addInstruction(new SUBSP(new ImmediateInteger(2)));
            //Overflow test
            if(!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(new Label("arithmetic_overflow_error")));
            }
//        //creation des registres
//        GPRegister R2 = Register.getR(2);
//        GPRegister R3 = Register.getR(3);
//        //partie gauche:calcul dans R2
//        getLeftOperand().codeGenInst(compiler);
//
//        // sauvegarde de R2 sur la pile
//        compiler.addInstruction(new PUSH(R2));
//
//        // partie droite:calcul dans R2
//        getRightOperand().codeGenInst(compiler);
//
//        // charge le resulat de partie droite dans R3
//        compiler.addInstruction(new LOAD(R2, R3));
//
//        //recuperation de la valeur stockée dans la pile dans R2
//        compiler.addInstruction(new POP(R2));
//
//        // effectue l operation binaire (+,/,*....)
//        codeGenOp(compiler, R3, R2);

        }
    }
    protected abstract void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2);

}


