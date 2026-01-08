package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Conversion extends AbstractExpr {
    AbstractExpr cible;
    AbstractIdentifier typeCible;

    public Conversion(AbstractIdentifier typeCible,AbstractExpr cible) {
        this.cible = cible;
        this.typeCible = typeCible;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        
        Type expType = this.cible.verifyExpr(compiler, localEnv, currentClass);
        Type convType = this.typeCible.verifyType(compiler);

        if (!convType.isCastCompatible(expType)){
            throw new ContextualError("Le type " + expType + " et le type " + convType +
            " ne sont pas cast_compatible (règle 3.39)", getLocation());
        }

        setType(convType);
        return convType; 
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        typeCible.decompile(s);
        s.print(") (");
        cible.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {}

}