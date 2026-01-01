package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl53
 * @date 01/01/2026
 */
public abstract class AbstractLValue extends AbstractExpr {
    public ExpDefinition getExpDefinition() {
        return null;
    }
}
