package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;



public class ListChamps extends TreeList<AbstractDeclField>{
    public void decompile(IndentPrintStream s){
        //pour eviter prob de compilation lever pa le throw dan s listDeclVar ... j la laisse vide pour l instant ...
    }

    public void verifyListChamps(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {

        for (AbstractDeclField declField : getList()){
            declField.verifyDeclField(compiler, currentClass);
        }
    }

    public void verifyListChampsInit(DecacCompiler compiler, ClassDefinition currentClass)
        throws ContextualError {

        for (AbstractDeclField field : getList()) {
            ((DeclField)field).verifyFieldInit(compiler, currentClass);
        }
    }


}