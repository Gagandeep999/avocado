package visitor;

import nodes.*;
import symbolTable.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class symbolTableVisitor extends visitor {

    String outputfile;
    String errorFile;
    PrintWriter err;
    HashMap<String, ArrayList<symTab>> tables;
    int funcOverLoadCounter;
    boolean isFuncOverload;
    boolean isClassFuncOverloaded;


    public symbolTableVisitor(){
        this.outputfile = "";
        this.tables = new HashMap<>();
    }

    public symbolTableVisitor(String errorFile){
        this.outputfile = outputfile;
        this.errorFile = errorFile;
        this.tables = new HashMap<>();
        try {
            this.err = new PrintWriter(new File(this.errorFile));
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        this.funcOverLoadCounter = 0;
        this.isFuncOverload = false;
        this.isClassFuncOverloaded = false;

    }

    @Override
    public void visit(progNode p_node){
        p_node.table = new symTab(0, "global");

        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }

        //printing everything

    }

    @Override
    public void visit(classNode p_node){
        String className = p_node.getChildren().get(0).getData();
        symTab localTable = new symTab(1, className, p_node.table);
        p_node.entry = new classEntry(className, "class", "NONE", localTable);
        p_node.table.addEntry(p_node.entry);
        p_node.table = localTable;

        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(funcDefNode p_node){
        int noOfChild = p_node.getChildren().get(0).getChildren().size();

        if (noOfChild==4){ // class function
            String className = p_node.getChildren().get(0).getChildren().get(0).getData();
            String funcName = p_node.getChildren().get(0).getChildren().get(1).getData();
            ArrayList<symTabEntry> prevTables = p_node.table.getTableList();
            for (symTabEntry eachPrevTable :
                    prevTables) {
                if (className.equals(eachPrevTable.getName())){
                    symTab classTable = eachPrevTable.getLink();
                    for (symTabEntry eachPrevFuncEntry :
                            classTable.getTableList()){
                        if (eachPrevFuncEntry.getName().equals(funcName)){
                            p_node.table = eachPrevFuncEntry.getLink();
                        }
                    }
                }
            }
        }
        else { //free function
            String funcName = p_node.getChildren().get(0).getChildren().get(0).getData();
            node fParamList = p_node.getChildren().get(0).getChildren().get(1);
            String funcType = p_node.getChildren().get(0).getChildren().get(2).getData();
            ArrayList<String> fParams = new ArrayList<>();
            for (node fparam :
                    fParamList.getChildren()) {
                fParams.add(fparam.getChildren().get(1).getData()+":"+fparam.getChildren().get(0).getData());
            }
            symTab localTable = new symTab(1, funcName, p_node.table);
            p_node.entry = new funcEntry(fParams, funcName, "function", funcType, localTable);
            p_node.table.addEntry(p_node.entry);
            p_node.table = localTable;
        }

        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(funcDeclNode p_node) {
//        System.out.println("inseid funcDecl");
        String funcScope = p_node.getChildren().get(0).getData();
        String funcName = p_node.getChildren().get(1).getData();
        node paramList = p_node.getChildren().get(2);
        String funcType = p_node.getChildren().get(3).getData();
        ArrayList<String> fparamList = new ArrayList<>();

        symTab localTable = new symTab(2, funcName, p_node.table);
        p_node.entry = new funcEntry(funcScope, fparamList, funcName, "function", funcType, localTable);
        p_node.table.addEntry(p_node.entry);
        p_node.table = localTable;

        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(fparamNode p_node) {
//        System.out.println("inside fparam");
        String type = p_node.getChildren().get(0).getData();
        String name = p_node.getChildren().get(1).getData();
        p_node.entry = new paramEntry(name, "parameter", type);
        String funcName = p_node.table.getName();

        if (p_node.table.getTableList().isEmpty()){
            p_node.table.addEntry(p_node.entry);
        } else { // it is not empty
            for (symTabEntry funcEntry :
                    p_node.table.getTableList()) {
                if ((funcEntry.getName().equals(name)) && (funcEntry.getType().equals(type))
                    && funcEntry.getKind().equals("parameter")) {
//                    System.out.println("same parameter name declared more than once");;
                }else {
                    p_node.table.addEntry(p_node.entry);
                    break;
                }
            }
        }

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

        if (p_node.getChildren().size()==4){ //class variables
            String varScope = p_node.getChildren().get(0).getData();
            String varType = p_node.getChildren().get(1).getData();
            String varName = p_node.getChildren().get(2).getData();
            String varDimList = p_node.getChildren().get(3).getData();

            p_node.entry = new varEntry(varScope, varName, "variable", varType);
            p_node.table.addEntry(p_node.entry);
        }
        else { // function variable
            String varType = p_node.getChildren().get(0).getData();
            String varName = p_node.getChildren().get(1).getData();
            String varDimList = p_node.getChildren().get(2).getData();
            p_node.entry = new varEntry(varName, "variable", varType);
            p_node.table.addEntry(p_node.entry);
        }
    }

    @Override
    public void visit(mainNode p_node){
        symTab localTable = new symTab(1, "main", p_node.table);
        p_node.entry = new funcEntry(new ArrayList(),"main", "function", "void", localTable);
        p_node.table.addEntry(p_node.entry);
        p_node.table = localTable;

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
    public void visit(multOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(compareOpNode p_node) {
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

    /** Visitor does not apply for the following methods **/

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
    public void visit(node p_node) {
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

    @Override
    public void visit(funcCallNode p_node) {
//        System.out.println("funcCallNode in symTabGen");
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }

    @Override
    public void visit(returnNode p_node) {
//        System.out.println("returnNode in symTabGen");
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
    }
}
