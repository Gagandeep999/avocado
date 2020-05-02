package symbolTable;

import java.util.ArrayList;

public class symTab {

    String name;
    ArrayList<symTabEntry> tableList;
    symTab upperTable;
    int level;
    int size = 0;

    public symTab(){
        this.tableList = new ArrayList<>();
    }

    public symTab(int level ,String name){
        this.level = 0;
        this.name = name;
        this.tableList = new ArrayList<>();
        this.upperTable = null;
    }

    public symTab(String name, ArrayList tableList){
        this.name = name;
        this.tableList = tableList;
    }

    public symTab(int level, String name, symTab subTable){
        this.level = level;
        this.name = name;
        this.tableList = new ArrayList<>();
        this.upperTable = subTable;
    }

    public symTab getUpperTable() {
        return upperTable;
    }

    public String getName() {
        return name;
    }

    public void setTableList(ArrayList<symTabEntry> tableList) {
        this.tableList = tableList;
    }

    public ArrayList<symTabEntry> getTableList() {
        return tableList;
    }

    public void addEntry(symTabEntry entry){
        this.tableList.add(entry);
    }

    public String toString(){
        String stringtoreturn = new String();
        String prelinespacing = new String();
        for (int i = 0; i < this.level; i++)
            prelinespacing += "|    ";
        stringtoreturn += "\n" + prelinespacing + "========================================================================\n";
        stringtoreturn += prelinespacing + String.format("%-20s" , "| table: " + name) + String.format("%-15s" , " scope offset: " + size) + String.format("%-3s", "|function parameters"+"\n");
        stringtoreturn += prelinespacing        + "========================================================================\n";
        for (int i = 0; i < tableList.size(); i++){
            stringtoreturn +=  prelinespacing + tableList.get(i).toString() + '\n';
        }
        stringtoreturn += prelinespacing        + "========================================================================";
        return stringtoreturn;
    }

}
