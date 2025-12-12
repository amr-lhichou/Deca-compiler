package fr.ensimag.deca.tree;


/**
 * Operator "x >= y"
 * 
 * @author gl53
 * @date 01/01/2026
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
