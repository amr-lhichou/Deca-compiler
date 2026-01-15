package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentType;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.List;

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

public int codeGenTableConstruction(DecacCompiler compiler, int index_parent, int index, List<AbstractDeclClass> list) {
    compiler.addComment("Code de la table des méthodes de " + name.getName().getName());

    compiler.addInstruction(new LEA(new RegisterOffset(index_parent, Register.GB), Register.R0));
    compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
    index++;

    compiler.addInstruction(new LOAD(new RegisterOffset(index_parent + 1, Register.GB), Register.R0));
    compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
    index++;

    DeclClass class_parent = null;
    String nomSuper = this.superclass.getName().getName();

    for (AbstractDeclClass c : list) {
        if (((DeclClass)c).getName().getName().getName().equals(nomSuper)) {
            class_parent = (DeclClass) c;
            break;
        }
    }
    if (class_parent != null) {
        for (AbstractDeclMeth  mParentAbs : class_parent.getMethods().getList()) {
            String nomMethodeParent = ((Identifier)((DeclMethod)mParentAbs).getNomMethode()).getName().getName();
            boolean isOverride = false;
            for (AbstractDeclMeth mMoiAbs : methods.getList()) {
                String nomMethodeMoi = ((DeclMethod)mMoiAbs).getNomMethode().getName().getName();
                if (nomMethodeMoi.equals(nomMethodeParent)) {
                    isOverride = true;
                    break;
                }
            }

            if (isOverride) {
                // it's ovveridden
                String label = "code." + name.getName().getName() + "." + nomMethodeParent;
                compiler.addInstruction(new LOAD(new LabelOperand(new Label(label)), Register.R0));
            } else {
                // it has the same name as the parent
                String label = "code." + nomSuper + "." + nomMethodeParent;
                compiler.addInstruction(new LOAD(new LabelOperand(new Label(label)), Register.R0));
            }

            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
            index++;
        }
    }

    for (AbstractDeclMeth mMoiAbs : methods.getList()) {
        String nomMethodeMoi = ((DeclMethod)mMoiAbs).getNomMethode().getName().getName();
        boolean existsInParent = false;
        if (class_parent != null) {
            for (AbstractDeclMeth mParentAbove : class_parent.getMethods().getList()) {
                String nomParent = ((DeclMethod)mParentAbove).getNomMethode().getName().getName();
                if (nomParent.equals(nomMethodeMoi)) {
                    //it's already written above by the parent
                    existsInParent = true;
                    break;
                }
            }
        }
        // if the method exist only in the son we write it
        if (!existsInParent) {
            String label = "code." + name.getName().getName() + "." + nomMethodeMoi;
            compiler.addInstruction(new LOAD(new LabelOperand(new Label(label)), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
            index++;
        }
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
