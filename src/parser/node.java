package parser;

import java.util.LinkedList;
import lexer.token;

public class node {
    String name;
    String type;
    int num;
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

    public node(String name, int num){
        this.name = name;
        this.type = "";
        this.num = num;
        this.children = new LinkedList<>();
    }

    public node(String name, String type, int num){
        this.name = name;
        this.type = type;
        this.num = num;
        this.children = new LinkedList<>();
    }

    public node(token t, int num){
        this.name = t.getLexeme().toString();
        this.type = t.getToken();
        this.num = num;
        this.children = new LinkedList<>();
    }

    public void makeRightChild(node y){
        this.children.addLast(y);
    }

    public void makeLeftChild(node x){
        this.children.addFirst(x);
    }

    public void adopt(node y){
        for (node child :
                y.children) {
            this.children.addLast(child);
        }
    }


}
