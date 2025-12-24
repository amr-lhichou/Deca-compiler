package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl53
 * @date 01/01/2026
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }
    @Override
    protected void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2) {
        compiler.addInstruction(new ADD(op1, op2));
    }
}
