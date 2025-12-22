package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class AccesChamp extends AbstractLValue {
    private AbstractExpr objetContexte;
    private AbstractIdentifier identifiantChamp;

    public AccesChamp(AbstractExpr objetContexte, AbstractIdentifier identifiantChamp) {
        this.objetContexte = objetContexte;
        this.identifiantChamp = identifiantChamp;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null; // À remplir en Partie B (houssam_amr)
    }

    public void decompile(IndentPrintStream s) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.objetContexte.prettyPrint(s, prefix, false);
        this.identifiantChamp.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        this.objetContexte.iter(f);
        this.identifiantChamp.iter(f);
    }
}
