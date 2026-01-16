package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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