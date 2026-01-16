package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;

public class ListDeclPara extends TreeList<AbstractDeclPara> {

    @Override
    public void decompile(IndentPrintStream s) {
        // {r := (r = ε ? ε : r.’,’).param} ) ∗ ]
        boolean first = true;
        for (AbstractDeclPara param : this.getList()){
            if (!first){
                s.print(", ");
            }
            param.decompile(s);
            first = false;
        }
    }

    public Signature verifyListDeclParaTypes(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclPara p : getList()) {
            sig.add(p.verifyParamType(compiler));
        }
        return sig;
    }

    public EnvironmentExp verifyListDeclPara(DecacCompiler compiler) throws ContextualError {
       
        EnvironmentExp envPara = new EnvironmentExp(null);
       
        for (AbstractDeclPara parameter : getList()) {
            parameter.verifyParamExp(compiler, envPara);
        }
        return envPara;
    }

}