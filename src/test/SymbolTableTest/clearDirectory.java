package test.SymbolTableTest;

import java.io.File;

class clearDirectory {
    public static void main(String args[]) {
        File folder = new File("src/test/SymbolTableTest");
        for (File file : folder.listFiles()) {
            if ( (file.getName().endsWith(".outderivation")) || (file.getName().endsWith(".outlexerrors"))
                    || (file.getName().endsWith(".outlextokens")) || (file.getName().endsWith(".outsyntaxerrors")) ) {
                file.delete();
            }
        }
    }
}