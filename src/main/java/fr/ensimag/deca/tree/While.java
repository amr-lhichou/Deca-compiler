package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl53
 * @date 01/01/2026
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        int cmp = compiler.getLabelId();
        Label start= new Label("start_While"+cmp);
        Label end = new Label("end_while" +cmp);
        compiler.addLabel(start);
        // on ajoute la generation du code de la condition stockée dans R2

        getCondition().codeGenInst(compiler);

        // On compare R2 avec 0 (Faux)
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(2)));

        // if egalité : on sort de la boucle
        compiler.addInstruction(new BEQ(end));

        // execution du body
        getBody().codeGenListInst(compiler);

        // On reteste  la condition : satrt
        compiler.addInstruction(new BRA(start));

        compiler.addLabel(end);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        Type condType = this.condition.verifyExpr(compiler, localEnv, currentClass);

        // while (boolean)
        if (!condType.isBoolean()){
            throw new ContextualError("Condition de while doit être de type boolean non pas " 
            + condType + " (règle 3.29)", condition.getLocation());
        }

        body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
