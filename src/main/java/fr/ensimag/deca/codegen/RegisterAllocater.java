package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

/**
 * Gère l'allocation dynamique des registres pour moins utliser le stack
 * Respecte la limitation imposée par l'option du compiler -r.
 */
public class RegisterAllocater {

    private int currentRegIndex = 2; //we start at R2
    private int maxRegIndex;         // Depends on the option -r


    public RegisterAllocater(int maxRegisters) {
        // R0 et R1 are reserved. We start with R2
        this.maxRegIndex = maxRegisters - 1;
    }

    // function that allows the first available register
    public GPRegister allocateRegister() {
        if (currentRegIndex <= maxRegIndex) {
            return Register.getR(currentRegIndex++);
        }
        //if none available we PUSH/POP
        return null;

    }

    public void freeRegister() {
       // we dont free R0 ,R1
        if (currentRegIndex > 2) {
            currentRegIndex--;
        }
    }
    //function that gives the current register we last used
    public GPRegister getCurrentRegister() {
        return Register.getR(currentRegIndex);
    }

}