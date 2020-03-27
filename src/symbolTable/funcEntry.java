package symbolTable;

import java.util.ArrayList;

public class funcEntry extends symTabEntry {

    String scope;
    ArrayList<String> params;

    public funcEntry(){
        super();
    }

    public funcEntry(String scope, ArrayList params, String name, String kind, String type, symTab link){
        super(name, kind, type, link);
        this.scope = scope;
        this.params = params;
    }

    public funcEntry(ArrayList params, String name, String kind, String type, symTab link){
        super(name, kind, type, link);
        this.scope = "global";
        this.params = params;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }
}
