package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

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

    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        Type fieldType = this.typeChamp.verifyType(compiler);

        if (fieldType.isVoid()){
            throw new ContextualError("Le type du champ ne peut pas être void (règle 2.5)", 
                                    getLocation());
        }

        Symbol nom = nomChamp.getName();

        ClassDefinition superClass = currentClass.getSuperClass();

        if (superClass != null){
            if (superClass.getMembers().get(nom) != null && !superClass.getMembers().get(nom).isField()){
                throw new ContextualError("Si le nom " + nom + " existe dans la superClass il est un champ (règle 2.5)", getLocation());
            }
        }

        int index = currentClass.getNumberOfFields();
        FieldDefinition fieldDef = new FieldDefinition(fieldType, getLocation(), natureAcces, currentClass, index);

        try {
            currentClass.getMembers().declare(nom, fieldDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(
                "Champ déjà défini dans la classe",
                getLocation()
            );
        }

    }

    protected void verifyFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError{
        
        Type fieldType = typeChamp.getType();
        initialisation.verifyInitialization(compiler, fieldType, currentClass.getMembers(), currentClass);
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