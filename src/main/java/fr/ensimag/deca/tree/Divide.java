package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }
    @Override
    protected void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2) {
        compiler.addInstruction(new QUO(op1, op2));
    }
}
