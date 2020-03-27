package symbolTable;

public class varEntry extends symTabEntry {

    String scope;

    public varEntry(){
        super();
    }

    public varEntry(String scope, String name, String kind, String type){
        super(name, kind, type, null);
        this.scope = scope;
    }

    public varEntry(String name, String kind, String type){
        super(name, kind, type);
    }
}
