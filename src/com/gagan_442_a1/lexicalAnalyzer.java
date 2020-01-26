package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Pattern;


class lexicalAnalyzer {

    public static void main(String args[]){
        int currentChar = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexpositivegrading_1.src"));
            lexer lex = new lexer(br);
            lex.nextToken();
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