package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!(leftType.isInt() || leftType.isFloat()) || !(rightType.isInt() || rightType.isFloat())){
            throw new ContextualError("Opération interdite entre " + leftType + " et " + rightType, getLocation());
        }

        Type expType;
        if (leftType.isFloat() || rightType.isFloat()){
            expType = compiler.environmentType.FLOAT;
        } else {
            expType = compiler.environmentType.INT;
        }
        setType(expType);
        return expType;
    }
}
