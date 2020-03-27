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
    PrintWriter out;
    HashMap<String, symTab> tables;

    public symbolTableVisitor(){
        this.outputfile = "";
        this.tables = new HashMap<>();
    }

    public symbolTableVisitor(String outputfile){
        this.outputfile = outputfile;
        this.tables = new HashMap<>();
        try {
            this.out = new PrintWriter(new File(this.outputfile));
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void visit(progNode p_node) {

        ArrayList<symTabEntry> globalList = new ArrayList<>();

        //iterate over the children of prog_node
        for (node child :
                p_node.getChildren()) {
            if (child.getClass().toString().contains("classList")) {
                for (node classChild :
                        child.getChildren()) {
                    /**
                     * for every class node -> get name -> create empty symbol table for that class ->
                     * -> create a symbolTableEntry -> add entry to global scope ->
                     * -> add classname and empty table in hashmap that will be completed later on.
                     */
                    String className = classChild.getChildren().get(0).getData();
                    symTab classTab = new symTab(className);
                    symTabEntry classEntry = new symTabEntry(className, "class", " ", classTab);
                    globalList.add(classEntry);
                    className = className.concat("_CLASS");
                    tables.put(className, classTab);
                }
            }else if (child.getClass().toString().contains("funcDefList")) {
                for (node funcDefChild :
                        child.getChildren()) {
                    /**
                     * for every free function -> get name and type -> create empty table for the free func ->
                     * -> create entry for the free func -> add entry to globalList ->
                     * -> add funcName and empty table to hashmap that will be completed later on
                     */
                    if (funcDefChild.getChildren().get(0).getChildren().size() == 3){ //three child means it is a free function
                        String funcName = funcDefChild.getChildren().get(0).getChildren().get(0).getData();
                        String funcType = funcDefChild.getChildren().get(0).getChildren().get(2).getData();
                        symTab funcTab = new symTab(funcName);
                        ArrayList<String> params = new ArrayList<>();
                        funcEntry funcEntryTab = new funcEntry(params, funcName, "function", funcType, funcTab);
                        globalList.add(funcEntryTab);
                        tables.put(funcName, funcTab);
                    }
                }
            }else {
                /**
                 * create an empty table for the main function -> add entry to globalList
                 */
                symTab mainTab = new symTab("main");
                symTabEntry mainEntry = new symTabEntry("main", "mainFunction", "void", mainTab);
                globalList.add(mainEntry);
                tables.put("main", mainTab);
            }
        }
        //create the global table and add it to the hash map with key global
        symTab globalTab = new symTab("GLOBAL", globalList);
        tables.put("GLOBAL", globalTab);

        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }

        //print the symbol table
        printSymbolTable();

    }

    @Override
    public void visit(classNode p_node) {
        /**
         * at this point, an entry for the class already exist in the globalTable and
         * an entry for the class exist in the hashmap.
         * once the table is created assign it to the corresponding entry in the hashmap
         * and also the classEntry in the global table
         */
        String className = p_node.getChildren().get(0).getData();

        ArrayList<symTabEntry> classList = new ArrayList<>();
        node varfuncDecl = p_node.getChildren().get(2);

        for (node varOrFunc :
                varfuncDecl.getChildren()) {
            if (varOrFunc.getClass().toString().contains("varDecl")){
                //get scope -> get type -> get name -> create entry -> add to classList
                String varScope = varOrFunc.getChildren().get(0).getData();
                String varType = varOrFunc.getChildren().get(1).getData();
                String varName = varOrFunc.getChildren().get(2).getData();
//                @TODO what to do in case of dimlist not beig zero
                varEntry var = new varEntry(varScope, varName, "variable", varType);
                classList.add(var);
            }
            else { // the only other child will be a funcDecl
                //get details -> create arraylist for fParam -> create entry -> add to classList
                String funcScope = varOrFunc.getChildren().get(0).getData();
                String funcName = varOrFunc.getChildren().get(1).getData();
                String funcType = varOrFunc.getChildren().get(3).getData();
                symTab funcTab = new symTab();
                node paramlist = varOrFunc.getChildren().get(2);
                ArrayList<String> fparamList = new ArrayList<>();
                for (node fparam :
                        paramlist.getChildren()) {
                    fparamList.add(fparam.getChildren().get(0).getData());
                }
//                @TODO how to handle dimlist
                funcEntry func = new funcEntry(funcScope, fparamList, funcName, "function", funcType, funcTab);
                classList.add(func);
            }
        }
        //fetch the classEntry from the globalTable and set it's link to point to the new classTable
        symTab classTab = new symTab(className, classList);
        symTab prevClassTab = tables.get("GLOBAL");
        ArrayList<symTabEntry> prevClassEntries = prevClassTab.getTableList();
        for (symTabEntry prevEachEntry:
                prevClassEntries) {
            if (prevEachEntry.getName().equals(className)){
                prevEachEntry.setLink(classTab);
            }
        }

        // now the className in the globalList points to the table of this class
        className = className.concat("_CLASS");
        symTab prevClass = tables.get(className);
        prevClass.setTableList(classList);

        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }


    }

    @Override
    public void visit(funcDefNode p_node) {
        //differentiate between free func and class func based on the number of children
        //free function can be found in the hashmap and  globalList and class function will be there in the class table

        int numChild = p_node.getChildren().get(0).getChildren().size();
        String funcName = "";
        String funcType = "";
        String className = "";
        ArrayList<symTabEntry> funcList = new ArrayList<>();
        ArrayList<String> paramlist = new ArrayList<>();

        if (numChild == 3){ //if three child means it is a free function
            funcName = p_node.getChildren().get(0).getChildren().get(0).getData();
            node paramList = p_node.getChildren().get(0).getChildren().get(1);
            for (node fParamListChild :
                    paramList.getChildren()) {
                String paramType = fParamListChild.getChildren().get(0).getData();
                String paramName = fParamListChild.getChildren().get(1).getData();
                String dimlist =  fParamListChild.getChildren().get(2).getData();
                paramEntry param = new paramEntry(paramName, "parameter",  paramType);
                funcList.add(param);
                paramlist.add(paramType);
            }
            node variables = p_node.getChildren().get(1).getChildren().get(0);
            for (node varDecl :
                    variables.getChildren()) {
                String varType = varDecl.getChildren().get(0).getData();
                String varName = varDecl.getChildren().get(1).getData();
                String dimlist = varDecl.getChildren().get(2).getData();
                varEntry var = new varEntry(varName, "variable", varType);
                funcList.add(var);
            }
            //get table from the hash map and assign it
            symTab funcTab = tables.get(funcName);
            funcTab.setTableList(funcList);
            //get entry from the global table and assign the link
            symTab global = tables.get("GLOBAL");
            ArrayList<symTabEntry> prevFuncEntries = global.getTableList();
            for (symTabEntry prevEachEntry:
                    prevFuncEntries) {
                if (prevEachEntry.getName().equals(funcName)){
                    prevEachEntry.setLink(funcTab);
                    ((funcEntry) prevEachEntry).setParams(paramlist);
                }
            }
        }
        else { //the function belongs to a class because it has 4 children
            className = p_node.getChildren().get(0).getChildren().get(0).getData();
            funcName = p_node.getChildren().get(0).getChildren().get(1).getData();
            funcType = p_node.getChildren().get(0).getChildren().get(3).getData();
            node paramList = p_node.getChildren().get(0).getChildren().get(2);
            ArrayList<String> fparamList = new ArrayList<>();
            for (node fParamListChild :
                    paramList.getChildren()) {
                String paramType = fParamListChild.getChildren().get(0).getData();
                String paramName = fParamListChild.getChildren().get(1).getData();
                String dimlist =  fParamListChild.getChildren().get(2).getData();
                paramEntry param = new paramEntry(paramName, "parameter",  paramType);
                funcList.add(param);
                paramlist.add(paramType);
            }
            node variables = p_node.getChildren().get(1).getChildren().get(0);
            for (node varDecl :
                    variables.getChildren()) {
                String varType = varDecl.getChildren().get(0).getData();
                String varName = varDecl.getChildren().get(1).getData();
                String dimlist = varDecl.getChildren().get(2).getData();
                varEntry var = new varEntry(varName, "variable", varType);
                funcList.add(var);
            }
            symTab funcTable = new symTab(funcName, funcList);
            //get globaltable -> get classEntry -> get functionEntry -> make link
            symTab globalTab = tables.get("GLOBAL");
            ArrayList<symTabEntry> prevGlobalEntries = globalTab.getTableList();
            for (symTabEntry prevClassEntry :
                    prevGlobalEntries) {
                if (prevClassEntry.getName().equals(className)){
                    symTab classTable = prevClassEntry.getLink();
                    ArrayList<symTabEntry> prevGlobalClassEntry = classTable.getTableList();
                    for (symTabEntry prevGlobalClassFuncEntry :
                            prevGlobalClassEntry) {
                        if (prevGlobalClassFuncEntry.getName().equals(funcName)) {
                            prevGlobalClassFuncEntry = (funcEntry)prevGlobalClassFuncEntry;
                            prevGlobalClassFuncEntry.setLink(funcTable);
                            ((funcEntry) prevGlobalClassFuncEntry).setParams(paramlist);
                        }
                    }
                }
            }
            //get classtable from the hash map and assign it
            className = className.concat("_CLASS");
            symTab classTab = tables.get(className);
            ArrayList<symTabEntry> prevClassEntries = classTab.getTableList();
            for (symTabEntry prevEachEntry:
                    prevClassEntries) {
                if (prevEachEntry.getName().equals(funcName)){
                    prevEachEntry.setLink(funcTable);
                }
            }
        }
    }

    @Override
    public void visit(mainNode p_node) {
        ArrayList<symTabEntry> mainList = new ArrayList<>();
        node varDecls = p_node.getChildren().get(0).getChildren().get(0);
        for (node varDeclChild :
                varDecls.getChildren()) {
            String varType = varDeclChild.getChildren().get(0).getData();
            String varName = varDeclChild.getChildren().get(1).getData();
            String varDimList = varDeclChild.getChildren().get(2).getData();
            varEntry var = new varEntry(varName, "variable", varType);
            mainList.add(var);
        }
        symTab mainTab = tables.get("main");
        mainTab.setTableList(mainList);

        symTab globalTab = tables.get("GLOBAL");
        ArrayList<symTabEntry> prevMainEntry = globalTab.getTableList();
        for (symTabEntry prevEachEntry:
                prevMainEntry) {
            if (prevEachEntry.getName().equals("main")){
                prevEachEntry.setLink(mainTab);
            }
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

    public void printSymbolTable(){

        for (String key :
            tables.keySet()) {
            //special case of class functions
            if (key.contains("_CLASS")){
                String table = tables.get(key).toString();
                String[] _table = table.split("->");
                out.print(_table[0]+"->\n");
                String[] _entries = _table[1].split(",");
                for (String eachEntry :
                        _entries) {
                    out.print("\t"+eachEntry+"\n");
                    out.flush();
                }
                symTab classTable = tables.get(key);
                ArrayList<symTabEntry> classEntries = classTable.getTableList();
                for (symTabEntry eachEntry :
                        classEntries) {
                    if (eachEntry.getKind().equals("function")){
                        out.print("\t\t");
                        String funcTable = eachEntry.getLink().toString();
                        String[] _funcTable = funcTable.split("->");
                        out.print(_funcTable[0]+"->\n");
                        String[] _funcTableEntries = _funcTable[1].split(",");
                        for (String eachFuncEntry :
                                _funcTableEntries) {
                            out.print("\t\t\t"+eachFuncEntry+"\n");
                            out.flush();
                        }
                    }
                }
            }
            else {
                String table = tables.get(key).toString();
                String[] _entry = table.split("->");
                out.print(_entry[0]+"->\n");
                String[] _entries = _entry[1].split(",");
                for (String eachEntry :
                        _entries) {
                    out.print("\t"+eachEntry+"\n");
                    out.flush();
                }
            }
            out.println("\n");
        }
    }
}
