package fr.ensimag.deca.tree;

import fr.ensimag.deca.*;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

/**
 * Explicit cast conversion.
 * Example: (float) 3
 */
public class Casting extends AbstractExpr {
    private  AbstractIdentifier type; // Le type vers lequel on convertit
    private  AbstractExpr operand;    // expression a convertir

    public Casting(AbstractIdentifier type, AbstractExpr operand) {
        this.type = type;
        this.operand = operand;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        // On vérifie que le cast est compatible
        // a implementer par habbouli
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}