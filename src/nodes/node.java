package nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import lexer.token;
import symbolTable.symTab;
import symbolTable.symTabEntry;
import visitor.visitor;

public abstract class node {

    String data;
    int myNum;
    static int curNum;
    ArrayList<node> children;
    node parent;
    public symTab table;
    public symTabEntry entry;


    public String getData() {
        return data;
    }

    public int getMyNum() {
        return myNum;
    }

    public static int getCurNum() {
        return curNum;
    }

    public node getParent() {
        return parent;
    }

    public ArrayList<node> getChildren() {
        return children;
    }

    public node(){
        this.data = "";
        this.children = new ArrayList<>();
    }

    public node(String data){
        this.data = data;
        this.myNum = curNum;
        this.children = new ArrayList<>();
        this.parent = null;
        node.curNum++;
    }

    public node(String data, node parent){
        this.data = data;
        this.parent = parent;
        node.curNum++;
    }

    public void makeRightChild(node y){
        this.children.add(y);
    }

    public void makeLeftChild(node x){
        this.children.add(0, x);
    }

    public void adopt(node y){
        for (node child :
                y.children) {
            this.children.add(child);
        }
    }

    public void accept(visitor visitorNode){
        visitorNode.visit(this);
    }
}
