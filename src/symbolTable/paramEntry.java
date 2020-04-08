package symbolTable;

public class paramEntry extends symTabEntry {

    public paramEntry(){
        super();
    }

    public paramEntry(String name, String kind, String type){
        super(name, kind, type);
    }

    public String toString(){
        return 	String.format("%-12s" , "| " + kind) +
                String.format("%-12s" , "| " + name) +
                String.format("%-24s"  , "| " + type)
                + "|";
    }
}
