package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Labels pour gérer le flux
        Label fin = new Label("And_fin");
        //on commence par le left
        // le calcul est dans R2 comme tjrs
        getLeftOperand().codeGenInst(compiler);

        //comparer à 0
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(2)));
        // si 0 on saute à la fin
        compiler.addInstruction(new BEQ(fin));

        // sinon on calcule maintenant la droite dans R2
        getRightOperand().codeGenInst(compiler);

        compiler.addLabel(fin);
    }

}
