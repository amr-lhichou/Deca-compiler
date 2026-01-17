package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {


        Type rightType = this.verifyExpr(compiler, localEnv, currentClass);

        if (!expectedType.isCompatible(rightType)) {
            throw new ContextualError(
                rightType + " et " + expectedType + " ne sont pas compatibles (règle 3.28)",
                getLocation()
            );
        }

        // Convertir si an assigne un int à un flotant ex : float x; = 1;
        if (rightType.isInt() && expectedType.isFloat()){
            ConvFloat rightConv = new ConvFloat(this);
            rightConv.verifyExpr(compiler, localEnv, currentClass);
            return rightConv;
        }

        return this;

    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type condType = this.verifyExpr(compiler, localEnv, currentClass);

        if (!condType.isBoolean()){
            throw new ContextualError("Condition doit être de type booléen non pas " + condType +
            " (règle 3.29)",getLocation());
        }

    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void  codeGenPrint(DecacCompiler compiler, boolean printHex) {

        this.codeGenInst(compiler);

        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();

        // Affichage selon le type
        if (getType().isInt()) {
            // we charge the result on R1 for the print
            compiler.addInstruction(new LOAD(R_target, Register.R1));
            compiler.addInstruction(new WINT());
        }
        else if (getType().isFloat()) {
            compiler.addInstruction(new LOAD(R_target, Register.R1));
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        }
        else if (getType().isBoolean()) {
            int id = compiler.getLabelId();
            Label labelFalse = new Label("print_false_" + id);
            Label labelEnd = new Label("print_end_" + id);

            // if R_target == 0, we jump
            compiler.addInstruction(new CMP(new ImmediateInteger(0), R_target));
            compiler.addInstruction(new BEQ(labelFalse));

            // if TRUE
            compiler.addInstruction(new WSTR("true"));
            compiler.addInstruction(new BRA(labelEnd));

            // if FALSE
            compiler.addLabel(labelFalse);
            compiler.addInstruction(new WSTR("false"));

            compiler.addLabel(labelEnd);
//        // si STRING : on a pas de calcul on affiche directement
//
//        this.codeGenInst(compiler);
//        // Tous les instance passe par codeGenInst.
//        // le calcul est stocké dans  R2
//        // on affiche Selon le type
//        if (getType().isInt()) {
//            // charge R2 dans R1 pour laffichage
//            compiler.addInstruction(new LOAD(Register.getR(2), Register.R1));
//            compiler.addInstruction(new WINT());
//        }
//        else if (getType().isFloat()) {
//            compiler.addInstruction(new LOAD(Register.getR(2), Register.R1));
//            if (printHex) {
//                compiler.addInstruction(new WFLOATX()); // en Hexa
//            } else {
//                compiler.addInstruction(new WFLOAT());  // en Décimal
//            }
//        }
//        else if (getType().isBoolean()) {
//            // On a le résultat  0 ou 1 dans R2.
//            int id = compiler.getLabelId();
//            Label labelFalse = new Label("print_false_" + id);
//            Label labelEnd = new Label("print_end_" + id);
//
//            // if R2 = 0, on saute pour afficher R2
//            compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(2)));
//            compiler.addInstruction(new BEQ(labelFalse));
//            // if R2=1 (true)
//            compiler.addInstruction(new WSTR("true"));
//            compiler.addInstruction(new BRA(labelEnd));
//            // cas false
//            compiler.addLabel(labelFalse);
//            compiler.addInstruction(new WSTR("false"));
//            // FIN
//            compiler.addLabel(labelEnd);
        }
        compiler.getRegisterAllocater().freeRegister();
    }



    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
