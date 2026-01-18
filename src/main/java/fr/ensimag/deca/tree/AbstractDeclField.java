package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public abstract class AbstractDeclField extends Tree {
    protected abstract void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void verifyFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void codeGenField(DecacCompiler compiler, Symbol className);
}