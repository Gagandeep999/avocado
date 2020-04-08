package symbolTable;

public class classEntry extends symTabEntry {

    public classEntry(){
        super();
    }

    public String toString(){
        return 	String.format("%-12s" , "| " + kind) +
                String.format("%-40s" , "| " + name) +
                "|" +
                link;
    }

}
