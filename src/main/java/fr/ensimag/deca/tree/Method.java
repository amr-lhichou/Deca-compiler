package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.context.MethodDefinition;

public class Method extends DeclMethod {
    private final ListDeclVar declarationsLocales;
    private final ListInst corpsMethode;

    public Method(AbstractIdentifier signatureType, AbstractIdentifier nomAppel,
                  ListDeclPara entrees, ListDeclVar declarationsLocales, ListInst corpsMethode) {
        super(signatureType, nomAppel, entrees);
        this.declarationsLocales = declarationsLocales;
        this.corpsMethode = corpsMethode;
    }

   protected void verifyMethBody(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError{

        MethodDefinition methDef = (MethodDefinition) currentClass.getMembers().get(this.nomMethode.getName());
        EnvironmentExp envPara = parametres.verifyListDeclPara(compiler);
        EnvironmentExp envLocals =new EnvironmentExp(envPara);
        declarationsLocales.verifyListDeclVariable(compiler, envLocals, currentClass);
        corpsMethode.verifyListInst(compiler, envLocals, currentClass, methDef.getType());

    }

    @Override
    public void decompile(IndentPrintStream s) {
        // { r := ’{’.vars.insts.’}’}
        s.print("{");
        this.declarationsLocales.decompile(s);
        this.corpsMethode.decompile(s);
        s.print("}");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.typeMethode.prettyPrint(s, prefix, false);
        this.nomMethode.prettyPrint(s, prefix, false);
        this.parametres.prettyPrint(s, prefix, false);
        this.declarationsLocales.prettyPrint(s, prefix, false);
        this.corpsMethode.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.typeMethode.iter(f);
        this.nomMethode.iter(f);
        this.parametres.iter(f);
        this.declarationsLocales.iter(f);
        this.corpsMethode.iter(f);
    }
}