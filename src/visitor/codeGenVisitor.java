package visitor;

import nodes.*;
import symbolTable.symTabEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Stack;

public class codeGenVisitor extends visitor {

    public Stack<String> registerPool = new Stack<>();
    public String dataCode = new String();
    public String execCode = new String();
    public String outputFileName = new String();
    public String indent = new String("           ");
    private final int numOfRegisters = 15;

    public codeGenVisitor(){
    }

    public codeGenVisitor(String fileName){
        this.outputFileName = fileName;
        for (Integer i = this.numOfRegisters; i>=1; i--){
            this.registerPool.push("R"+i.toString());
        }
    }

    @Override
    public void visit(progNode p_node) {
//        System.out.println("progNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        try {
            PrintWriter out = new PrintWriter(new File(this.outputFileName));
            out.print(this.execCode);
            out.print(this.dataCode);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(addOpNode p_node) {
        System.out.println("addOpNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }

        String rightChildTag = getChildTag(p_node, 1);
        String leftChildTag = getChildTag(p_node, 0);
        String rightChildName = getChildName(p_node, 1);
        String leftChildName = getChildName(p_node, 0);

        String localReg = this.registerPool.pop();
        String leftChildReg = this.registerPool.pop();
        String rightChildReg = this.registerPool.pop();

        this.execCode += "% processing " +leftChildName + " + " + rightChildName + "\n";
        this.execCode += this.indent + "lw " + leftChildReg + ", " + leftChildTag + "(R0)\n";
        this.execCode += this.indent + "lw " + rightChildReg + ", " + rightChildTag + "(R0)\n";
        if (p_node.getData().equals("+")){
            this.execCode += this.indent + "add " + localReg + ", " + leftChildReg + ", " + rightChildReg + "\n";
        } else if (p_node.getData().equals("-")){
            this.execCode += this.indent + "sub " + localReg + ", " + leftChildReg + ", " + rightChildReg + "\n";
        } else { // it is "or"
            System.out.println(" or is not yet implemented");
        }


        this.dataCode += "% space for " + p_node.entry.getName() +"\n";
        this.dataCode += String.format("%-10s", p_node.entry.getTag() + " res " + p_node.entry.getSize() + "\n");

        this.execCode += this.indent + "sw " + p_node.entry.getTag() +"(R0), " + localReg + "\n";

        this.registerPool.push(leftChildReg);
        this.registerPool.push(rightChildReg);
        this.registerPool.push(localReg);
    }

    @Override
    public void visit(multOpNode p_node) {
//        System.out.println("multOpNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }

        String rightChildTag = getChildTag(p_node, 1);
        String leftChildTag = getChildTag(p_node, 0);
        String rightChildName = getChildName(p_node, 1);
        String leftChildName = getChildName(p_node, 0);

        String localReg = this.registerPool.pop();
        String leftChildReg = this.registerPool.pop();
        String rightChildReg = this.registerPool.pop();

        this.execCode += "% processing " +leftChildName + " + " + rightChildName + "\n";
        this.execCode += this.indent + "lw " + leftChildReg + ", " + leftChildTag + "(R0)\n";
        this.execCode += this.indent + "lw " + rightChildReg + ", " + rightChildTag + "(R0)\n";
        if (p_node.getData().equals("*")){
            this.execCode += this.indent + "mul " + localReg + ", " + leftChildReg + ", " + rightChildReg + "\n";
        } else if (p_node.getData().equals("/")){
            this.execCode += this.indent + "div " + localReg + ", " + leftChildReg + ", " + rightChildReg + "\n";
        } else { // this is the case when it's and
            System.out.println("and is not yet implemented");
        }

        this.dataCode += "% space for " + p_node.entry.getName() +"\n";
        this.dataCode += String.format("%-10s", p_node.entry.getTag() + " res " + p_node.entry.getSize() + "\n");

        this.execCode += this.indent + "sw " + p_node.entry.getTag() +"(R0), " + localReg + "\n";

        this.registerPool.push(leftChildReg);
        this.registerPool.push(rightChildReg);
        this.registerPool.push(localReg);
    }

    @Override
    public void visit(assignStatNode p_node) {
//        System.out.println("assignStatNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }

        String rightChildTag = getChildTag(p_node, 1);
        String leftChildTag = getChildTag(p_node, 0);
        String rightChildName = getChildName(p_node, 1);
        String leftChildName = getChildName(p_node, 0);

        String localReg = this.registerPool.pop();

        this.execCode += "% processing " + leftChildName + " = " + rightChildName + "\n";
        this.execCode += this.indent + "lw " + localReg + ", " + rightChildTag+"(R0)\n";
        this.execCode += this.indent + "sw " + leftChildTag+"(R0), " + localReg + "\n";

        this.registerPool.push(localReg);
    }

    @Override
    public void visit(numNode p_node) {
//        System.out.println("numNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        String localReg = this.registerPool.pop();

        this.dataCode += "% space for constant " + p_node.getData()+"\n";
        this.dataCode += String.format("%-10s", p_node.entry.getTag() + " res 4 \n");

        this.execCode += "% processing " + p_node.moonVarName +" stores "+ p_node.getData()+"\n";
        this.execCode += this.indent + "addi " + localReg + ", R0, " + p_node.getData() + "\n";
        this.execCode += this.indent + "sw " + p_node.entry.getTag() +"(R0), " + localReg + "\n";

        this.registerPool.push(localReg);
    }

    @Override
    public void visit(varDeclNode p_node) {
//        System.out.println("varDeclNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        this.dataCode += "% space for " + p_node.entry.getTag()+"\n";
        this.dataCode += String.format("%-10s", p_node.entry.getTag() + " res 4 \n");
    }

    @Override
    public void visit(mainNode p_node) {
        this.execCode += "% PROGRAM START\n";
        this.execCode += this.indent + "entry\n";
//        this.execCode += this.indent + "addi R14, R0, topaddr\n";

        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }

        this.execCode += this.indent + "hlt\n";
        this.execCode += "% PROGRAM END\n\n";
    }

    @Override
    public void visit(statBlockNode p_node) {
//        System.out.println("statBlockNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        String tag = "";
        String localReg = this.registerPool.pop();
        if (p_node.getData().equals("write")){ // routine for write
            String varName = p_node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData();
            for (symTabEntry entry :
                    p_node.table.getTableList()) {
                if (entry.getName().equals(varName)) {
                    tag = entry.getTag();
                    break;
                }
            }

            this.execCode += "% writing to console\n";
            this.execCode += this.indent + "lw " + localReg + ", " + tag + "(R0)" + "\n";
            this.execCode += this.indent + "jl R15, putint\n";

            this.registerPool.push(localReg);

        }else if (p_node.getData().equals("read")){ //routine for read
            System.out.println("routine for read");
        }else { // if, while, return
            System.out.println("Not yet implemented");
        }
    }


    private String getChildTag(node p_node, int childNum){
        String childTag = "";
        String childNodeType = p_node.getChildren().get(childNum).getClass().toString();
        if (childNodeType.contains("generalNode")){
            String varName = p_node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData();
            for (symTabEntry entry :
                    p_node.table.getTableList()) {
                if (entry.getName().equals(varName)){
                    childTag = entry.getTag();
                    break;
                }
            }
        }else { //it is a numNode and we can get the type right away
            childTag = p_node.getChildren().get(childNum).getEntry().getTag();
        }
        return childTag;
    }

    private String getChildName(node p_node, int childNum){
        String childName = "";
        String childNodeType = p_node.getChildren().get(childNum).getClass().toString();
        if (childNodeType.contains("generalNode")){
            childName = p_node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData();
        }else { //it is a numNode and we can get the type right away
            childName = p_node.getChildren().get(childNum).getData();
        }
        return childName;
    }

    // below methods do not apply to visitor

    @Override
    public void visit(classListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(classNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(funcDeclNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(funcDefListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(funcDefNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(generalNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(idNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(node p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(typeNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(fparamNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(compareOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }
}
