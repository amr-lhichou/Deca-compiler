package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentType;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl53
 * @date 01/01/2026
 */
public class DeclClass extends AbstractDeclClass {
    private AbstractIdentifier name;
    private AbstractIdentifier superclass;
    private ListChamps fields;
    private ListeMethod methods;
    public ListeMethod getMethods() {
        return methods;
    }
    public AbstractIdentifier getName() {
        return name;
    }
    public AbstractIdentifier getSuperclass() {
        return superclass;
    }
    public DeclClass(AbstractIdentifier name, AbstractIdentifier superclass, ListChamps fields, ListeMethod methods) {
        this.name = name;
        this.superclass = superclass;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //On recupère le type et on verifie la super class
        Type superType = superclass.verifyType(compiler);
        if (!superType.isClass()) throw new ContextualError("Super-classe invalide ", superclass.getLocation());
        ClassDefinition superClassDef = ((ClassType) superType).getDefinition();

        ClassType classType = new ClassType(name.getName(), getLocation(), superClassDef);

        try{
            compiler.environmentType.declare(name.getName(), classType.getDefinition());
        } catch (EnvironmentType.DoubleDefException e){
            throw new ContextualError("Classe " + name.getName() + " déjà définie", name.getLocation());
        }

        name.setType(classType);
        name.setDefinition(classType.getDefinition());

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        ClassDefinition currentClass = this.name.getClassDefinition();
        this.methods.verifyListMethods(compiler, currentClass);
        this.fields.verifyListChamps(compiler, currentClass);
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition currentClass = this.name.getClassDefinition();
        this.methods.verifyListMethodsBody(compiler, currentClass);
        this.fields.verifyListChampsInit(compiler, currentClass);
    }

    private List<String> getRecursiveMethodLabels(List<AbstractDeclClass> list) {
        List<String> vtable;
        // we look for the parent
        DeclClass class_parent = null;
        String nomSuper = this.superclass.getName().getName();
        for (AbstractDeclClass c : list) {
            if (((DeclClass)c).getName().getName().getName().equals(nomSuper)) {
                class_parent = (DeclClass) c;
                break;
            }
        }
        // first get the parent vtable
        if (class_parent != null) {
            vtable = class_parent.getRecursiveMethodLabels(list);
        } else {
            // if there's no parent (Object is the parent) we initialize
            vtable = new ArrayList<>();
            vtable.add("code.Object.equals");
        }
        //and now we add our methods
        for (AbstractDeclMeth my_method : methods.getList()) {
            String nomMethodeMoi = ((DeclMethod)my_method).getNomMethode().getName().getName();
            String labelMoi = "code." + this.getName().getName().getName() + "." + nomMethodeMoi;

            boolean isOverride = false;
            for (int i = 0; i < vtable.size(); i++) {
                String labelExistant = vtable.get(i);
                String nomMethodeExistante = labelExistant.substring(labelExistant.lastIndexOf('.') + 1);
                if (nomMethodeExistante.equals(nomMethodeMoi)) {
                    vtable.set(i, labelMoi);
                    isOverride = true;
                    break;
                }
            }
            if (!isOverride) {
                vtable.add(labelMoi);
            }
        }

        return vtable;
    }

public int codeGenTableConstruction(DecacCompiler compiler, int index_parent, int index, List<AbstractDeclClass> list) {
    compiler.addComment("Code de la table des méthodes de " + name.getName().getName());
    // store parent adress
    compiler.addInstruction(new LEA(new RegisterOffset(index_parent, Register.GB), Register.R0));
    compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
    index++;
    // get his table
    List<String> labels = getRecursiveMethodLabels(list);
    for (String label : labels) {
        compiler.addInstruction(new LOAD(new LabelOperand(new Label(label)), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
        index++;
    }

    return index;
}


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.name.prettyPrint(s,prefix,false);
        this.superclass.prettyPrint(s,prefix,false);
        this.fields.prettyPrint(s,prefix,false);
        this.methods.prettyPrint(s,prefix,false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
