package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class AccesChamp extends AbstractLValue {
    private AbstractExpr objetContexte;
    private AbstractIdentifier identifiantChamp;
    public AbstractExpr getObjetContexte() {
        return objetContexte;
    }

    public AbstractIdentifier getIdentifiantChamp() {
        return identifiantChamp;
    }

    public AccesChamp(AbstractExpr objetContexte, AbstractIdentifier identifiantChamp) {
        this.objetContexte = objetContexte;
        this.identifiantChamp = identifiantChamp;

        // il fallait metre le lieu du noeud accesschamp
        this.setLocation(identifiantChamp.getLocation());
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type objetType = objetContexte.verifyExpr(compiler, localEnv, currentClass);
        if (!objetType.isClassOrNull()) {
            throw new ContextualError("The type of the object context is not a class", getLocation());
        }
        if(objetType.isNull()) {
            throw new ContextualError("Cannot access a field on a null object", getLocation());
        }

        
        ClassDefinition classDef = objetType.asClassType("Error: not a class type", getLocation()).getDefinition();
        ExpDefinition champDef = classDef.getMembers().get(identifiantChamp.getName());


        if (champDef == null) {
            throw new ContextualError("The field '" + identifiantChamp.getName().getName() + "' does not exist in class " + classDef.getType().getName().getName(), getLocation());
        }
        if (!champDef.isField()) {
            throw new ContextualError(identifiantChamp.getName().getName() + " is not a field", getLocation());
        }

        FieldDefinition fieldDef = (FieldDefinition) champDef;

        

        if(fieldDef.getVisibility() == Visibility.PROTECTED) {
            if(currentClass == null) {
                throw new ContextualError("Cannot access protected field outside a class", getLocation());
            }

            ClassDefinition owner = fieldDef.getContainingClass();
            ClassType ownerType = currentClass.getType();

            if (!ownerType.isSubClassOf(owner.getType())) {
                throw new ContextualError("Cannot access protected field from unrelated class", getLocation());
            }

            ClassType objetClassType = objetType.asClassType("Error: not a class type", getLocation());

            if(!objetClassType.isSubClassOf(currentClass.getType())) {
                throw new ContextualError("Cannot access protected field on unrelated object", getLocation());
            }
        }

        identifiantChamp.setDefinition(champDef);
        identifiantChamp.setType(champDef.getType());
        setType(champDef.getType());
        return champDef.getType();
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        objetContexte.codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), R_target));
            compiler.addInstruction(new BEQ(new Label("null_pointer_error")));
        }
        FieldDefinition def = identifiantChamp.getFieldDefinition();
        compiler.addInstruction(new LOAD(new RegisterOffset(def.getIndex(), R_target), R_target));
    }




    @Override
    public void decompile(IndentPrintStream s) {
        objetContexte.decompile(s);
        s.print(".");
        identifiantChamp.decompile(s);
    }

    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.objetContexte.prettyPrint(s, prefix, false);
        this.identifiantChamp.prettyPrint(s, prefix, true);
    }

    protected void iterChildren(TreeFunction f) {
        this.objetContexte.iter(f);
        this.identifiantChamp.iter(f);
    }




}
