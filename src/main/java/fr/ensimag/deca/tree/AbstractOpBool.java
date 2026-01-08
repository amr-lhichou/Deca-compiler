package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        // Opération booléenne entre deux booléens
        if (!leftType.isBoolean() || !rightType.isBoolean()){
            throw new ContextualError("Opération booléenne '" + getOperatorName() + "' interdite entre " + leftType + " et " +
            rightType + " (règle 3.33)", getLocation());
        }

        setType(leftType);
        return leftType;
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // label
        int cmp = compiler.getLabelId();
        String labelName = get_Name() + "_end_" +cmp;
        Label fin = new Label(labelName);

        // we start with the left
        getLeftOperand().codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();

        // We stop at a value(1 pour OR, 0 pour AND)
        compiler.addInstruction(new CMP(new ImmediateInteger(get_Value()), R_target));
        // if equal we jump to end
        compiler.addInstruction(new BEQ(fin));
        // we free the register target
        compiler.getRegisterAllocater().freeRegister();
        // if not now we calculate the right
        getRightOperand().codeGenInst(compiler);

        compiler.addLabel(fin);

    }

    protected abstract String get_Name();
    protected abstract int get_Value();

}
