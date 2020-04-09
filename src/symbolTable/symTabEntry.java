package symbolTable;

public class symTabEntry {

    String name;
    String kind;
    String type;
    symTab link;

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public symTab getLink() {
        return link;
    }

    public void setLink(symTab link) {
        this.link = link;
    }

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

    public symTabEntry(String kind, symTab link){
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
        return (this.kind+" | "+this.name+" | "+this.type);
    }

}
