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
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        // L'addition est arithmétique seulement
        if (!(leftType.isInt() || leftType.isFloat()) || !(rightType.isInt() || rightType.isFloat())){
            throw new ContextualError("Opération '" + getOperatorName() + "' interdite entre " + leftType + " et " + rightType +
            " (règle (3.33)", getLocation());
        }

        Type expType;

        // int + float (convertir le int en float)
        if (leftType.isInt() && rightType.isFloat()){
            ConvFloat convOp = new ConvFloat(getLeftOperand());
            expType = convOp.verifyExpr(compiler, localEnv, currentClass);
            this.setLeftOperand(convOp);
            setType(expType);
            return expType;
        }

        // float + int (convertir le int en float)
        if (rightType.isInt() && leftType.isFloat()){
            ConvFloat convOp = new ConvFloat(getRightOperand());
            expType = convOp.verifyExpr(compiler, localEnv, currentClass);
            this.setRightOperand(convOp);
            setType(expType);
            return expType;
        }

        // int + int
        if (leftType.isInt() && rightType.isInt()){
            expType = compiler.environmentType.INT;
            setType(expType);
            return expType;
        }

        // float + float
        expType = compiler.environmentType.FLOAT;
        setType(expType);
        return expType;
    }
}

