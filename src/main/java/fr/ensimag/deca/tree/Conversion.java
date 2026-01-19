package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

public class Conversion extends AbstractExpr {
    AbstractExpr cible;
    AbstractIdentifier typeCible;
    Type expType=null;
    Type convType=null;

    public Conversion(AbstractIdentifier typeCible,AbstractExpr cible) {
        this.cible = cible;
        this.typeCible = typeCible;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        
        Type expType = this.cible.verifyExpr(compiler, localEnv, currentClass);
        Type convType = this.typeCible.verifyType(compiler);
        this.expType = expType;
        this.convType = convType;
        if (!convType.isCastCompatible(expType)){
            throw new ContextualError("Le type " + expType + " et le type " + convType +
            " ne sont pas cast_compatible (règle 3.39)", getLocation());
        }

        setType(convType);
        return convType; 
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        typeCible.decompile(s);
        s.print(") (");
        cible.decompile(s);
        s.print(")");
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        cible.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();

        Type srcType = cible.getType();
        Type dstType = this.getType();

        // int TO  float
        if (srcType.isInt() && dstType.isFloat()) {
            compiler.addInstruction(new FLOAT(R_target, R_target));
            return;
        }

        // float TO int
        if (srcType.isFloat() && dstType.isInt()) {
            compiler.addInstruction(new INT(R_target, R_target));
            return;
        }

        // cast dynamique classe/null -> classe
        if (srcType.isClassOrNull() && dstType.isClass()) {
            int id = compiler.getLabelId();
            Label labelOk   = new Label("cast_ok_" + id);
            Label labelLoop = new Label("cast_loop_" + id);
            Label labelError = new Label("cast_error");

            compiler.addInstruction(new CMP(new NullOperand(), R_target));
            compiler.addInstruction(new BEQ(labelOk));
            //we look for the vtable and we compare from parent to parent
            int indexVTable = typeCible.getClassDefinition().getIndex_vtable();
            compiler.addInstruction(new LEA(new RegisterOffset(indexVTable, Register.GB), Register.R1));
            compiler.addInstruction(new LOAD(new RegisterOffset(0, R_target), Register.R0));
            compiler.addLabel(labelLoop);
            compiler.addInstruction(new CMP(Register.R1, Register.R0));
            compiler.addInstruction(new BEQ(labelOk));
            compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
            compiler.addInstruction(new BEQ(labelError));
            compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R0), Register.R0));
            compiler.addInstruction(new BRA(labelLoop));
            compiler.addLabel(labelOk);
        }
    }


    @Override
    protected void iterChildren(TreeFunction f) {
       typeCible.iter(f);
       cible.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
       typeCible.prettyPrint(s, prefix, false);
       cible.prettyPrint(s, prefix, true);
    }
   @Override
   String prettyPrintNode() {
       return "("+ expType +")" +" to " + "(" + convType + ")";
   }

}