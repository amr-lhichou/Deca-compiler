package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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