package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;


public class MethodASM extends AbstractDeclMeth {
    private final AbstractIdentifier typeRetour;
    private final AbstractIdentifier etiquette;
    private final ListDeclPara listeParametres;
    private final StringLiteral blocAsm;

    public MethodASM(AbstractIdentifier typeRetour, AbstractIdentifier etiquette,
                     ListDeclPara listeParametres, StringLiteral blocAsm) {
        this.typeRetour = typeRetour;
        this.etiquette = etiquette;
        this.listeParametres = listeParametres;
        this.blocAsm = blocAsm;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // { r := ’asm(’.code.’);’}
        s.print("asm(");
        this.blocAsm.decompile(s);
        s.print(");");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.typeRetour.prettyPrint(s, prefix, false);
        this.etiquette.prettyPrint(s, prefix, false);
        this.listeParametres.prettyPrint(s, prefix, false);
        this.blocAsm.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.typeRetour.iter(f);
        this.etiquette.iter(f);
        this.listeParametres.iter(f);
        this.blocAsm.iter(f);
    }
}