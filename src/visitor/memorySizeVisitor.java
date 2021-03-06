package visitor;

import com.sun.xml.internal.bind.v2.TODO;
import nodes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class memorySizeVisitor extends visitor {

    PrintWriter err;
    String error;

    public memorySizeVisitor(){
    }

    public memorySizeVisitor(PrintWriter errFile){
        this.error = "";
        this.err = errFile;
//        try {
//            this.err = new PrintWriter(new File(errFile));
//        }catch (FileNotFoundException e){
//            System.out.println(e.getMessage());
//        }
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
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        TODO: change tag name to make it more informative
//        System.out.println("in addopnode");
        p_node.entry.setSize(sizeOfTypeNode(p_node));
        String name = p_node.getMoonVarName();
        p_node.entry.setTag("temp_"+name);
    }

    @Override
    public void visit(multOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("in multpopnode");
        p_node.entry.setSize(sizeOfTypeNode(p_node));
        String name = p_node.getMoonVarName();
        p_node.entry.setTag("temp_"+name);
    }

    @Override
    public void visit(compareOpNode p_node) {
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
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("in varDeclNode");
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
//        System.out.println("fparamNode");
        String name = p_node.entry.getName();
        p_node.entry.setTag("param_"+name);
        p_node.entry.setSize(sizeOfTypeNode(p_node));
    }

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

    @Override
    public void visit(funcCallNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(returnNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ifNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(readNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(whileNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(writeNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }
}
