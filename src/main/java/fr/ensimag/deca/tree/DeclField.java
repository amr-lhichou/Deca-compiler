package fr.ensimag.deca.tree;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;


public class DeclField extends AbstractDeclField{
    private Visibility natureAcces;
    private AbstractIdentifier typeChamp;
    private AbstractIdentifier nomChamp;
    private AbstractInitialization initialisation;

    public DeclField(Visibility natureAcces, AbstractIdentifier typeChamp, AbstractIdentifier nomChamp, AbstractInitialization initialisation) {
        this.natureAcces = natureAcces;
        this.typeChamp = typeChamp;
        this.nomChamp = nomChamp;
        this.initialisation = initialisation;
    }

    public DeclField(AbstractIdentifier nomChamp, Visibility natureAcces, AbstractIdentifier typeChamp, AbstractInitialization initialisation) {
        this.nomChamp = nomChamp;
        this.natureAcces = natureAcces;
        this.typeChamp = typeChamp;
        this.initialisation = initialisation;
    }

    public void decompile(IndentPrintStream s){
        //laisse aussi vide hh pour eviter prob de throw new unsupprtbleoperationexeption
    }

    protected void prettyPrintChildren(PrintStream s,String prefix){
        this.typeChamp.prettyPrint(s,prefix,false);
        this.nomChamp.prettyPrint(s,prefix,false);
        this.initialisation.prettyPrint(s,prefix,false);
    }
    protected void iterChildren(TreeFunction f){
        this.typeChamp.iter(f);
        this.nomChamp.iter(f);
        this.initialisation.iter(f);
    }
}