package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type rightOpType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!(leftOpType.isInt() || leftOpType.isFloat()) || !(rightOpType.isInt() || rightOpType.isFloat())){
            throw new ContextualError("Les opérands doivent être de Type INT ou FLOAT (règle 3.33)", getLocation());
        }

        // Convertion si de different type
        if (rightOpType.isFloat() && leftOpType.isInt()){
            setLeftOperand(new ConvFloat(getLeftOperand()));
        } else if (leftOpType.isFloat() && rightOpType.isInt()){
            setRightOperand(new ConvFloat(getRightOperand()));
        }

        Type opCmpType = compiler.environmentType.BOOLEAN;
        setType(opCmpType);
        return opCmpType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //creation des registres
        GPRegister R2 = Register.getR(2);
        GPRegister R3 = Register.getR(3);
        //partie gauche:calcul dans R2
        getLeftOperand().codeGenInst(compiler);

        // sauvegarde de R2 sur la pile
        compiler.addInstruction(new PUSH(R2));

        // partie droite:calcul dans R2
        getRightOperand().codeGenInst(compiler);

        // charge le resulat de partie droite dans R3
        compiler.addInstruction(new LOAD(R2, R3));

        //recuperation de la valeur stockée dans la pile dans R2
        compiler.addInstruction(new POP(R2));

        // effectue l operation binaire (+,/,*....)
        codeGenOp(compiler, R3, R2);
    }
    protected abstract void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2);



}
