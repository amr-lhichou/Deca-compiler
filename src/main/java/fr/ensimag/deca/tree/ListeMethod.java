package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;


public class ListeMethod extends TreeList<AbstractDeclMeth>{
    public void decompile(IndentPrintStream s){}

    // pass 2
    public void verifyListMethods(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {

        for (AbstractDeclMeth declMeth : getList()){
            declMeth.verifyDeclMeth(compiler, currentClass);
        }
    }

    // pass 3
    void verifyListMethodsBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {

        for (AbstractDeclMeth declMeth : getList()) {
            declMeth.verifyMethBody(compiler, currentClass);
        }
    }
}