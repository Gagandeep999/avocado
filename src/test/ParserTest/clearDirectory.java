package test.ParserTest;

import java.io.File;

class clearDirectory {
    public static void main(String args[]) {
        File folder = new File("src/test/ParserTest");
        for (File file : folder.listFiles()) {
            if ( (file.getName().endsWith(".outlextokens")) || (file.getName().endsWith(".outlexerrors")) ){
                file.delete();
            }
        }
    }
}