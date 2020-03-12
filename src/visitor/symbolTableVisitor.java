package visitor;

import nodes.*;
import symbolTable.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class symbolTableVisitor extends visitor {

    String outputfile;

    public symbolTableVisitor(){

    }

    public symbolTableVisitor(String outputfile){
        this.outputfile = outputfile;
    }

    @Override
    public void visit(progNode p_node) {
        p_node.table = new symTab("global");
        //visit every child of the prognode
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
        try {
            PrintWriter out = new PrintWriter(new File(this.outputfile));
            out.print(p_node.table);
            out.flush();
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void visit(classNode p_node) {
        //get the className -> create a symboltable for that class -> create a entry for that class ->
        //->add a link in the entry to the table -> add the entry to the symbol table.
        String classname = p_node.getChildren().get(0).getData();
        symTab localClassTable = new symTab(classname);
        p_node.entry = new classEntry(classname, "class", localClassTable);
        p_node.table.addToTable(classname, p_node.entry);

        p_node.table = localClassTable;

//        node funcDeclListNode = p_node.getChildren().get(2);
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }

    }

    @Override
    public void visit(funcDefNode p_node) {

        String fname = p_node.getChildren().get(0).getChildren().get(1).getData();
        String ftype = p_node.getChildren().get(0).getChildren().get(3).getData();
        symTab localFuncTable = new symTab(fname);

        node paramList = p_node.getChildren().get(0).getChildren().get(2);
        for (node child :
                paramList.getChildren()) {
            String type = child.getChildren().get(0).getData();
            String name = child.getChildren().get(1).getData();
            varEntry paramEntry = new varEntry(name, "parameter", type);
            localFuncTable.addToTable(name, paramEntry);
        }

        node varList = p_node.getChildren().get(0).getChildren().get(2);
        for (node child :
                varList.getChildren()) {
            String type = child.getChildren().get(0).getData();
            String name = child.getChildren().get(1).getData();
            varEntry varEntry = new varEntry(name, "variable", type);
            localFuncTable.addToTable(name, varEntry);
        }

        p_node.entry = new funcEntry(fname, "function", ftype, localFuncTable);
        p_node.table.addToTable(fname, p_node.entry);

    }

    @Override
    public void visit(mainNode p_node) {
        String name = p_node.getData();
        symTab mainTable = new symTab(name);

        node varDecl = p_node.getChildren().get(0).getChildren().get(0);
        for (node child :
                varDecl.getChildren()) {
            String type = child.getChildren().get(0).getData();
            String childname = child.getChildren().get(1).getData();
            varEntry varEntry = new varEntry(childname, "variable", type);
            mainTable.addToTable(name, varEntry);
        }
    }

    @Override
    public void visit(funcDeclNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(varDeclNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    //Visitor does not apply for the following methods


    @Override
    public void visit(classListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(funcDefListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(addOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(assignStatNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(generalNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(idNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(multOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(node p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(numNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(statBlockNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(typeNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }
}
