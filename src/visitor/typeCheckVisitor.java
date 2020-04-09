package visitor;

import nodes.*;
import symbolTable.varEntry;

public class typeCheckVisitor extends visitor {

    String outputFileName;
    String errors;


    public typeCheckVisitor(){
        this.errors = new String();

    }

    public typeCheckVisitor(String outputFileName){
        this.outputFileName = outputFileName;
        this.errors = new String();
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
        System.out.println("inside addOpNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        String leftChildType = p_node.getChildren().get(0).getType();
        String rightChildType = p_node.getChildren().get(1).getType();
        if (leftChildType.equals(rightChildType)){
            p_node.setType(leftChildType);
        }else {
            p_node.setType("ERROR");
            this.errors += "error in addOpNode" + p_node.toString();
        }
    }

    @Override
    public void visit(multOpNode p_node) {
        System.out.println("inside multOpNode");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        String leftChildType = p_node.getChildren().get(0).getType();
        String rightChildType = p_node.getChildren().get(1).getType();
        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);

        } else {
            p_node.setType("ERROR");
            this.errors += "error in multOpNode" + p_node.toString();
        }
    }

    @Override
    public void visit(assignStatNode p_node) {
        System.out.println("inside assignStatNode");
        for (node child :
        p_node.getChildren()) {
            child.accept(this);
        }
        String leftChildType = p_node.getChildren().get(0).getType();
        String rightChildType = p_node.getChildren().get(1).getType();
        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);
        } else {
            p_node.setType("ERROR");
            this.errors += "error in assignStatNode" + p_node.toString();
        }
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
    public void visit(numNode p_node) {
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
