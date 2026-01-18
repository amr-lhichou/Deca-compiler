package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;


public class MethodASM extends DeclMethod {
    private final StringLiteral blocAsm;

    public MethodASM(AbstractIdentifier typeRetour, AbstractIdentifier etiquette,
                     ListDeclPara listeParametres, StringLiteral blocAsm) {
        super(typeRetour, etiquette, listeParametres);
        this.blocAsm = blocAsm;
    }

   protected void verifyMethBody(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError{

    }

    @Override
    public void codeGenMethod(DecacCompiler compiler, ClassDefinition currentClass){

        String className = currentClass.getType().getName().getName();
        String methodName = nomMethode.getName().getName();

        compiler.addLabel(new Label("code." + className + "." + methodName));
        compiler.add(new InlinePortion(blocAsm.getValue()));
        compiler.addInstruction(new RTS());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // { r := ’asm(’.code.’);’}
        s.print("asm(");
        this.blocAsm.decompile(s);
        s.print(");");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.typeMethode.prettyPrint(s, prefix, false);
        this.nomMethode.prettyPrint(s, prefix, false);
        this.parametres.prettyPrint(s, prefix, false);
        this.blocAsm.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.typeMethode.iter(f);
        this.nomMethode.iter(f);
        this.parametres.iter(f);
        this.blocAsm.iter(f);
    }
}