package visitor;

import com.sun.xml.internal.bind.v2.TODO;
import nodes.*;
import symbolTable.symTab;
import symbolTable.symTabEntry;
import symbolTable.varEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
//        System.out.println("addOpNode in typeCheck");
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
            this.errors += "error in addOpNode";
        }
    }

    @Override
    public void visit(multOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("multOpNode in typeCheck");
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
            this.errors += "error in multOpNode";
        }
    }

    @Override
    public void visit(compareOpNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("compareOpNode in type check");
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
            this.errors += "error in compareOpNode";
        }
    }

    @Override
    public void visit(assignStatNode p_node) {
        for (node child :
        p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("assignStatNode in type check");
        String leftChildType = "";

        String leftChildName = p_node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData();
        for (symTabEntry entry :
                p_node.table.getTableList()) {
            if (entry.getName().equals(leftChildName)) {
                leftChildType = entry.getType();
                break;
            }
        }

        String rightChildType = getChildType(p_node,1);
        if (leftChildType.equals(rightChildType)) {
            p_node.setType(leftChildType);
        } else if (leftChildType.isEmpty()){
            err.println("variable \"" + leftChildName + "\" is not declared.");
//            this.errors += "variable \"" + leftChildName + "\" is not declared.";
        }else {
            p_node.setType("ERROR");
            err.println("type mismatch in assignment");
//            this.errors += "type mismatch in for assignment";
        }
        err.flush();
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

    @Override
    public void visit(classListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("classlist node in type check");
        checkDuplicates(p_node, "class");
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
        System.out.println("funcDecl in typecheck");
        checkDuplicates(p_node, "variable");
    }

    @Override
    public void visit(classNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        checkDuplicates(p_node, "variable");
        checkDuplicates(p_node, "function");
    }

    @Override
    public void visit(funcDefListNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("funcDefListNode in typecheck");
        checkDuplicates(p_node, "function");
    }

    @Override
    public void visit(funcDefNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
        checkDuplicates(p_node, "variable");
        checkDuplicates(p_node, "function");
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
//        System.out.println("mainNode in typeCheck");
        checkDuplicates(p_node, "variable");
    }

    @Override
    public void visit(fparamNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(funcCallNode p_node) {
//        System.out.println("before visitng children");
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//        System.out.println("funcCallNode in typecheck");
        chekckFunctionExists(p_node);
        numOfParameter(p_node);
    }

    @Override
    public void visit(returnNode p_node) {
        for (node child :
                p_node.getChildren()) {
            child.accept(this);
        }
//         System.out.println("returnNode in typecheck");
        String funcReturnType = getChildType(p_node, 0);
        String funcName = p_node.table.getName();
        for (symTabEntry entry :
                p_node.table.getUpperTable().getTableList()) {
            if ( (funcName.equals(entry.getName())) && (funcReturnType.equals(entry.getType())) ){
                //this means that the return type is good
                break;
            }else {
                err.println("return type mismatch for function \"" + funcName + "\"");
                break;
            }
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

    public void checkDuplicates(node p_node, String kind){
        ArrayList<String> idList = new ArrayList<>();

        for (symTabEntry entry :
                p_node.getTable().getTableList()) {
            if (entry.getKind().equals(kind)){
                idList.add(entry.getName());
            }
        }
        Set<String> idSet = new HashSet<>(idList);

        if (idSet.size() < idList.size()){
            if (kind.equals("function")){
                err.println("WARNING: function overloading");
            } else {
                ArrayList<symTabEntry> noDuplicate = new ArrayList<>();
                for (symTabEntry entry :
                        p_node.getTable().getTableList()) {
                    if (!noDuplicate.contains(entry)){
                        noDuplicate.add(entry);
                    } else {
                        err.println("ERROR: duplicate " + kind + " \"" + entry.getName()  + "\" found in " + p_node.getData());
                    }
                }
                p_node.getTable().setTableList(noDuplicate);
            }
        }
        err.flush();
    }

    public void chekckFunctionExists(node p_node){
        String funcName = p_node.getParent().getChildren().get(0).getData();
        symTab upperTable = p_node.getTable().getUpperTable();
        int count = 0;
        for (symTabEntry entry :
                upperTable.getTableList()) {
            if (entry.getName().equals(funcName)){
                count++;
            }
        }
        if (count<1){
            err.println("function \"" + funcName + "\" is not declared.");
        }
    }

    public void numOfParameter(node p_node){
        String funcName = p_node.getParent().getChildren().get(0).getData();
        symTab upperTable = p_node.getTable().getUpperTable();
        ArrayList<String> params = new ArrayList<>();
        for (symTabEntry entry :
                upperTable.getTableList()) {
            if ((entry.getName().equals(funcName)) && (entry.getKind().equals("function"))){
                symTab funcTable = entry.getLink();
                for (symTabEntry funcEntries :
                        funcTable.getTableList()) {
                    if (funcEntries.getKind().equals("parameter")){
                        params.add(funcEntries.getType());
                    }
                }
            }
        }
        if (p_node.getChildren().size()!=params.size()){
            err.println("number of parameter for function \"" + funcName + "\" mismatch");
        }
    }
}
