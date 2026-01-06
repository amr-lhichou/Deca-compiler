package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
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
        //for int we use : QUO
        if(getType().isInt()){
            compiler.addInstruction(new CMP(new ImmediateInteger(0),op1));
            compiler.addInstruction(new BEQ(new Label("division_zero_error")));
            compiler.addInstruction(new QUO(op1, op2));

        }
        // for the float we use : DIV
        else if (getType().isFloat()) {
            compiler.addInstruction(new CMP(new ImmediateFloat(0.0f), op1));
            compiler.addInstruction(new BEQ(new Label("division_zero_error")));
            compiler.addInstruction(new DIV(op1, op2));
    }
    }
}
