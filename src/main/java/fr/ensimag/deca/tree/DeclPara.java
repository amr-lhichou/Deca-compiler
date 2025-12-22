package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class DeclPara extends AbstractDeclPara {
    private AbstractIdentifier typeArgument;
    private AbstractIdentifier nomArgument;

    public DeclPara(AbstractIdentifier typeArgument, AbstractIdentifier nomArgument) {
        this.typeArgument = typeArgument;
        this.nomArgument = nomArgument;
    }

    public void decompile(IndentPrintStream s) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.typeArgument.prettyPrint(s, prefix, false);
        this.nomArgument.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        this.typeArgument.iter(f);
        this.nomArgument.iter(f);
    }
}