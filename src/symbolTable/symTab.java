package symbolTable;

import java.util.HashMap;

public class symTab {

    String name;
    HashMap<String, symTabEntry> tableMap;

    public symTab(){
        this.name = "";
        this.tableMap = new HashMap<>();
    }

    public symTab(String name){
        this.name = name;
        this.tableMap = new HashMap<>();
    }

    public symTab(String name, HashMap tableMap){
        this.name = name;
        this.tableMap = tableMap;
    }

    public void addToTable(String entryName, symTabEntry entry){
        this.tableMap.put(entryName, entry);
    }

    public String toString(){
        String output = this.name;
        output += "\n\t";
        output += tableMap;
        return output;
    }
}
