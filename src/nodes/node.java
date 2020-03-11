package nodes;

import java.util.LinkedList;
import lexer.token;
import visitor.visitor;

public abstract class node {

    String data;
    int myNum;
    static int curNum;
    LinkedList<node> children;
    node parent;


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

    public LinkedList<node> getChildren() {
        return children;
    }

    public node(){
        this.data = "";
        this.children = new LinkedList<>();
    }

    public node(String data){
//        this.name = "";
//        this.type = "";
        this.data = data;
//        this.num = 0;
        this.myNum = curNum;
        this.children = new LinkedList<>();
        this.parent = null;
        node.curNum++;
    }

    public node(String data, node parent){
        this.data = data;
        this.parent = parent;
        node.curNum++;
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

    public void accept(visitor visitorNode){
        visitorNode.visit(this);
    }
}
