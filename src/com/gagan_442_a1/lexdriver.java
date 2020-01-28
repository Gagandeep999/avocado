package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class lexdriver {
    /**
     * Starting point of the lexical analyzer. Creates a lexer object that calls nextToken()
     * method.
     * MAYBE in this method i can define a bufferewriter that will store the returned tokens
     * and once all the token are returned write them to file.
     * @param args filename for the analyzer
     */
    public static void main(String args[]){
        try{
            String filename = args[0];
//            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexpositivegrading_1.src"));
//            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexnegativegrading.src"));
            BufferedReader br = new BufferedReader(new FileReader(filename));
            lexer lex = new lexer(filename);
            lex.nextToken(br);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}