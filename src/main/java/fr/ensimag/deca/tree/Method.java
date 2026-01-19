package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;

import java.io.PrintStream;
import fr.ensimag.deca.context.MethodDefinition;

public class Method extends DeclMethod {
    private final ListDeclVar declarationsLocales;
    private final ListInst corpsMethode;

    public Method(AbstractIdentifier signatureType, AbstractIdentifier nomAppel,
                  ListDeclPara entrees, ListDeclVar declarationsLocales, ListInst corpsMethode) {
        super(signatureType, nomAppel, entrees);
        this.declarationsLocales = declarationsLocales;
        this.corpsMethode = corpsMethode;
    }

   protected void verifyMethBody(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError{

        MethodDefinition methDef = (MethodDefinition) currentClass.getMembers().get(this.nomMethode.getName());
        EnvironmentExp envPara = parametres.verifyListDeclPara(compiler);
        EnvironmentExp envLocals =new EnvironmentExp(envPara);
        declarationsLocales.verifyListDeclVariable(compiler, envLocals, currentClass);
        corpsMethode.verifyListInst(compiler, envLocals, currentClass, methDef.getType());

        Type returnType = methDef.getType();
        if (!returnType.isVoid() && !corpsMethode.endsWithReturn()) {
            throw new ContextualError(
                "Absence d'un retour pour une methode non void '" + returnType + "'(règle 3.24)",
                this.getLocation()
            );
        }

    }

    @Override
    public void codeGenMethod(DecacCompiler compiler, ClassDefinition currentClass){
        

        compiler.addComment("Code de la méthode " + this.nomMethode.getName());
        Label methodLabel = new Label("code." + currentClass.getType().getName() + "." + this.nomMethode.getName());
        Label endLabel = new Label("fin." + currentClass.getType().getName() + "." + this.nomMethode.getName());
        ((MethodDefinition) this.nomMethode.getDefinition()).setLabel(methodLabel);
        compiler.addLabel(methodLabel);

        int limit = compiler.getCompilerOptions().getRegisters();
        int rMax = limit - 1;
        // stack check
        compiler.addInstruction(new TSTO(new ImmediateInteger(12+rMax)));
        compiler.addInstruction(new BOV(new Label("stack_overflow_error")));
        for (int r = 2; r <= rMax; r++) {
            compiler.addInstruction(new PUSH(Register.getR(r)));
        }

        // gen des parametres
        if (this.parametres != null){
            this.parametres.codeGenParams(compiler);
        }

        int nbLocals = 0;

        // gen variables
        if (this.declarationsLocales != null){
            nbLocals = this.declarationsLocales.codeGenListDeclVarInstMethod(compiler);
            if (nbLocals > 0){
                compiler.addInstruction(new ADDSP(new ImmediateInteger(nbLocals)));
            }
        }
        Label oldLabel = compiler.getCurrentMethodEndLabel();
        compiler.setCurrentMethodEndLabel(endLabel);

        // gen corps de la methode
        if (corpsMethode != null) {
            compiler.addComment("----corps-method-----");
            corpsMethode.codeGenListInst(compiler);
            compiler.addComment("------end-corps-method-----");
        }
        compiler.setCurrentMethodEndLabel(oldLabel);

        compiler.addLabel(endLabel);

        if (nbLocals > 0){
            compiler.addInstruction(new SUBSP(new ImmediateInteger(nbLocals)));
        }
        // Restauration
        for (int r = rMax; r >= 2; r--) {
            compiler.addInstruction(new POP(Register.getR(r)));
        }
        compiler.addInstruction(new RTS());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nom methode et parametres
        this.typeMethode.decompile(s);
        s.print(" ");
        this.nomMethode.decompile(s);
        s.print("(");
        parametres.decompile(s);
        s.print(") ");

        // { r := ’{’.vars.insts.’}’}
        s.println("{");
        this.declarationsLocales.decompile(s);
        this.corpsMethode.decompile(s);
        s.print("}");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.typeMethode.prettyPrint(s, prefix, false);
        this.nomMethode.prettyPrint(s, prefix, false);
        this.parametres.prettyPrint(s, prefix, false);
        this.declarationsLocales.prettyPrint(s, prefix, false);
        this.corpsMethode.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.typeMethode.iter(f);
        this.nomMethode.iter(f);
        this.parametres.iter(f);
        this.declarationsLocales.iter(f);
        this.corpsMethode.iter(f);
    }
}