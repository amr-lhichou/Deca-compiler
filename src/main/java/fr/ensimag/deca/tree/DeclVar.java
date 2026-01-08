package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import org.apache.commons.lang.Validate;

/**
 * @author gl53
 * @date 01/01/2026
 */
public class DeclVar extends AbstractDeclVar {
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public AbstractIdentifier getVarName() {
        return varName;
    }

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // System.out.println("Declaration de la variable " + varName.getName());
        Type varType = type.verifyType(compiler);
        if (varType.isVoid()){
            throw new ContextualError("Variables de type void non acceptées (règle 3.17)", 
            getLocation());
        }

        VariableDefinition varDef = new VariableDefinition(varType, varName.getLocation());

        try {
            localEnv.declare(varName.getName(), varDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(
                "Variable " + varName.getName() + " déjà définie (règle 3.17)",
                varName.getLocation()
            );
        }

        varName.setDefinition(varDef);

        initialization.verifyInitialization(
        compiler, varType, localEnv, currentClass
        );
    }

    @Override
    public void codegenDeclVar(DecacCompiler compiler) {
        // on calcule l'adresse
        DAddr addr = varName.getExpDefinition().getOperand();
        // on calcule le type
        Type type = varName.getExpDefinition().getType();
        // on genere le code de l'initialisation ou la non initialisation
        initialization.codeGenInit(compiler, addr, type);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        // { r := type.’ ’.name.init.’;’}
        this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        this.initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
