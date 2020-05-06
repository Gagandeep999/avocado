package test.SymbolTableTest;

import java.io.File;

class clearDirectory {
    public static void main(String args[]) {
        File folder = new File("src/test/SymbolTableTest");
        deleteFiles(folder);
    }

    public static void deleteFiles(File folder){
        for (File file : folder.listFiles()) {
            if ( (file.getName().endsWith(".outderivation")) || (file.getName().endsWith(".outlexerrors"))
                    || (file.getName().endsWith(".outlextokens")) || (file.getName().endsWith(".outsyntaxerrors"))
                    || (file.getName().endsWith(".outast") )) {
                file.delete();
            }
        }
    }

    public static void runAll(File folder){
        for (File file : folder.listFiles()){
            if (file.getName().endsWith(".src")){
                System.out.println("how to run code??");
            }
        }
    }
}