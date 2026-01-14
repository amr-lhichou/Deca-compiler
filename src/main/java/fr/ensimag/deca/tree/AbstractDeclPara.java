package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.Type;

public abstract class AbstractDeclPara extends Tree {
    public abstract AbstractIdentifier getTypeArgument();
    public abstract AbstractIdentifier getNomArgument();
    protected abstract Type verifyParamType(DecacCompiler compiler) throws ContextualError;
    protected abstract void verifyParamExp(DecacCompiler compiler,
            EnvironmentExp localEnv) throws ContextualError;

}