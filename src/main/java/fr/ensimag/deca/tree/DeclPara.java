package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

public class DeclPara extends AbstractDeclPara {
    private AbstractIdentifier typeArgument;
    private AbstractIdentifier nomArgument;

    public DeclPara(AbstractIdentifier typeArgument, AbstractIdentifier nomArgument) {
        this.typeArgument = typeArgument;
        this.nomArgument = nomArgument;
    }

    public AbstractIdentifier getTypeArgument() {
        return typeArgument;
    }

    public AbstractIdentifier getNomArgument() {
        return nomArgument;
    }

    protected Type verifyParamType(DecacCompiler compiler) throws ContextualError{
        Type paramType = this.typeArgument.verifyType(compiler);
        if (paramType.isVoid()){
            throw new ContextualError("Le paramètre ne peut pas être de type void (règle 2.9)",
                                    this.getLocation());
        }
        return paramType;
    }
    protected void verifyParamExp(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError{
        Type paraType = this.typeArgument.verifyType(compiler);
        //EnvironmentExp envExp = new EnvironmentExp(localEnv);
        if(paraType.isVoid()){
            throw new ContextualError("Le paramètre ne peut pas être de type void (règle 2.9)", getLocation());
        }

        Symbol nomParam = this.nomArgument.getName();
        ParamDefinition defPara = new ParamDefinition(paraType, this.getLocation());
        try {
            localEnv.declare(nomParam, defPara);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Le paramètre " + nomParam.getName() + " est déjà défini", getLocation());
        }

        nomArgument.setDefinition(defPara);
    }

    public void codeGenParam(DecacCompiler compiler, int paramOffset) {
        ((ParamDefinition) nomArgument.getDefinition()).setOperand(
            new RegisterOffset(paramOffset, Register.LB));
    }


    @Override
    public void decompile(IndentPrintStream s) {
        // { r := type.’ ’.name}
        this.typeArgument.decompile(s);
        s.print(" ");
        this.nomArgument.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.typeArgument.prettyPrint(s, prefix, false);
        this.nomArgument.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.typeArgument.iter(f);
        this.nomArgument.iter(f);
    }
}