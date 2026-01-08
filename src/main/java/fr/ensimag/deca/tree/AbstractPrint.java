package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for (AbstractExpr expr : arguments.getList()) {
            // 1- verif de contexte de expr
            Type exprType = expr.verifyExpr(compiler,localEnv,currentClass);

            // 2- verif de type autorisé 
            if (!(exprType.isInt()
                 ||exprType.isFloat()
                 ||exprType.isString())) {
                   
                throw new ContextualError(
                    "Type " + exprType + " non autorisé dans une instruction print (règle 3.31)",
                    expr.getLocation()
                );    
            }
        }         
        
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            if (getPrintHex()) {
                a.codeGenPrint(compiler,true); // print Hex
            } else {
                a.codeGenPrint(compiler,false); // Print normal
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {


        s.print((getPrintHex() ? "print" + getSuffix() + "x" : "print" + getSuffix()) + "(");
        this.getArguments().decompile(s);
        s.print(");");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
