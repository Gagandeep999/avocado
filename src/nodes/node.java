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
    public String moonVarName = new String();


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
        this.moonVarName = new String();
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

//    public void print(){
//        System.out.println("=====================================================================");
//        System.out.println("Node type                 | data      | type      | subtreestring");
//        System.out.println("=====================================================================");
//        this.printSubtree();
//        System.out.println("=====================================================================");
//    }
//
//    public void printSubtree(){
//        for (int i = 0; i < Node.m_nodelevel; i++ )
//            System.out.print("  ");
//
//        String toprint = String.format("%-25s" , this.getClass().getName());
//        for (int i = 0; i < Node.m_nodelevel; i++ )
//            toprint = toprint.substring(0, toprint.length() - 2);
//        toprint += String.format("%-12s" , (this.getData() == null || this.getData().isEmpty())         ? " | " : " | " + this.getData());
//        toprint += String.format("%-12s" , (this.getType() == null || this.getType().isEmpty())         ? " | " : " | " + this.getType());
//        toprint += (String.format("%-16s" , (this.m_subtreeString == null || this.m_subtreeString.isEmpty()) ? " | " : " | " + (this.m_subtreeString.replaceAll("\\n+",""))));
//
//        System.out.println(toprint);
//
//        Node.m_nodelevel++;
//        List<Node> children = this.getChildren();
//        for (int i = 0; i < children.size(); i++ ){
//            children.get(i).printSubtree();
//        }
//        Node.m_nodelevel--;
//    }

    public String toString(){
        return table.toString();
    }

    public void accept(visitor visitorNode){
        visitorNode.visit(this);
    }
}
