package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl53
 * @date 01/01/2026
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {

        if (otherType == null) {
            return false;
        }

        return otherType.isClass() &&
               ((ClassType) otherType).definition == this.definition;
    }

    // Si A est sous Classe de B alors A est sous Type de B
    @Override
    public boolean isSubTypeOf(Type otherType){

        if (!otherType.isClass()) return false;

        ClassType supperClass = (ClassType) otherType;

        return this.isSubClassOf(supperClass);

    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {

        ClassDefinition currentDef = this.definition;
        while (currentDef != null){
            if (currentDef == potentialSuperClass.definition) return true;
            currentDef = currentDef.getSuperClass();
        }
        return false;

    }


}
