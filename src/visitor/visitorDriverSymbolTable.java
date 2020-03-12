package visitor;

import lexer.lex;
import lexer.token;
import nodes.node;
import parser.parse;

import java.io.*;
import java.util.LinkedList;
import java.util.Stack;

public class visitorDriverSymbolTable {

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
                System.out.println("Parser Finished");
            } else System.out.println("Error in parsing");

            String symTableFile = filename.replace(".src", ".sym");
            PrintWriter symtab = new PrintWriter(symTableFile, "UTF-8");
            Stack<node> astTree = p.ast;
            System.out.println("tree available");

            System.out.println("start to create symbol table");
            symbolTableVisitor symTabVisit = new symbolTableVisitor(symTableFile);
            astTree.pop().accept(symTabVisit);
            System.out.println("symbol table created");

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
