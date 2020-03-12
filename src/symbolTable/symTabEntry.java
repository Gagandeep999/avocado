package symbolTable;

import java.util.HashMap;

public class symTabEntry {

    String name;
    String kind;
    String type;
    symTab link;

    public symTabEntry(){
        this.name = "";
        this.kind = "";
        this.type = "";
        this.link = null;
    }

    public symTabEntry(String name, String kind){
        this.name = name;
        this.kind = kind;
        this.type = "";
        this.link = null;
    }

    public symTabEntry(String name, String kind, String type){
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.link = null;
    }

    public symTabEntry(String name, String kind, symTab link){
        this.name = name;
        this.kind = kind;
        this.type = "";
        this.link = link;
    }

    public symTabEntry(String name, String kind, String type, symTab link){
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.link = link;
    }

    public String toString(){
        return ("name-> "+this.name+" kind-> "+this.kind+" type-> "+this.type+" link-> "+this.link);
    }
}
