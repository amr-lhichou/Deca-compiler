package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;


public class ListChamps extends TreeList<AbstractDeclField>{

    @Override
    public void decompile(IndentPrintStream s){
        for (AbstractDeclField field : getList()) {
            field.decompile(s);
            s.println();
        }
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

    protected void codeGenListChamps(DecacCompiler compiler, ClassDefinition currentClass){

        Symbol className = currentClass.getType().getName();

        compiler.addComment("Initialisation des champs de " + className);

        compiler.addLabel(new Label("init." + className));

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));

        compiler.addComment("zero fields introduced by " + className);

        // initialisation à zero
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));

        for (AbstractDeclField field : getList()){
            FieldDefinition defChamp = ((DeclField)field).getNomField().getFieldDefinition();
            DAddr addr = new RegisterOffset(defChamp.getIndex(), Register.R1);
            compiler.addInstruction(new STORE(Register.R0, addr));
        }

        

        ClassDefinition superClass = currentClass.getSuperClass();
        if (superClass != null && superClass.getSuperClass() != null) {
            compiler.addComment("initialisation des champs hérités");

            compiler.addInstruction(new PUSH(Register.R1));
            compiler.addInstruction(new BSR(new Label("init." + currentClass.getSuperClass().getType().getName())));
            compiler.addInstruction(new SUBSP(1));
        }

        for (AbstractDeclField field : getList()){
            field.codeGenField(compiler, className);
        }

        compiler.addInstruction(new RTS());

    }

}