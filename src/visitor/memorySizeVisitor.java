package visitor;

import nodes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class memorySizeVisitor extends visitor {

    PrintWriter err;
    String error;

    public memorySizeVisitor(){
    }

    public memorySizeVisitor(String errFile){
        this.error = "";
        try {
            this.err = new PrintWriter(new File(errFile));
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public int sizeOfTypeNode(node p_node) {
        int size = 0;
        if(p_node.entry.getType() == "integer")
            size = 4;
        else if(p_node.entry.getType() == "float")
            size = 8;
        return size;
    }

    @Override
    public void visit(progNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(addOpNode p_node) {
//        System.out.println("in addopnode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        String type = p_node.entry.getType();
        p_node.entry.setSize(sizeOfTypeNode(p_node));
        String name = p_node.getMoonVarName();
        p_node.entry.setTag("temp_"+name);
    }

    @Override
    public void visit(multOpNode p_node) {
//        System.out.println("in multpopnode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        p_node.entry.setSize(sizeOfTypeNode(p_node));
        String name = p_node.getMoonVarName();
        p_node.entry.setTag("temp_"+name);
    }

    @Override
    public void visit(varDeclNode p_node) {
//        System.out.println("in varDeclNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        String name = p_node.entry.getName();
        p_node.entry.setTag("var_"+name);
        p_node.entry.setSize(sizeOfTypeNode(p_node));
    }

    @Override
    public void visit(classNode p_node) {
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
    public void visit(numNode p_node) {
//        System.out.println("numnode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        p_node.entry.setSize(sizeOfTypeNode(p_node));
        String name = p_node.getMoonVarName();
        p_node.entry.setTag("lit_"+name);
    }


    @Override
    public void visit(fparamNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        System.out.println("fparamNode");
        p_node.entry.setSize(sizeOfTypeNode(p_node));
        String name = p_node.entry.getName();
        p_node.entry.setTag("param_"+name);
    }

    // visitor does not apply for the method below

    @Override
    public void visit(assignStatNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(classListNode p_node) {
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
}
