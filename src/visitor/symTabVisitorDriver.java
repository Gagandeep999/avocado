package visitor;

import lexer.lex;
import lexer.token;
import nodes.node;
import parser.parse;
import symbolTable.symTabEntry;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class symTabVisitorDriver {

    public static void main(String args[]){
        try {
            String filename = args[0];
            BufferedReader br = new BufferedReader(new FileReader(filename));
            lex lex = new lex(filename);
            lex.nextToken(br);
            LinkedList<token> tlist = lex.getTokenList();
            LinkedList<token> newTList = new LinkedList<>();
            for (token t : tlist) {
                if ((t.getToken().equals("inlinecmnt")) || (t.getToken().equals("blockcmnt"))) {
                    ;
                } else newTList.add(t);
            }
            parse parseTree = new parse(newTList, filename);
            parse parseVisitor = new parse(newTList, filename);
            if (parseTree.parse() && parseVisitor.parse()) {
                ;
            } else System.out.println("Error in parsing");

            String graphFile = filename.replace(".src", ".outast");
            String symTableFile = filename.replace(".src", ".outsymboltables");
            String symTableError = filename.replace(".src", ".outsemanticerrors");
            String moonfile = filename.replace(".src", ".m");

            PrintWriter ast = new PrintWriter(graphFile, "UTF-8");
            PrintWriter symTab = new PrintWriter(new File(symTableFile));

            Stack<node> astTree = parseTree.ast;
            Stack<node> astVisitor = parseVisitor.ast;

            //printing the graph
            ast.println("digraph AST {");
            while (!astTree.isEmpty()){
                node x = astTree.pop();
                ast.println(x.getMyNum()+"[label=\""+x.getData()+"\"]");
                ast.flush();
                for (node child :
                        x.getChildren()) {
                    ast.println(x.getMyNum()+"->"+child.getMyNum());
                    ast.flush();
                    astTree.push(child);
                }
            }
            ast.println("}");
            ast.flush();

            node prog = astVisitor.pop();

//            typeCheckVisitor typeCheckVisit = new typeCheckVisitor(symTableError);
//            symbolTableVisitor symTabVisit = new symbolTableVisitor(symTableError);
//            memorySizeVisitor memorySizeVisit = new memorySizeVisitor(symTableError);

//            prog.accept(symTabVisit);
//            prog.accept(typeCheckVisit);
//            prog.accept(memorySizeVisit);
            System.out.println("symbol table created");

            //printing the symbol table
            symTab.print(prog.table);
            for (symTabEntry eachSymTabEntry :
                    prog.table.getTableList()) {
                symTab.print(eachSymTabEntry.getLink());
                ArrayList<symTabEntry> eachClassFuncEntry = eachSymTabEntry.getLink().getTableList();
                for (symTabEntry eachEntry :
                        eachClassFuncEntry) {
                    if (eachEntry.getKind().equals("function")) {
                        symTab.print(eachEntry.getLink());
                    }
                }
            }
            symTab.flush();
            symTab.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

