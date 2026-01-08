package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //calcul dans R2
        getOperand().codeGenInst(compiler);
        GPRegister R_target = compiler.getRegisterAllocater().getCurrentRegister();
       // appel de la méthode abstraite
        codeGenUnaryOp(compiler,R_target );
    }

    // Nouvelle méthode abstraite
    protected abstract void codeGenUnaryOp(DecacCompiler compiler, GPRegister op);


    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {

        // pour le not et le unaryMinus on afiche ex: (-x); (!x);

        s.print("(" + getOperatorName());
        getOperand().decompile(s);
        s.print(")");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
