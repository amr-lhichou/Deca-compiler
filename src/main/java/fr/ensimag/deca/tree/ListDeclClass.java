package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;


/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        // throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass decClass : getList()){
            decClass.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass decClass : getList()){
            decClass.verifyClassMembers(compiler);
        }
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass decClass : getList()){
            decClass.verifyClassBody(compiler);
        }
    }


    public void codeGenListDeclClass(DecacCompiler compiler) {
        codeGenObjecttable(compiler);
        compiler.setCurrent_index(3);
        for (AbstractDeclClass c : getList()) {
            DeclClass dc = (DeclClass) c;
            ClassDefinition currentClass=dc.getName().getClassDefinition();
            currentClass.setIndex_vtable(compiler.getCurrent_index());
            int index_parent;
            if (currentClass.getSuperClass() != null){
                //System.out.println("Class"+ dc.getName().getName().getName()+"extends" + dc.getSuperclass().getName().getName());
                index_parent=currentClass.getSuperClass().getIndex_vtable();
            } else {
                // cette partie du else n est jamais activée -- à corriger par hossam/ insaf la superclass si A n'a pas de parent
                // si le parent de A est object --- superclass doit etre null .
                index_parent=1 ;
            }
            compiler.setCurrent_index(dc.codeGenTableConstruction(compiler, index_parent, compiler.getCurrent_index(), getList()));
            
        }
        compiler.setVar_size(compiler.getCurrent_index());
        compiler.setStack_size(compiler.getCurrent_index());

    }
    public void codeGenObjecttable(DecacCompiler compiler) {
        Label label_equals = new Label("code.Object.equals");
        compiler.addLabel(label_equals);
        compiler.addComment("Code de la table des méthodes de Object");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, Register.GB)));
        compiler.addInstruction(new LOAD(new LabelOperand(label_equals), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, Register.GB)));
    }
    public void codeGenMethods(DecacCompiler compiler) {
        compiler.addComment("Début d'initialisation des Champs.");

        for (AbstractDeclClass c : getList()) {
            DeclClass dc = (DeclClass) c;
            ClassDefinition currentClass = dc.getName().getClassDefinition();
            if (dc.getFields() != null) {
                dc.getFields().codeGenListChamps(compiler, currentClass);
            }
        }

        compiler.addComment("Définition des Methodes");
        for (AbstractDeclClass classDec : getList()) {
            DeclClass decClass = (DeclClass) classDec;
            ClassDefinition currentClass = decClass.getName().getClassDefinition();

            if (decClass.getMethods() != null) {
                decClass.getMethods().codeGenListMethods(compiler, currentClass);
            }
        }
    }
}
