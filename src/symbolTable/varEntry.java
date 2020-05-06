package symbolTable;

public class varEntry extends symTabEntry {

    String scope="";

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

    public String toString(){
        return 	String.format("%-12s" , "| " + kind) +
                String.format("%-12s" , "| " + name) +
                String.format("%-10s"  , "| " + type)+
                String.format("%-5s"  , "| " + size) +
                String.format("%-12s"  , "| " + tag)
                + "|";
    }
}
