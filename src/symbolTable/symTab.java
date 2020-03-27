package symbolTable;

import java.util.ArrayList;

public class symTab {

    String name;
    ArrayList<symTabEntry> tableList;

    public symTab(){
        this.tableList = new ArrayList<>();
    }

    public symTab(String name){
        this.name = name;
        this.tableList = new ArrayList<>();
    }

    public symTab(String name, ArrayList tableList){
        this.name = name;
        this.tableList = tableList;
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

    public String toString(){
        return (" "+ this.name + "->"+ this.tableList +" ");
    }
}
