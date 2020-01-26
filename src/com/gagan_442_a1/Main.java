package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) {
	// write your code here
        // starting point of teh application
        // reads everything into a var "source" line-ny-line
        // and sends it to lexicalAnalyzer.
        try{
            int lineNo = 0;
//            token t = new token();
            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexpositivegrading_1.src"));
//            lexicalAnalyzer lex = new lexicalAnalyzer(br);
            int currentChar = 0;
            do {
                currentChar = br.read();
                System.out.println(currentChar);
//                t = lex.nextToken();
            } while(currentChar!=(-1));






//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while(line != null){
////                System.out.println(line);
//                lineNo++;
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            sb.append("-1"); //to mark the end of my string builder
//            token t = new token();
//            lexicalAnalyzer lex = new lexicalAnalyzer(sb);
//            do{
//                lineNo--;
//                t = lex.nextToken();
//                if((t.getToken().equals("tabspace")||(t.getToken().equals("space")))){
//                    System.out.print(" ");
//                }else if (t.getToken().equals("newline")){
//                    System.out.println();
//                }else{
//                    System.out.print(t);
//                }
//            }while(lineNo!=0);

        }catch (IOException x){
            System.out.println(x.getMessage());
        }

    }

}




