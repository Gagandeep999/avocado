package test;

import com.gagan_442_a1.lexer;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.*;

public class lexicalAnalyzerTest {

    public static void main(String args[]) {
        testInt("123");
        testId("abc");

        deleteFile();

    }

    @Test
    public static void testInt(String input) {
        final String actual = "[intnum, 123, 1]";
        BufferedReader br = new BufferedReader(new StringReader(input));
        lexer lex = new lexer("src/test/outlexors");
        lex.nextToken(br);
        test(actual);
    }

    @Test
    public static void testId(String input) {
        final String actual = "[id, abc, 1]";
        BufferedReader br = new BufferedReader(new StringReader(input));
        lexer lex = new lexer("src/test/outlexors");
        lex.nextToken(br);
        test(actual);
    }

    public static void test(String actual) {
        try {
            String expected = new String(Files.readAllBytes(Paths.get("src/test/outlexors")));
            Assert.assertTrue(expected.equals(actual));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (AssertionError a) {
            System.out.println("message: " + a.getMessage());
        }
    }

    public static void deleteFile() {
        File outlexors = new File("src/test/outlexors");
        if (outlexors.exists()) {
            outlexors.delete();
        } else {
            System.err.println(
                    "I cannot find '" + outlexors + "' ('" + outlexors.getAbsolutePath() + "')");
        }
    }
}