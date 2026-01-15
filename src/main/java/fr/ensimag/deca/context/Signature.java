package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl53
 * @date 01/01/2026
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        if (this.size() != signature.size()) return false;
        for (int i = 0; i < this.size(); i++) {
            Type t1 = this.paramNumber(i);
            Type t2 = signature.paramNumber(i);
            if (!t1.sameType(t2)) {
                return false;
            }
        }

        return true;
    }

}
