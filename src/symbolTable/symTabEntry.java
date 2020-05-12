package symbolTable;

import java.util.Objects;

public class symTabEntry {

    String name;
    String kind;
    String type;
    symTab link;
    int size;
    String tag;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLink(symTab link) {
        this.link = link;
    }

    public symTabEntry(){
        this.name = "";
        this.kind = "";
        this.type = "";
        this.link = null;
        this.size = 0;
        this.tag = "";
    }

    public symTabEntry(String name, String kind){
        this.name = name;
        this.kind = kind;
        this.type = "";
        this.link = null;
        this.size = 0;
        this.tag = "";
    }

    public symTabEntry(String name, String kind, String type){
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.link = null;
        this.size = 0;
        this.tag = "";
    }

    public symTabEntry(String kind, symTab link){
        this.kind = kind;
        this.type = "";
        this.link = link;
        this.size = 0;
        this.tag = "";
    }

    public symTabEntry(String name, String kind, String type, symTab link){
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.link = link;
        this.size = 0;
        this.tag = "";
    }

    public symTabEntry(String name, String kind, symTab link){
        this.name = name;
        this.kind = kind;
        this.type = "";
        this.link = link;
        this.size = 0;
        this.tag = "";
    }

    public String toString(){
        return (this.kind+" | "+this.name+" | "+this.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        symTabEntry entry = (symTabEntry) o;
        return Objects.equals(name, entry.name) &&
                Objects.equals(kind, entry.kind) &&
                Objects.equals(type, entry.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kind, type);
    }
}
