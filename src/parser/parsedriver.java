package parser;
import lexer.token;
import lexer.lex;
import nodes.node;

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
                ast.println(x.getNum()+"[label=\""+x.getName()+"\"]");
                ast.flush();
                for (node child :
                        x.getChildren()) {
                    ast.println(x.getNum()+"->"+child.getNum());
                    ast.flush();
//                    ast.flush();
//                    ast.println(child.num+"[label=\""+child.name+"\"]");
//                    ast.flush();

//                    ast.println("\""+x.name+"\" -> \""+child.name+"\"");
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