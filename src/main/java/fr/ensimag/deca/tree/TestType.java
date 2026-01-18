package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class TestType extends AbstractExpr {
    private final AbstractExpr object;
    private final AbstractIdentifier typeVise;

    public TestType(AbstractExpr object, AbstractIdentifier typeVise) {
        this.object = object;
        this.typeVise = typeVise;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type lefType = object.verifyExpr(compiler, localEnv, currentClass);
        Type rigType = typeVise.verifyType(compiler);

        if(!rigType.isClass()) {
            throw new ContextualError("The right operand of the 'instanceof' operator must be a class type.", typeVise.getLocation());
        }

        if(!lefType.isClassOrNull()) {
            throw new ContextualError("The left operand of the 'instanceof' operator must be a class type or null.", object.getLocation());
        }

        Type resultType = compiler.environmentType.BOOLEAN;
        setType(resultType);
        return resultType;
    }

        public void codeGenInst(DecacCompiler compiler) {
        compiler.addComment("---- Begin___Instance___of----");
        int cmp =compiler.getLabelId();
        Label labelTrue = new Label("instanceof_true_" + cmp);
        Label labelFalse = new Label("instanceof_false_" + cmp);
        Label labelEnd = new Label("instanceof_end_" + cmp);
        Label labelLoop = new Label("instanceof_loop_" + cmp);
        object.codeGenInst(compiler);
        GPRegister R_target =compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new CMP(new NullOperand(), R_target));
        compiler.addInstruction(new BEQ(labelFalse));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, R_target), R_target));
        ClassDefinition other_type = (ClassDefinition) typeVise.getDefinition();
        compiler.addInstruction(new LEA(new RegisterOffset(other_type.getIndex_vtable(),Register.GB), Register.R0));
        compiler.addLabel(labelLoop);
        compiler.addInstruction(new CMP(Register.R0, R_target));
        compiler.addInstruction(new BEQ(labelTrue));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, R_target), R_target));
        compiler.addInstruction(new CMP(new NullOperand(), R_target));
        // if not null we continue to look for the parent
        compiler.addInstruction(new BNE(labelLoop));
        compiler.addLabel(labelFalse);
        compiler.addInstruction(new LOAD(0, R_target));
        compiler.addInstruction(new BRA(labelEnd));
        compiler.addLabel(labelTrue);
        compiler.addInstruction(new LOAD(1, R_target));
        compiler.addLabel(labelEnd);
        compiler.addComment("---- End___Instance___of----");
    }
    @Override
    public void decompile(IndentPrintStream s) {
        object.decompile(s);
        s.print(" instanceof ");
        typeVise.decompile(s);
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        object.iter(f);
        typeVise.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        object.prettyPrint(s, prefix, false);
        typeVise.prettyPrint(s, prefix, true);
    }

}