package nodes;

import java.util.LinkedList;
import lexer.token;
import visitor.visitor;

public abstract class node {
    String name; //corresponds to the lexeme
    String type; //corresponds to the token
    String data;
    int num;
    int myNum;
    static int curNum;
    LinkedList<node> children;
    node parent;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getNum() { return num; }

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
        this.name = "";
        this.children = new LinkedList<>();
    }

    public node(String data){
        this.data = data;
        this.myNum = curNum;
        node.curNum++;
    }

    public node(String data, node parent){
        this.data = data;
        this.parent = parent;
        node.curNum++;
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

    public void accept(visitor visitorNode){
        visitorNode.visit(this);
    }
}
