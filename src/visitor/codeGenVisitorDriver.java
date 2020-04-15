package visitor;

import lexer.lex;
import lexer.token;
import nodes.node;
import org.junit.Test;
import parser.parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class codeGenVisitorDriver {

    public static void main(String args[]){
        // if no arguments are passed then test the output of the moon file for all test cases
        if (args.length==0){
            executeSimpleMoonProgram_withPutStatements_getPrintedResults("src/test/CodeGenTest/test_1.m", "5");
            executeSimpleMoonProgram_withPutStatements_getPrintedResults("src/test/CodeGenTest/test_2.m", "10");
            executeSimpleMoonProgram_withPutStatements_getPrintedResults("src/test/CodeGenTest/test_3.m", "25");
            executeSimpleMoonProgram_withPutStatements_getPrintedResults("src/test/CodeGenTest/test_4.m", "30");
        } // arguments should be the path of the file; it generates the moon code for that specific file, but does not
          // check the moon output
        else {
            try {
                String filename = args[0];
                BufferedReader br = new BufferedReader(new FileReader(filename));
                lex lex = new lex(filename);
                lex.nextToken(br);
                LinkedList<token> tlist = lex.getTokenList();
                LinkedList<token> newTList = new LinkedList<>();
                for (token t : tlist) {
                    if ((t.getToken().equals("inlinecmnt")) || (t.getToken().equals("blockcmnt"))) {
                        ;
                    } else {
                        newTList.add(t);
                    }
                }
                parse p = new parse(newTList, filename);
                if (p.parse()) {
                    ;
                } else System.out.println("Error in parsing");

                String symTableFile = filename.replace(".src", ".outsymboltables");
                String symTableError = filename.replace(".src", ".outsemanticerrors");
                String moonFile = filename.replace(".src", ".m");

                Stack<node> astTree = p.ast;

                node prog = astTree.pop();

                symbolTableVisitor symTabVisit = new symbolTableVisitor(symTableError);
                typeCheckVisitor typeCheckVisit = new typeCheckVisitor(symTableError);
                memorySizeVisitor memorySizeVisit = new memorySizeVisitor(symTableError);
                codeGenVisitor codeGenVisit = new codeGenVisitor(moonFile);

                prog.accept(symTabVisit);
                prog.accept(typeCheckVisit);
                prog.accept(memorySizeVisit);
                prog.accept(codeGenVisit);

            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public static void executeSimpleMoonProgram_withPutStatements_getPrintedResults(String moonFile, String expected) {

        moonFile += " src/test/CodeGenTest/util.m";
        CodeGenHelper.MoonResult result = CodeGenHelper.executeMoonProgram(moonFile);
        List<String> output = result.getUsefulOutput();

        assertEquals(1, output.size());
        assertEquals(expected, output.get(0));

    }
}



