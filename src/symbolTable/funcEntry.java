package symbolTable;

import java.util.ArrayList;

public class funcEntry extends symTabEntry {

    String scope;
    ArrayList<String> params;
    boolean isOverloaded;

    public funcEntry(){
        super();
    }

    public funcEntry(String scope, ArrayList params, String name, String kind, String type, symTab link){
        super(name, kind, type, link);
        this.scope = scope;
        this.params = params;
        this.isOverloaded = false;
    }

    public funcEntry(ArrayList params, String name, String kind, String type, symTab link){
        super(name, kind, type, link);
        this.scope = "global";
        this.params = params;
        this.isOverloaded = false;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public boolean isOverloaded() {
        return isOverloaded;
    }

    public void setOverloaded(boolean overloaded) {
        isOverloaded = overloaded;
    }

//    public String toString(){
//        return (this.scope+" | "+this.name+" | "+this.kind+" | "+this.type);
//    }

    public String toString(){
        return 	String.format("%-12s" , "| " + kind) +
                String.format("%-12s" , "| " + name) +
                String.format("%-10s"  , "| " + type) +
                String.format("%-25s"  , "| " + params) +
                "|" ;
//                params;
    }
}
