package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class AppelMethode extends AbstractExpr {
    private final AbstractExpr instance;
    private final AbstractIdentifier methodeCible;
    private final ListExpr parametres;

    public AppelMethode(AbstractExpr instance, AbstractIdentifier methodeCible, ListExpr parametres) {
        this.instance = instance;
        this.methodeCible = methodeCible;
        this.parametres = parametres;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type instType = instance.verifyExpr(compiler, localEnv, currentClass);

        if(!instType.isClassOrNull()) {
            throw new ContextualError("The type of the instance is not a class", getLocation());
        }

        if(instType.isNull()) {
            throw new ContextualError("Cannot call method on a null object", getLocation());
        }

        ClassDefinition classDef = instType.asClassType("Error: not a class type", getLocation()).getDefinition();

        ExpDefinition def = classDef.getMembers().get(methodeCible.getName());
        if(def==null){
            throw new ContextualError("The method " + methodeCible.getName().getName() + " does not exist in class " + instType.getName().getName(), getLocation());
        }

        if (!def.isMethod()) {
            throw new ContextualError(methodeCible.getName().getName() + " is not a method", getLocation());
        }

        MethodDefinition methDef = (MethodDefinition) def;

        Signature sig = methDef.getSignature();
        int nExp = sig.size();
        int nParam = parametres.getList().size();

        if(nParam != nExp) {
            throw new ContextualError("Number of parameters does not match number of arguments", getLocation());
        }

        for (int i = 0; i < nExp; i++) {
            AbstractExpr arg = parametres.getList().get(i);
            Type expType = sig.paramNumber(i);

            arg.verifyRValue(compiler, localEnv, currentClass, expType);
        }

        methodeCible.setDefinition(methDef);
        methodeCible.setType(methDef.getType());

        setType(methDef.getType());
        return methDef.getType();
    }
    @Override
    public void codeGenInst(DecacCompiler compiler) {

        int number_Args = parametres.size();
        // get place for arg+vtable_adress
        compiler.addInstruction(new ADDSP(new ImmediateInteger(number_Args + 1)));
        // empiler the arguments droite a gauche
        int offset = -number_Args;
        for (AbstractExpr arg : parametres.getList()) {
            arg.codeGenInst(compiler);
            GPRegister regArg  = compiler.getRegisterAllocater().getCurrentRegister();
            compiler.addInstruction(new STORE(regArg, new RegisterOffset(offset, Register.SP)));
            offset++;
        }

        // put this in stask (0)SP
        instance.codeGenInst(compiler);
        GPRegister regThis = compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new STORE(regThis, new RegisterOffset(0, Register.SP)));
        compiler.getRegisterAllocater().freeRegister();
        GPRegister new_register =compiler.getRegisterAllocater().allocateRegister();
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), new_register));
        compiler.addInstruction(new CMP(new NullOperand(), new_register));
        compiler.addInstruction(new BEQ(new Label("null_pointer_error")));
        // get the Vtable
        compiler.addInstruction(new LOAD(new RegisterOffset(0, new_register), new_register));
        // call the method
        MethodDefinition methodDef = (MethodDefinition) methodeCible.getDefinition();
        compiler.addInstruction(new BSR(new RegisterOffset(methodDef.getIndex(), new_register)));
        compiler.getRegisterAllocater().freeRegister();
        // clean the stack
        compiler.addInstruction(new SUBSP(new ImmediateInteger(number_Args + 1)));
        //copy the result(in R0) to our currentRegister
        GPRegister R_target = compiler.getRegisterAllocater().allocateRegister();
        compiler.addInstruction(new LOAD(Register.R0, R_target));
    }


    public void decompile(IndentPrintStream s) {

    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.instance.prettyPrint(s, prefix, false);
        this.methodeCible.prettyPrint(s, prefix, false);
        this.parametres.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        this.instance.iter(f);
        this.methodeCible.iter(f);
        this.parametres.iter(f);
    }
}