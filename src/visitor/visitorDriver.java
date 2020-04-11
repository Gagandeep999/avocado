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

public class visitorDriver {

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
                } else {
                    newTList.add(t);
                }
            }
            parse p = new parse(newTList, filename);
            if (p.parse()) {
//                System.out.println("Parser Finished");
            } else System.out.println("Error in parsing");

            String symTableFile = filename.replace(".src", ".outsymboltables");
            String symTableError = filename.replace(".src", ".outsemanticerrors");
            String moonfile = filename.replace(".src", ".m");
            Stack<node> astTree = p.ast;
//            System.out.println("tree available");

//            System.out.println("start to create symbol table");
            node prog = astTree.pop();
            typeCheckVisitor typeCheckVisit = new typeCheckVisitor(symTableError);
            symbolTableVisitor symTabVisit = new symbolTableVisitor(symTableError);
            memorySizeVisitor memorySizeVisit = new memorySizeVisitor(symTableError);
            codeGenVisitor codeGenVisit = new codeGenVisitor(moonfile);

            prog.accept(symTabVisit);
            prog.accept(typeCheckVisit);
            prog.accept(memorySizeVisit);
            System.out.println("symbol table created");
            prog.accept(codeGenVisit);
            System.out.println("moon code generated");



            PrintWriter out = new PrintWriter(new File(symTableFile));

            for (symTabEntry eachSymTabEntry :
                    prog.table.getTableList()) {
                out.print(eachSymTabEntry.getLink());
                ArrayList<symTabEntry> eachClassFuncEntry = eachSymTabEntry.getLink().getTableList();
                for (symTabEntry eachEntry :
                        eachClassFuncEntry) {
                    if (eachEntry.getKind().equals("function")) {
                        out.print(eachEntry.getLink());
                    }
                }
            }
            out.flush();
            out.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
