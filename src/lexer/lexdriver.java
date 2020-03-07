package lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class lexdriver {
    /**
     * Starting point of the lexical analyzer. Creates a lex object that calls nextToken()
     * method.
     * @param args filename for the analyzer
     */
    public static void main(String args[]){
        try{
            String filename = args[0];
            BufferedReader br = new BufferedReader(new FileReader(filename));
            lex lex = new lex(filename);
            lex.nextToken(br);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}