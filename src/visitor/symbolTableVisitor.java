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
    PrintWriter out;
    PrintWriter err;
    HashMap<String, ArrayList<symTab>> tables;
    int funcOverLoadCounter;
    boolean isFuncOverload;
    boolean isClassFuncOverloaded;
    int tempVarNum;

    public symbolTableVisitor(){
        this.outputfile = "";
        this.tables = new HashMap<>();
    }

    public symbolTableVisitor(String outputfile, String errorFile){
        this.outputfile = outputfile;
        this.errorFile = errorFile;
        this.tables = new HashMap<>();
        try {
            this.out = new PrintWriter(new File(this.outputfile));
            this.err = new PrintWriter(new File(this.errorFile));
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        this.funcOverLoadCounter = 0;
        this.isFuncOverload = false;
        this.isClassFuncOverloaded = false;
        this.tempVarNum = 0;
    }

    public String getNewTempVarName(){
        tempVarNum++;
        return "t" + Integer.toString(tempVarNum);
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
        this.out.print(p_node.table);
        for (symTabEntry eachSymTabEntry :
                p_node.table.getTableList()) {
            this.out.print(eachSymTabEntry.getLink());
            ArrayList<symTabEntry> eachClassFuncEntry = eachSymTabEntry.getLink().getTableList();
            for (symTabEntry eachEntry :
                    eachClassFuncEntry) {
                if (eachEntry.getKind().equals("function")) {
                    this.out.print(eachEntry.getLink());
                }
            }
        }
        this.out.flush();
        this.out.close();
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
                fParams.add(fparam.getChildren().get(0).getData()+":"+fparam.getChildren().get(1).getData());
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
    public void visit(addOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }
        String leftChildType = getChildType(p_node, 0);
        String rightChildType = getChildType(p_node, 1);

        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);
        }
        else{
            System.out.println("error type mismatch");
            System.exit(1);
        }

        String tempvarname = this.getNewTempVarName();
        p_node.moonVarName = tempvarname;
        p_node.entry = new varEntry(tempvarname, "variable", p_node.getType());
        p_node.table.addEntry(p_node.entry);
    }

    @Override
    public void visit(multOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.table = p_node.table;
            child.accept(this);
        }

        String leftChildType = getChildType(p_node, 0);
        String rightChildType = getChildType(p_node, 1);

        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);
        }
        else{
            System.out.println("error type mismatch");
            System.exit(1);
        }

        String tempvarname = this.getNewTempVarName();
        p_node.moonVarName = tempvarname;
        p_node.entry = new varEntry(tempvarname, "variable", p_node.getType());
        p_node.table.addEntry(p_node.entry);
    }

    @Override
    public void visit(numNode p_node) {
        String tempvarname = this.getNewTempVarName();
        p_node.moonVarName = tempvarname;
        p_node.entry = new varEntry(tempvarname, "variable", p_node.getType());
        p_node.table.addEntry(p_node.entry);
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
    public void visit(funcDeclNode p_node) {
//        System.out.println("inseid funcDecl");
        String funcScope = p_node.getChildren().get(0).getData();
        String funcName = p_node.getChildren().get(1).getData();
        node paramList = p_node.getChildren().get(2);
        String funcType = p_node.getChildren().get(3).getData();
        ArrayList<String> fparamList = new ArrayList<>();
                for (node fparam :
                        paramList.getChildren()) {
                    fparamList.add(fparam.getChildren().get(0).getData()+":"+fparam.getChildren().get(1).getData());
                }
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

    //helper method to determine the child datatype
    private String getChildType(node p_node, int childNum){
        String childType = "";
        if (p_node.getChildren().get(childNum).getClass().toString().contains("generalNode")){ //meaning that it is not numNode
            String varName = p_node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData();
            for (symTabEntry entry :
                    p_node.table.getTableList()) {
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
}
