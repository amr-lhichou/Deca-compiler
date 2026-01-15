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
                throws ContextualError {
        Type retourType = this.typeMethode.verifyType(compiler);
        Signature sig = this.parametres.verifyListDeclParaTypes(compiler);
        ClassDefinition superClass = currentClass.getSuperClass();

        ExpDefinition defSuper = null;
        if (superClass != null) {
            defSuper = superClass.getMembers().get(nomMethode.getName());
        }
        int finalIndex;
        if (defSuper != null && defSuper.isMethod()) {
            MethodDefinition methSuper = (MethodDefinition) defSuper;
            if (!methSuper.getSignature().equals(sig)) {
                throw new ContextualError("La signature " + methSuper.getSignature() + " ne correspond pas à la signature " +
                        sig + " (règle 2.7)", getLocation());
            }

            if (!retourType.isSubTypeOf(methSuper.getType())) {
                throw new ContextualError("Le type de retour " + retourType + " doit être un sous type de " +
                        methSuper.getType() + " (règle 2.7)", getLocation());
            }
            finalIndex = methSuper.getIndex();

        } else {
            //nouvelle méthode
            //either superclass is null or not a method
            finalIndex = currentClass.getNumberOfMethods() + 1;
            currentClass.setNumberOfMethods(finalIndex);
        }

        MethodDefinition methDef = new MethodDefinition(retourType, getLocation(), sig, finalIndex);
        try {
            currentClass.getMembers().declare(nomMethode.getName(), methDef);
            this.nomMethode.setDefinition(methDef);
        } catch (Exception e) {
            throw new ContextualError("Méthode" + nomMethode.getName() + "existe déjà.", getLocation());
        }
    }

}
