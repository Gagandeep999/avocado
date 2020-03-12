package symbolTable;

public class funcEntry extends symTabEntry {

    public funcEntry(){
        super();
    }

    public funcEntry(String name, String kind){
        super(name, kind);
    }

    public funcEntry(String name, String kind, String type){
        super(name, kind, type);
    }

    public funcEntry(String name, String kind, symTab link){
        super(name, kind, link);
    }

    public funcEntry(String name, String kind, String type, symTab link){
        super(name, kind, type, link);
    }
}
