package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentType;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

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
        
    }

    @Override
    public void codeGenTableConstruction(DecacCompiler compiler) {
        compiler.addComment("Code de la table des méthodes de " + name.getName());
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
