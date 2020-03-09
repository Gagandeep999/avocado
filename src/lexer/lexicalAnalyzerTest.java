package lexer;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.*;

public class lexicalAnalyzerTest {

    public static void main(String args[]) {

        tester("123", "[intnum, 123, 1] ", "");
        tester("a_bc", "[id, a_bc, 1] ", "");
        tester("_abc", "", "[invalidid, _abc, 1] ");
        tester("1abc", "", "[invalidid, 1abc, 1] ");
        tester("0", "[intnum, 0, 1] ", "");
        tester("123", "[intnum, 123, 1] ", "");
        tester("00", "", "[invalidinteger, 00, 1] ");
        tester("1.23", "[floatnum, 1.23, 1] ", "");
        tester("1.1e+1", "[floatnum, 1.1e+1, 1] ", "");
        tester("1.0", "[floatnum, 1.0, 1] ", "");
        tester("120.340e10", "", "[invalidfloat, 120.340e10, 1] ");
        tester("if", "[if, if, 1] ", "");
        tester("do", "[do, do, 1] ", "");
        tester("then", "[then, then, 1] ", "");
        tester("end", "[end, end, 1] ", "");
        tester("else", "[else, else, 1] ", "");
        tester("public", "[public, public, 1] ", "");
        tester("while", "[while, while, 1] ", "");
        tester("private", "[private, private, 1] ", "");
        tester("class", "[class, class, 1] ", "");
        tester("or", "[or, or, 1] ", "");
        tester("integer", "[integer, integer, 1] ", "");
        tester("and", "[and, and, 1] ", "");
        tester("float", "[float, float, 1] ", "");
        tester("not", "[not, not, 1] ", "");
        tester("read", "[read, read, 1] ", "");
        tester("inherits", "[inherits, inherits, 1] ", "");
        tester("write", "[write, write, 1] ", "");
        tester("local", "[local, local, 1] ", "");
        tester("return", "[return, return, 1] ", "");
        tester("main", "[main, main, 1] ", "");
        tester("a\na", "[id, a, 1] \n[id, a, 2] ", "");
        tester("+", "[plus, +, 1] ", "");
        tester("-", "[minus, -, 1] ", "");
        tester("/", "[div, /, 1] ", "");
        tester("*", "[mult, *, 1] ", "");
        tester("(", "[lpar, (, 1] ", "");
        tester(")", "[rpar, ), 1] ", "");
        tester("[", "[lsqbr, [, 1] ", "");
        tester("]", "[rsqbr, ], 1] ", "");
        tester("{", "[lcurbr, {, 1] ", "");
        tester("}", "[rcurbr, }, 1] ", "");
        tester("<", "[lessthan, <, 1] ", "");
        tester(">", "[gt, >, 1] ", "");
        tester("<=", "[leq, <=, 1] ", "");
        tester(">=", "[geq, >=, 1] ", "");
        tester("<>", "[neq, <>, 1] ", "");
        tester("=", "[equal, =, 1] ", "");
        tester(";", "[semi, ;, 1] ", "");
        tester(",", "[comma, ,, 1] ", "");
        tester(".", "[dot, ., 1] ", "");
        tester(":", "[colon, :, 1] ", "");
        tester("::", "[sr, ::, 1] ", "");
        tester("// inline", "[inlinecmnt, // inline, 1] ", "");
        tester("/* block comment one line */", "[blockcmnt, /* block comment one line */, 1] ", "");
        tester("// comment /* inside */ comment", "[inlinecmnt, // comment /* inside */ comment, 1] ", "");
        tester("int *a=5", "[id, int, 1] [mult, *, 1] [id, a, 1] [equal, =, 1] [intnum, 5, 1] ", "");
        tester("!abc", "", "[invalidid, !abc, 1] ");
        tester("+abc", "[plus, +, 1] [id, abc, 1] ", "");
        tester("/abc", "[div, /, 1] [id, abc, 1] ", "");
        tester(";5", "[semi, ;, 1] [intnum, 5, 1] ", "");
        tester("=5", "[equal, =, 1] [intnum, 5, 1] ", "");
        tester("+ 5", "[plus, +, 1] [intnum, 5, 1] ", "");
        tester("int a=5;", "[id, int, 1] [id, a, 1] [equal, =, 1] [intnum, 5, 1] [semi, ;, 1] ", "");
        tester("int a = 5 ;", "[id, int, 1] [id, a, 1] [equal, =, 1] [intnum, 5, 1] [semi, ;, 1] ", "");
        tester("+ - / * ", "[plus, +, 1] [minus, -, 1] [div, /, 1] [mult, *, 1] ", "");
        tester("class gagan {}", "[class, class, 1] [id, gagan, 1] [lcurbr, {, 1] [rcurbr, }, 1] ", "");
        tester("5+5", "[intnum, 5, 1] [plus, +, 1] [intnum, 5, 1] ", "");
        tester("++", "[plus, +, 1] [plus, +, 1] ", "");
        tester("=++", "[equal, =, 1] [plus, +, 1] [plus, +, 1] ", "");
        tester("= ++", "[equal, =, 1] [plus, +, 1] [plus, +, 1] ", "");
        tester("!++", "[plus, +, 1] [plus, +, 1] ", "[invalidid, !, 1] ");
        tester("a/++5", "[id, a, 1] [div, /, 1] [plus, +, 1] [plus, +, 1] [intnum, 5, 1] ", "");
        tester("/++", "[div, /, 1] [plus, +, 1] [plus, +, 1] ", "");
        tester(" / + + ", "[div, /, 1] [plus, +, 1] [plus, +, 1] ", "");
        tester("+/+", "[plus, +, 1] [div, /, 1] [plus, +, 1] ", "");
        tester("++/", "[plus, +, 1] [plus, +, 1] [div, /, 1] ", "");
        tester("a=++5", "[id, a, 1] [equal, =, 1] [plus, +, 1] [plus, +, 1] [intnum, 5, 1] ", "");
        tester("+/!*-", "[plus, +, 1] [div, /, 1] [mult, *, 1] [minus, -, 1] ", "[invalidid, !, 1] ");
        tester("while (i < n-1)", "[while, while, 1] [lpar, (, 1] [id, i, 1] [lessthan, <, 1] [id, n, 1] [minus, -, 1] [intnum, 1, 1] [rpar, ), 1] ",
                "");
        tester("new_function.a = A;", "[id, new_function, 1] [dot, ., 1] [id, a, 1] [equal, =, 1]" +
                " [id, A, 1] [semi, ;, 1] ", "");
        tester("0.0", "[floatnum, 0.0, 1] ", "");
        tester("==", "[eq, ==, 1] ", "");
        //The case below gives an error; need to fix.

        deleteFile();

    }

