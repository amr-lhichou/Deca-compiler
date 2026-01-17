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
        // put this in stask (0)SP
        instance.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new STORE(R_target, new RegisterOffset(0, Register.SP)));// empiler the arguments droite a gauche
        int offset = -1; // commence après 'this'
        for (AbstractExpr arg : parametres.getList()) {
            arg.codeGenInst(compiler);
            R_target = compiler.getRegisterAllocater().getCurrentRegister();
            compiler.addInstruction(new STORE(R_target, new RegisterOffset(offset, Register.SP)));
            offset--; // indice décroissant
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), R_target));
        compiler.addInstruction(new CMP(new NullOperand(), R_target));
        compiler.addInstruction(new BEQ(new Label("null_pointer_error")));
        // get the Vtable
        compiler.addInstruction(new LOAD(new RegisterOffset(0, R_target), R_target));
        // call the method
        MethodDefinition methodDef = (MethodDefinition) methodeCible.getDefinition();
        compiler.addInstruction(new BSR(new RegisterOffset(methodDef.getIndex(), R_target)));
        // clean the stack
        compiler.addInstruction(new SUBSP(new ImmediateInteger(number_Args + 1)));
        //copy the result(in R0) to our currentRegister
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