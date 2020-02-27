package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class parsedriver {

    public static void main (String args[]){
        try{
            String filename = args[0];
            BufferedReader br = new BufferedReader(new FileReader(filename));
            lexer lex = new lexer(filename);
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
            parser p = new parser(newTList, filename);
            if (p.parse()){
                System.out.println("Parser Finished");
            }else System.out.println("Error in parsing");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}