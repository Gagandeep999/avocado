package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class lexicalAnalyzer {
    /**
     * Starting point of the lexical analyzer. Creates a lexer object that calls nextToken()
     * method.
     * MAYBE in this method i can define a bufferewriter that will store the returned tokens
     * and once all the token are returned write them to file.
     * @param args
     */
    public static void main(String args[]){
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexpositivegrading_1.src"));
//            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexnegativegrading.src"));
            lexer lex = new lexer();
            lex.nextToken(br);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

//    public enum tokenType{
//        Identifier,
//        Integer,
//        Fraction,
//        Flot,
//        Plus,
//        Minus,
//        Multiply,
//        Divide,
//        Equal,
//        Openround,
//        Closeround,
//        Opencurly,
//        Closecurly,
//        Opensquare,
//        Closesquare,
//        Semicolon,
//        Comma,
//        Period,
//        Colon,
//        Doublecolon,
//        Equalequal,
//        Notequal,
//        Lessthan,
//        Greaterthan,
//        Lessthanequal,
//        Greaterthanequal,
//        If,
//        Then,
//        Else,
//        While,
//        Class,
//        Do,
//        End,
//        Public,
//        Private,
//        Or,
//        And,
//        Read,
//        Write,
//        Return,
//        Main,
//        Inherit,
//        Local,
//        Blockcmnt,
//        Inlinecmnt;
//    }
}