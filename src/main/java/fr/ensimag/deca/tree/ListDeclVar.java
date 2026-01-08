package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl53
 * @date 01/01/2026
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        // {r := r.decl}
        for (AbstractDeclVar var : this.getList()){
            var.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // on ajoute un index à incrémenter pour augmenter la pile
        int index=1;
        for (AbstractDeclVar decVar : this.getList()){
            decVar.verifyDeclVar(compiler, localEnv, currentClass);
            ExpDefinition def = decVar.getVarName().getExpDefinition();
            // On calcule l'adresse avec GB/LB
            // pour le Main :Registre GB
            def.setOperand(new RegisterOffset(index, Register.GB));
            // Incrémenter pour la prochaine variable
            index++;
        }
    }
    // ajout de methode de gencode pour les declVar
    public void codeGenListDeclVarInst(DecacCompiler compiler){
        for(AbstractDeclVar decVar : this.getList()){
            decVar.codegenDeclVar(compiler);
        }

    }


}
