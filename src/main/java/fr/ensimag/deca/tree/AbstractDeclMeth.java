package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclMeth extends Tree {
   // a continue par hossam_amr respo partie B ;)
   protected abstract void verifyDeclMeth(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;
   
   protected abstract void verifyMethBody(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;
}