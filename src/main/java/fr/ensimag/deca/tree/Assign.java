package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import java.lang.reflect.Field;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
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

        if ( getLeftOperand() instanceof AccesChamp) {
            FieldDefinition fieldDef = ((AccesChamp) getLeftOperand()).getIdentifiantChamp().getFieldDefinition();
            if (fieldDef.isFinal()) {
                throw new ContextualError("Cannot assign to final field ", getLocation());
            }
        }
        else if ( getLeftOperand() instanceof Identifier) {
            ExpDefinition def = ((Identifier) getLeftOperand()).getExpDefinition();

            if(def.isField()) {
                FieldDefinition fieldDef = (FieldDefinition) def;
                if (fieldDef.isFinal()) {
                    throw new ContextualError("Cannot assign to final field ", getLocation());
                }
            }
        }



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
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // we generate the code for the right part
        getRightOperand().codeGenInst(compiler);
        GPRegister rightResult = compiler.getRegisterAllocater().getCurrentRegister();

        // On récupère la valeur de l expression a gauche
        AbstractLValue leftOp = getLeftOperand();

        // On récupère l'adresse calculée lors de l'étape de déclaration
        DAddr addr = leftOp.getExpDefinition().getOperand();

        // On store dans Adresse
        compiler.addInstruction(new STORE(rightResult, addr));
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2) {
    }
}


