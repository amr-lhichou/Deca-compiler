package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

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

        // L'addition est arithmétique seulement
        if (!(leftType.isInt() || leftType.isFloat()) || !(rightType.isInt() || rightType.isFloat())){
            throw new ContextualError("Opération interdite entre " + leftType + " et " + rightType, getLocation());
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
        // Overflow test
        compiler.addInstruction(new BOV(new Label("arithmetic_overflow_error")));
    }
    protected abstract void codeGenOp(DecacCompiler compiler, GPRegister op1, GPRegister op2);

}
