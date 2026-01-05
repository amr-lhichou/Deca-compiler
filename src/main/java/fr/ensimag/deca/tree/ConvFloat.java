package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

import fr.ensimag.deca.context.ContextualError;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl53
 * @date 01/01/2026
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    protected void codeGenUnaryOp(DecacCompiler compiler, GPRegister op) {
        compiler.addInstruction(new FLOAT(op, op));
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError{
        // throw new UnsupportedOperationException("not yet implemented");
        Type oldType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!oldType.isInt()){
            throw new ContextualError("On ne peut convertir en Float le Type " +
            oldType + " mais seulement le Type INT ", getLocation());
        }

        Type floatType = compiler.environmentType.FLOAT;
        setType(floatType);
        return floatType;

    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
