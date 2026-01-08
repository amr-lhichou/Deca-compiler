package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl53
 * @date 01/01/2026
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type typeExpr = this.expression.verifyExpr(compiler, localEnv, currentClass);

        if (!t.isCompatible(typeExpr)){
            throw new ContextualError("Initialisation incompatible : le type " + typeExpr + 
            " est incompatible avec le type " + t + " (règle 3.28)"
            , getLocation());
        }

        // if we initialize float with int we convert int to float ex : float x =1;

        if (t.isFloat() && typeExpr.isInt()){
            ConvFloat convInit = new ConvFloat(getExpression());
            convInit.verifyExpr(compiler, localEnv, currentClass);
            setExpression(convInit);
        }

    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr addr, Type t) {
        // on genere le code de l'expression, elle est calculée dans R2
        getExpression().codeGenInst(compiler);
        // on charge cette valeur dans l 'adresse
        compiler.addInstruction(new STORE(Register.getR(2), addr));
    }


    @Override
    public void decompile(IndentPrintStream s) {

        s.print(" = ");
        this.getExpression().decompile(s);

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
