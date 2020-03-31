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
    PrintWriter out;
    HashMap<String, ArrayList<symTab>> tables;
    int funcOverLoadCounter;
    boolean isFuncOverload;
    boolean isClassFuncOverloaded;

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
        this.funcOverLoadCounter = 0;
        this.isFuncOverload = false;
        this.isClassFuncOverloaded = false;
    }

    @Override
    public void visit(progNode p_node) {

        ArrayList<symTabEntry> globalListEntry = new ArrayList<>();
        ArrayList<symTab> funcList = new ArrayList<>();
        ArrayList<symTab> globalListTable = new ArrayList<>();

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
                    ArrayList<symTab> classList = new ArrayList<>();
                    String className = classChild.getChildren().get(0).getData();
                    symTab classTab = new symTab(className);
                    symTabEntry classEntry = new symTabEntry(className, "class", " ", classTab);
                    globalListEntry.add(classEntry);
                    className = className.concat("_CLASS");
                    classList.add(classTab);
                    tables.put(className, classList);
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
                        if (tables.containsKey(funcName)){ // function overloading
                            ArrayList<symTab> prevFuncList = tables.get(funcName);
                            prevFuncList.add(funcTab);
                            globalListEntry.add(funcEntryTab);
                            isFuncOverload = true;
                        }else{ // no function overloading
                            globalListEntry.add(funcEntryTab);
                            funcList.add(funcTab);
                            tables.put(funcName, funcList);
                        }
                    }
                }
            }else {
                /**
                 * create an empty table for the main function -> add entry to globalList
                 */
                ArrayList<symTab> mainList = new ArrayList<>();
                symTab mainTab = new symTab("main");
                symTabEntry mainEntry = new symTabEntry("main", "mainFunction", "void", mainTab);
                globalListEntry.add(mainEntry);
                mainList.add(mainTab);
                tables.put("main", mainList);
            }
        }
        //create the global table and add it to the hash map with key global
        symTab globalTab = new symTab("GLOBAL", globalListEntry);
        globalListTable.add(globalTab);
        tables.put("GLOBAL", globalListTable);

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
                for (symTabEntry classFunc :
                        classList) {
                    if (classFunc.getName().equals(funcName)){
                        isClassFuncOverloaded = true;
                    }
                }
                classList.add(func);
            }
        }
        //fetch the classEntry from the globalTable and set it's link to point to the new classTable
        symTab classTab = new symTab(className, classList);
        symTab prevClassTab = tables.get("GLOBAL").get(0);
        ArrayList<symTabEntry> prevClassEntries = prevClassTab.getTableList();
        for (symTabEntry prevEachEntry:
                prevClassEntries) {
            if (prevEachEntry.getName().equals(className)){
                prevEachEntry.setLink(classTab);
            }
        }

        // now the className in the globalList points to the table of this class
        className = className.concat("_CLASS");
        symTab prevClass = tables.get(className).get(0);
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
            //create a new table for the function
            symTab funcTab = new symTab(funcName, funcList);

            //get table from the hash map and assign it
            ArrayList<symTab> prevFuncTabList = tables.get(funcName);
            if (prevFuncTabList.size() != 1){ // function is overloaded
                symTab prevFuncTab = prevFuncTabList.get(funcOverLoadCounter);
                prevFuncTab.setTableList(funcList);
                funcOverLoadCounter++;
            }else { // function is not overloaded
                symTab prevFuncTab = prevFuncTabList.get(0);
                prevFuncTab.setTableList(funcList);
            }

            //get entry from the global table and assign the link
            symTab global = tables.get("GLOBAL").get(0);
            for (symTabEntry prevEachEntry:
                    global.getTableList()) {
                if (prevEachEntry.getName().equals(funcName) && !((funcEntry)prevEachEntry).isOverloaded() ) {
                    if (((funcEntry) prevEachEntry).isOverloaded()) {
                        prevEachEntry.setLink(funcTab);
                        ((funcEntry) prevEachEntry).setParams(paramlist);
                        ((funcEntry) prevEachEntry).setOverloaded(true);
                        break;
                    } else {
                        continue;
                    }
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
            symTab globalTab = tables.get("GLOBAL").get(0);
            for (symTabEntry prevClassEntry :
                    globalTab.getTableList()) {
                if (prevClassEntry.getName().equals(className)){
                    symTab classTable = prevClassEntry.getLink();
                    for (symTabEntry prevGlobalClassFuncEntry :
                            classTable.getTableList()) {
                        if (prevGlobalClassFuncEntry.getName().equals(funcName)
                                && !((funcEntry) prevGlobalClassFuncEntry).isOverloaded() ) {
                            prevGlobalClassFuncEntry.setLink(funcTable);
                            ((funcEntry) prevGlobalClassFuncEntry).setParams(paramlist);
                            ((funcEntry) prevGlobalClassFuncEntry).setOverloaded(true);
                            break;
                        }else {
                            continue;
                        }
                    }
                }
            }
            //get classtable from the hash map and assign it
            className = className.concat("_CLASS");
            symTab classTab = tables.get(className).get(0);
            for (symTabEntry prevEachEntry:
                    classTab.getTableList()) {
                if (prevEachEntry.getName().equals(funcName) && !((funcEntry) prevEachEntry).isOverloaded()){
                    prevEachEntry.setLink(funcTable);
                    ((funcEntry) prevEachEntry).setOverloaded(true);
                    break;
                }else {
                    continue;
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
        symTab mainTab = tables.get("main").get(0);
        mainTab.setTableList(mainList);

        symTab globalTab = tables.get("GLOBAL").get(0);
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

    private void printSymbolTable(){
        for (String key :
            tables.keySet()) {
            //firstly print all the entries of the global table
            if (key.contains("GLOBAL")){
                String globalTableStr = tables.get(key).toString();
                String[] _globalEntry = globalTableStr.split("->");
                out.print(_globalEntry[0]+"->\n");
                String[] _globalEntries = _globalEntry[1].split(",");
                for (String eachGlobalEntry :
                        _globalEntries) {
                    out.print("\t"+eachGlobalEntry+"\n");
                    out.flush();
                }
//                out.println("\n");
                //now get the table and print table of each function and class
                symTab globalTableTab = tables.get("GLOBAL").get(0);
                ArrayList<symTabEntry> globalEntries = globalTableTab.getTableList();
                for (symTabEntry eachEntry :
                        globalEntries) {
                    if (eachEntry.getKind().equals("function") || eachEntry.getKind().equals("mainFunction")){
                        //if it is a free function entry; move cursor to newline
                        //newline -> funcName ->newline -> tabspace -> FuncEntry ->newline
                        out.print("\n");
                        String globalFuncTable = eachEntry.getLink().toString();
                        String[] _globalFuncTable = globalFuncTable.split("->");
                        out.print(_globalFuncTable[0]+"->\n");
                        String[] _globalfuncTableEntries = _globalFuncTable[1].split(",");
                        for (String eachGlobalFuncEntry :
                                _globalfuncTableEntries) {
                            out.print("\t"+eachGlobalFuncEntry+"\n");
                            out.flush();
                        }
                    }
                    else if (eachEntry.getKind().equals("class")){
                        //if it is a class, first print the class table
                        //newline -> classname ->newline -> tabspace -> classEntry ->newline
                        out.print("\n");
                        String classTableStr = eachEntry.getLink().toString();
                        String[] _classTable = classTableStr.split("->");
                        out.print(_classTable[0]+"->\n");
                        String[] _classEntries = _classTable[1].split(",");
                        for (String eachClassEntry :
                                _classEntries) {
                            out.print("\t"+eachClassEntry+"\n");
                            out.flush();
                        }
                        symTab classTableTab = eachEntry.getLink();
                        ArrayList<symTabEntry> classEntries = classTableTab.getTableList();
                        for (symTabEntry eachClassEntryTab :
                                classEntries) {
                            if (eachClassEntryTab.getKind().equals("function")){
                                //if it is a class function entry
                                //we don't want to add extra space, so we don't add new line here
                                //two tabspace -> classFuncName -> newline -> three tabspace -> classFuncEntry -> newline
                                out.print("\t\t");
                                String classFuncTable = eachClassEntryTab.getLink().toString();
                                String[] _classFuncTable = classFuncTable.split("->");
                                out.print(_classFuncTable[0]+"->\n");
                                String[] _classFuncTableEntries = _classFuncTable[1].split(",");
                                for (String eachClassFuncEntry :
                                        _classFuncTableEntries) {
                                    out.print("\t\t\t"+eachClassFuncEntry+"\n");
                                    out.flush();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
