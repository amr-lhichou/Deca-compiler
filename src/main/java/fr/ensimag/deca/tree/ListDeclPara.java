package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclPara extends TreeList<AbstractDeclPara> {

    @Override
    public void decompile(IndentPrintStream s) {
        // {r := (r = ε ? ε : r.’,’).param} ) ∗ ]
        boolean first = true;
        for (AbstractDeclPara param : this.getList()){
            if (!first){
                s.print(", ");
            }
            param.decompile(s);
            first = !first;
        }
        }
}