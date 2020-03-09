package parser;
import lexer.token;
import lexer.lex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Stack;

public class parsedriver {

    public static void main (String args[]){
        try{
            String filename = args[0];
            BufferedReader br = new BufferedReader(new FileReader(filename));
            lex lex = new lex(filename);
            lex.nextToken(br);
            LinkedList<token> tlist = lex.getTokenList();
            LinkedList<token> newTList = new LinkedList<>();
            for (token t : tlist){
                if ( (t.getToken().equals("inlinecmnt")) || (t.getToken().equals("blockcmnt")) ){
                    ;
                }else {
                    newTList.add(t);
                }
            }
            parse p = new parse(newTList, filename);
            if (p.parse()){
                System.out.println("Parser Finished");
            }else System.out.println("Error in parsing");

            String graphFile = filename.replace(".src", ".gv");

            PrintWriter ast = new PrintWriter(graphFile, "UTF-8");
            Stack<node> astTree = p.ast;
            System.out.println("tree available");

            ast.println("digraph AST {");
            while (!astTree.isEmpty()){
                node x = astTree.pop();
                ast.println(x.num+"[label=\""+x.name+"\"]");
                ast.flush();
                for (node child :
                        x.children) {
                    ast.println(x.num+"->"+child.num);
                    ast.flush();
                    astTree.push(child);
                }
            }
            ast.println("}");
            ast.flush();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}