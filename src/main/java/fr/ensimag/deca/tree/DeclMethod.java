package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;

public abstract class DeclMethod extends AbstractDeclMeth{
    protected AbstractIdentifier typeMethode;
    protected AbstractIdentifier nomMethode;
    protected ListDeclPara parametres;
    public AbstractIdentifier getTypeMethode() {
        return typeMethode;
    }
    public AbstractIdentifier getNomMethode() {
        return nomMethode;
    }

    public DeclMethod(AbstractIdentifier typeMethode, AbstractIdentifier nomMethode,
                      ListDeclPara parametres){
        this.typeMethode = typeMethode;
        this.nomMethode = nomMethode;
        this.parametres = parametres;
    }

    protected void verifyDeclMeth(DecacCompiler compiler, ClassDefinition currentClass)
                throws ContextualError{
        Type retourType = this.typeMethode.verifyType(compiler);
        Signature sig = this.parametres.verifyListDeclParaTypes(compiler);
        ClassDefinition superClass = currentClass.getSuperClass();
        if (superClass != null){
            ExpDefinition defSuper = superClass.getMembers().get(nomMethode.getName());
            if (defSuper != null){
                if (!defSuper.isMethod()){
                    throw new ContextualError("On ne peut pas redéfinir une methode (règle 2.7)", getLocation());
                }
                MethodDefinition methSuper = (MethodDefinition) defSuper;
                if (!methSuper.getSignature().equals(sig)){
                    throw new ContextualError("La signature " + methSuper.getSignature() + " ne correspond pas à la signature " + 
                    sig + " (règle 2.7)", getLocation());
                }

                if (!retourType.isSubTypeOf(superClass.getType())){
                    throw new ContextualError("Le type de retour " + retourType + " doit être un sous type de " +
                     superClass.getType() + " (règle 2.7)", getLocation());
                }
            }
        }
        int index = currentClass.getNumberOfMethods();
        MethodDefinition methDef = new MethodDefinition(retourType, getLocation(), sig, index);
        try {
            currentClass.getMembers().declare(nomMethode.getName(), methDef);
        } catch (Exception e) {
            throw new ContextualError("Méthode" + nomMethode.getName() + "existe déjà.", getLocation());
        }
    }

}
