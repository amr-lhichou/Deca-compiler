package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.DecacCompiler;

public class This extends AbstractExpr {
    public void decompile(IndentPrintStream s) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) { }


    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        //Autoriser l'utilisation de this uniquement dans une classe ou une méthode
        if(currentClass == null) {
            throw new ContextualError("Use of this outside a class or method", getLocation());
        }

        Type t = currentClass.getType();
        setType(t);
        return t;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), R_target));
    }


    @Override
    protected void iterChildren(TreeFunction f) { }
}