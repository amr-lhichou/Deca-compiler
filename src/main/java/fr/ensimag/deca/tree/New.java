package fr.ensimag.deca.tree;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.DecacCompiler;


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


    public void decompile(IndentPrintStream s) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.entiteCreer.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        entiteCreer.iter(f);
    }
}
