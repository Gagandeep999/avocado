package symbolTable;

public class classEntry extends symTabEntry {

    public classEntry(){
        super();
    }

    public classEntry(String name, String kind){
        super(name, kind);
    }

    public classEntry(String name, String kind, String type){
        super(name, kind, type);
    }

    public classEntry(String name, String kind, symTab link){
        super(name, kind, link);
    }

    public classEntry(String name, String kind, String type, symTab link){
        super(name, kind, type, link);
    }
}
