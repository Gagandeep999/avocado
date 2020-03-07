package parser;

import java.util.LinkedList;
import lexer.token;

public class node {
    String name;
    String type;
    LinkedList<node> children;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LinkedList<node> getChildren() {
        return children;
    }

    public node(){
        this.name = "";
        this.children = new LinkedList<>();
    }

    public node(String name){
        this.name = name;
        this.type = "";
        this.children = new LinkedList<>();
    }

    public node(String name, String type){
        this.name = name;
        this.type = type;
        this.children = new LinkedList<>();
    }

    public node(token t){
        this.name = t.getLexeme().toString();
        this.type = t.getToken();
        this.children = new LinkedList<>();
    }

    public void makeRightChild(node y){
        this.children.addLast(y);
    }

    public void makeLeftChild(node y){
        this.children.addFirst(y);
    }

    public void adopt(node y){
        for (node child :
                y.children) {
            this.children.addLast(child);
        }
    }


}
