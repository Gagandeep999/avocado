package symbolTable;

public class varEntry extends symTabEntry {

    public varEntry(){
        super();
    }

    public varEntry(String name, String kind){
        super(name, kind);
    }

    public varEntry(String name, String kind, String type){
        super(name, kind, type);
    }

    public varEntry(String name, String kind, symTab link){
        super(name, kind, link);
    }

    public varEntry(String name, String kind, String type, symTab link){
        super(name, kind, type, link);
    }
}
