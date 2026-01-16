package fr.ensimag.deca.tree;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;



public class New extends AbstractExpr {
    private AbstractIdentifier entiteCreer;

    public New(AbstractIdentifier entiteCreer) {
        this.entiteCreer = entiteCreer;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = entiteCreer.verifyType(compiler);
        if(!t.isClass()) {
            throw new ContextualError("New can only be used with class types", getLocation());
        }
        setType(t);
        return t;
    }


    @Override
    public void codeGenInst(DecacCompiler compiler) {

        ClassDefinition def = (ClassDefinition) entiteCreer.getDefinition();
        int d = def.getNumberOfFields() + 1;
        compiler.addInstruction(new NEW(new ImmediateInteger(d), Register.getR(2)));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(new Label("heap_overflow")));
        }
        compiler.addInstruction(new LEA(new RegisterOffset(def.getIndex_vtable(),Register.GB), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, Register.getR(2))));
        compiler.addInstruction(new PUSH(Register.getR(2)));
        String class_name = entiteCreer.getName().getName();
        compiler.addInstruction(new BSR(new Label("init." + class_name)));
        compiler.addInstruction(new POP(Register.getR(2)));
    }

    public void decompile(IndentPrintStream s) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.entiteCreer.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        entiteCreer.iter(f);
    }
}
