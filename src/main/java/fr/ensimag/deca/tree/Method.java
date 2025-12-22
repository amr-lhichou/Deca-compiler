package fr.ensimag.deca.tree;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class Method extends AbstractDeclMeth {
    private final AbstractIdentifier signatureType;
    private final AbstractIdentifier nomAppel;
    private final ListDeclPara entrees;
    private final ListDeclVar declarationsLocales;
    private final ListInst corpsMethode;

    public Method(AbstractIdentifier signatureType, AbstractIdentifier nomAppel,
                  ListDeclPara entrees, ListDeclVar declarationsLocales, ListInst corpsMethode) {
        this.signatureType = signatureType;
        this.nomAppel = nomAppel;
        this.entrees = entrees;
        this.declarationsLocales = declarationsLocales;
        this.corpsMethode = corpsMethode;
    }

    public void decompile(IndentPrintStream s) {
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.signatureType.prettyPrint(s, prefix, false);
        this.nomAppel.prettyPrint(s, prefix, false);
        this.entrees.prettyPrint(s, prefix, false);
        this.declarationsLocales.prettyPrint(s, prefix, false);
        this.corpsMethode.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        this.signatureType.iter(f);
        this.nomAppel.iter(f);
        this.entrees.iter(f);
        this.declarationsLocales.iter(f);
        this.corpsMethode.iter(f);
    }
}