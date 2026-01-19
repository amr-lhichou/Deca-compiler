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
        this.setLocation(methodeCible.getLocation());
        this.instance = instance;
        this.methodeCible = methodeCible;
        this.parametres = parametres;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type instType = instance.verifyExpr(compiler, localEnv, currentClass);

        if(!instType.isClassOrNull()) {
            throw new ContextualError("Le type de l'instance n'est pas une classe (règle 3.41)", getLocation());
        }

        if(instType.isNull()) {
            throw new ContextualError("On ne peut pas appeler une méthode sur un objet null (règle 3.41)", getLocation());
        }

        ClassDefinition classDef = instType.asClassType("Error: n'est pas un type class", getLocation()).getDefinition();

        ExpDefinition def = classDef.getMembers().get(methodeCible.getName());
        if(def==null){
            throw new ContextualError("La méthode '" + methodeCible.getName().getName() + "' n'existe pas dans la classe "
                                        + instType.getName().getName() + " (règle 3.71)", getLocation());}

        if (!def.isMethod()) {throw new ContextualError("'" + methodeCible.getName().getName() +
                                "' n'est pas une méthode (règle 3.71)", getLocation());}

        MethodDefinition methDef = (MethodDefinition) def;

        Signature sig = methDef.getSignature();
        int nExp = sig.size();
        int nParam = parametres.getList().size();

        if(nParam != nExp) {
            throw new ContextualError("Le nombre d'arguments ne correspond pas au nombre de paramètres (règle 3.41)", getLocation());
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
        compiler.addComment("------Begin-Call-Methode------");
        compiler.addInstruction(new ADDSP(new ImmediateInteger(number_Args + 1)));
        // put this in stask (0)SP
        instance.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        compiler.addInstruction(new STORE(R_target, new RegisterOffset(0, Register.SP)));// empiler the arguments droite a gauche
        compiler.getRegisterAllocater().freeRegister();
        compiler.addComment("------Begin-Empiler-paramteres---");
        int offset = -1; // commence après 'this'
        for (AbstractExpr arg : parametres.getList()) {
            arg.codeGenInst(compiler);
            R_target = compiler.getRegisterAllocater().getCurrentRegister();
            compiler.addInstruction(new STORE(R_target, new RegisterOffset(offset, Register.SP)));
            compiler.getRegisterAllocater().freeRegister();
            offset--; // indice décroissant
        }
        compiler.addComment("------FIN-Empiler-paramteres------");


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
        compiler.addComment("------END-Call-Methode------");
        // R_target dernier a été libéré on doit le réallouer
        compiler.getRegisterAllocater().allocateRegister();

    }


    public void decompile(IndentPrintStream s) {
        // Print l'instance 
        instance.decompile(s);
        s.print(".");

        // Print le nom de la methode
        methodeCible.decompile(s);

        // Print les parametres (...)
        s.print("(");
        if (!parametres.getList().isEmpty()) {
            for (int i = 0; i < parametres.getList().size(); i++) {
                parametres.getList().get(i).decompile(s);
                if (i < parametres.getList().size() - 1) {
                    s.print(", ");
                }
            }
        }
        s.print(")");
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