package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

public class Conversion extends AbstractExpr {
    AbstractExpr cible;
    AbstractIdentifier typeCible;
    Type expType=null;
    Type convType=null;

    public Conversion(AbstractIdentifier typeCible,AbstractExpr cible) {
        this.cible = cible;
        this.typeCible = typeCible;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        
        Type expType = this.cible.verifyExpr(compiler, localEnv, currentClass);
        Type convType = this.typeCible.verifyType(compiler);
        this.expType = expType;
        this.convType = convType;
        if (!convType.isCastCompatible(expType)){
            throw new ContextualError("Le type " + expType + " et le type " + convType +
            " ne sont pas cast_compatible (règle 3.39)", getLocation());
        }

        setType(convType);
        return convType; 
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        cible.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();

        Type typeSource = cible.getType(); // type de l'expression a droite
        Type typeDest = this.getType();    // vers lequel on convertit

        if (typeSource.isInt() && typeDest.isFloat()) {
            compiler.addInstruction(new FLOAT(R_target, R_target));
        }

        else if (typeSource.isFloat() && typeDest.isInt()) {
            compiler.addInstruction(new INT(R_target, R_target));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        typeCible.decompile(s);
        s.print(") (");
        cible.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
//        typeCible.iter(f);
//        cible.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
//        typeCible.prettyPrint(s, prefix, false);
//        cible.prettyPrint(s, prefix, true);
    }
//    @Override
//    String prettyPrintNode() {
//        return "("+ expType +")" +" to " + "(" + convType + ")";
//    }

}