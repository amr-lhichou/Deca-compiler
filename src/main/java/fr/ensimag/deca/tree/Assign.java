package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.context.FieldDefinition;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl53
 * @date 01/01/2026
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        // On doit utiliser verifyRValue
        AbstractExpr rightType = getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);

        setRightOperand(rightType);

        setType(leftType);
        return leftType;

    }


    @Override
    protected String getOperatorName() {
        return "=";
    }
    protected void codeGenInst(DecacCompiler compiler) {
    getRightOperand().codeGenInst(compiler);
    GPRegister Rightresult = compiler.getRegisterAllocater().getCurrentRegister();
    AbstractLValue left = getLeftOperand();
    if (left instanceof AccesChamp) {
        AccesChamp ac = (AccesChamp) left;
        compiler.addInstruction(new PUSH(Rightresult));
        compiler.getRegisterAllocater().freeRegister();
        ac.getObjetContexte().codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), R_target));
            compiler.addInstruction(new BEQ(new Label("null_pointer_error")));
        }
        int offset = ac.getIdentifiantChamp().getFieldDefinition().getIndex();
        compiler.addInstruction(new POP(Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(offset, R_target)));
        compiler.addInstruction(new LOAD(Register.R0, R_target));
    }
    else if (left instanceof Identifier && ((Identifier) left).getExpDefinition().isField()) {
        int off = ((Identifier) left).getFieldDefinition().getIndex();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new STORE(Rightresult, new RegisterOffset(off, Register.R1)));
    }
    // local variable or globale
    else {
        compiler.addInstruction(new STORE(Rightresult, left.getExpDefinition().getOperand()));
    }

}
    

    @Override
    protected void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2) {
    }
}


