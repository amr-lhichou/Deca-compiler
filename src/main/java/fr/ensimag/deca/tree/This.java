package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
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


    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        // Obligatoire pour compiler, sera rempli en Partie B par Houssam_Amr
        throw new UnsupportedOperationException("verifyExpr non implémentée pour New attention hh");
    }


    @Override
    protected void iterChildren(TreeFunction f) { }
}