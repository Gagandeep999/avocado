package visitor;

import nodes.*;
import symbolTable.symTabEntry;
import symbolTable.varEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class typeCheckVisitor extends visitor {

    String outputfile;
    String errors;
    PrintWriter err;
    int tempVarNum;

    public typeCheckVisitor(){
        this.errors = new String();
        this.tempVarNum = 0;
        try {
            this.err = new PrintWriter(new File(this.outputfile));
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public typeCheckVisitor(String outputFileName){
        this.outputfile = outputFileName;
        this.errors = new String();
        this.tempVarNum = 0;
        try {
            this.err = new PrintWriter(new File(this.outputfile));
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public String getNewTempVarName(){
        tempVarNum++;
        return "t" + Integer.toString(tempVarNum);
    }

    @Override
    public void visit(progNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        System.out.println(this.errors);
        //print to file
    }

    @Override
    public void visit(addOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        System.out.println("inside addOpNode");
        String leftChildType = getChildType(p_node, 0);
        String rightChildType = getChildType(p_node, 1);

        if (leftChildType.equals(rightChildType)){
            p_node.setType(leftChildType);
            String tempvarname = this.getNewTempVarName();
            p_node.moonVarName = tempvarname;
            p_node.entry = new varEntry(("temp_"+tempvarname), p_node.moonVarName, p_node.getType());
            p_node.table.addEntry(p_node.entry);
        }else {
            p_node.setType("ERROR");
            this.errors += "error in addOpNode" + p_node.toString();
        }
    }

    @Override
    public void visit(multOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        System.out.println("inside multOpNode");
        String leftChildType = getChildType(p_node, 0);
        String rightChildType = getChildType(p_node, 1);

        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);
            String tempvarname = this.getNewTempVarName();
            p_node.moonVarName = tempvarname;
            p_node.entry = new varEntry(("temp_"+tempvarname), p_node.moonVarName, p_node.getType());
            p_node.table.addEntry(p_node.entry);
        } else {
            p_node.setType("ERROR");
            this.errors += "error in multOpNode" + p_node.toString();
        }
    }

//    currently assignop only checks for simple cases like a=0 or a=1+1
//    @TODO how to check for the case a=b+c
    @Override
    public void visit(assignStatNode p_node) {
        for (node child :
        p_node.getChildren()) {
            child.accept(this);
        }
        System.out.println("inside assignStatNode");
        String leftChildType = "";
        String rightChildType = "";

        String leftChildName = p_node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData();
        for (symTabEntry entry :
                p_node.table.getTableList()) {
            if (entry.getName().equals(leftChildName)) {
                leftChildType = entry.getType();
                break;
            }
        }

        rightChildType = getChildType(p_node,1);
        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);
        } else {
            p_node.setType("ERROR");
            System.out.println("left child name is: "+leftChildName);
            this.errors += "error in assignStatNode" + p_node.toString();
        }
    }

    @Override
    public void visit(numNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        String tempvarname = this.getNewTempVarName();
        p_node.moonVarName = tempvarname;
        p_node.entry = new varEntry(("lit_"+tempvarname), p_node.moonVarName, p_node.getType());
        p_node.table.addEntry(p_node.entry);
    }

    private String getChildType(node p_node, int childNum){
        String childType = "";
        if (p_node.getChildren().get(childNum).getClass().toString().contains("generalNode")){ //meaning that it is not numNode
            String varName = p_node.getChildren().get(childNum).getChildren().get(0).getChildren().get(0).getData();
            for (symTabEntry entry :
                    p_node.table.getTableList()) {
                if (entry.getName().equals(varName)){
                    childType = entry.getType();
                    break;
                }
            }
            for (symTabEntry entry :
                    p_node.table.getUpperTable().getTableList()) {
                if (entry.getName().equals(varName)){
                    childType = entry.getType();
                    break;
                }
            }
        }else { //it is a numNode and we can get the type right away
            childType = p_node.getChildren().get(childNum).getType();
        }
        return childType;
    }

    /** Visitor does not apply for the following methods **/

    @Override
    public void visit(classListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(varDeclNode p_node) {
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
    public void visit(classNode p_node) {
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
    public void visit(statBlockNode p_node) {
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
    public void visit(mainNode p_node) {
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
}
