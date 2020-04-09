package symbolTable;

public class classEntry extends symTabEntry {

    public classEntry(){
        super();
    }

    public classEntry( String name, String kind, String type, symTab link){
        super(name, kind, type, link);
    }

//    public String toString(){
//        return 	String.format("%-12s" , "| " + kind) +
//                String.format("%-40s" , "| " + name) +
//                "|" +
//                link;
//    }

    public String toString(){
        return 	String.format("%-12s" , "| " + kind) +
                String.format("%-12s" , "| " + name) +
                String.format("%-28s"  , "| " + type) +
                String.format("%-20s"  , "| ") +
                "|" ;
//                params;
    }

}
