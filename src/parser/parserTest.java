package parser;

import lexer.lex;
import org.junit.Test;

import java.io.*;

public class parserTest {

    public static void main(String args[]){

        testBubble();

        deleteFile();
    }

    @Test
    private static void testBubble(){
        try {
            BufferedReader bubble = new BufferedReader(new FileReader("src/test/ParserTest/bubblesort.src"));
            lex lexer = new lex("src/parser/bubble.outlextokens");
            lexer.nextToken(bubble);
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }


    }

    private static void deleteFile() {
        File parseDerivation = new File("src/parser/testLex.outderivation");
        File parserError = new File("src/lexer/testLex.outsyntaxerror");
        File parserAST = new File("src/lexer/testLex.gv");
        if (parseDerivation.exists()) {
            parseDerivation.delete();
        } else {
            System.err.println(
                    "I cannot find '" + parseDerivation + "' ('" + parseDerivation.getAbsolutePath() + "')");
        }
        if (parserError.exists()) {
            parserError.delete();
        } else {
            System.err.println(
                    "I cannot find '" + parserError + "' ('" + parserError.getAbsolutePath() + "')");
        }
        if (parserAST.exists()) {
            parserAST.delete();
        } else {
            System.err.println(
                    "I cannot find '" + parserAST + "' ('" + parserAST.getAbsolutePath() + "')");
        }
    }
}
