package visitor;

import lexer.lex;
import lexer.token;
import nodes.node;
import org.junit.Test;
import parser.parse;
import symbolTable.symTabEntry;

import java.io.*;
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
                    } else newTList.add(t);
                }
                parse parseTree = new parse(newTList, filename);
                parse parseVisitor = new parse(newTList, filename);
                if (parseTree.parse() && parseVisitor.parse()) {
                    ;
                } else System.out.println("Error in parsing");

                String graphFile = filename.replace(".src", ".outast");
                String symTableFile = filename.replace(".src", ".outsymboltables");
                String symTableError = filename.replace(".src", ".outsemanticerrors");
                String moonFile = filename.replace(".src", ".m");

                PrintWriter ast = new PrintWriter(graphFile, "UTF-8");
                PrintWriter symTab = new PrintWriter(new File(symTableFile));

                Stack<node> astTree = parseTree.ast;
                Stack<node> astVisitor = parseVisitor.ast;

                //printing the graph
                ast.println("digraph AST {");
                while (!astTree.isEmpty()){
                    node x = astTree.pop();
                    ast.println(x.getMyNum()+"[label=\""+x.getData()+"\"]");
                    ast.flush();
                    for (node child :
                            x.getChildren()) {
                        ast.println(x.getMyNum()+"->"+child.getMyNum());
                        ast.flush();
                        astTree.push(child);
                    }
                }
                ast.println("}");
                ast.flush();

                node prog = astVisitor.pop();

                typeCheckVisitor typeCheckVisit = new typeCheckVisitor(symTableError);
                symbolTableVisitor symTabVisit = new symbolTableVisitor(symTableError);
                memorySizeVisitor memorySizeVisit = new memorySizeVisitor(symTableError);
                codeGenVisitor codeGenVisit = new codeGenVisitor(moonFile);

                prog.accept(symTabVisit);
                prog.accept(typeCheckVisit);
                prog.accept(memorySizeVisit);
                prog.accept(codeGenVisit);

                //printing the symbol table
                symTab.print(prog.table);
                for (symTabEntry eachSymTabEntry :
                        prog.table.getTableList()) {
                    symTab.print(eachSymTabEntry.getLink());
                    ArrayList<symTabEntry> eachClassFuncEntry = eachSymTabEntry.getLink().getTableList();
                    for (symTabEntry eachEntry :
                            eachClassFuncEntry) {
                        if (eachEntry.getKind().equals("function")) {
                            symTab.print(eachEntry.getLink());
                        }
                    }
                }

                symTab.flush();
                symTab.close();

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



