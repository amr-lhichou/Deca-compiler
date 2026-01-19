package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;

/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {

        Type t1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if ((t1.isInt() || t1.isFloat()) && (t2.isInt() || t2.isFloat())) {
            if (t1.isInt() && t2.isFloat()) {
                ConvFloat conv = new ConvFloat(getLeftOperand());
                conv.verifyExpr(compiler, localEnv, currentClass);
                setLeftOperand(conv);
            } else if (t1.isFloat() && t2.isInt()) {
                ConvFloat conv = new ConvFloat(getRightOperand());
                conv.verifyExpr(compiler, localEnv, currentClass);
                setRightOperand(conv);
            }
        } else {
            // Si c'est des Objets ou des Booléens : ERREUR
            throw new ContextualError("Les opérands doivent être de Type INT ou FLOAT (règle 3.33)", getLocation());
        }

        setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

}