    @Test
    private static void tester(String input, String token, String error) {
        String expectedToken = "";
        String expectedError = "";
        BufferedReader br = new BufferedReader(new StringReader(input));
        lex lex = new lex("src/lexer/testLex.src");
        lex.nextToken(br);
        try {
            expectedToken = new String(Files.readAllBytes(Paths.get("src/lexer/testLex.outlextokens")));
            expectedError = new String(Files.readAllBytes(Paths.get("src/lexer/testLex.outlexerrors")));
            Assert.assertTrue(expectedToken.equals(token));
            Assert.assertTrue(expectedError.equals(error));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (AssertionError a) {
            System.out.println("input --> "+input+"\ninput token-->"+token+"\nexpected token -->"+expectedToken
                    +"\ninput error"+error+"\nexpected error"+expectedError);
        }
    }

    private static void deleteFile() {
        File outlexorsToken = new File("src/lexer/testLex.outlextokens");
        File outlexorsError = new File("src/lexer/testLex.outlexerrors");
        if (outlexorsToken.exists()) {
            outlexorsToken.delete();
        } else {
            System.err.println(
                    "I cannot find '" + outlexorsToken + "' ('" + outlexorsToken.getAbsolutePath() + "')");
        }
        if (outlexorsError.exists()) {
            outlexorsError.delete();
        } else {
            System.err.println(
                    "I cannot find '" + outlexorsError + "' ('" + outlexorsError.getAbsolutePath() + "')");
        }
    }
}