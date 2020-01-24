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
            BufferedReader br = new BufferedReader(new FileReader("src/com/gagan_442_a1/lexpositivegrading_1.src"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null){
//                System.out.println(line);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            sb.append("%"); //to mark the end of my string builder
            token t = new token();
            lexicalAnalyzer lex = new lexicalAnalyzer(sb);
            do{
                t = lex.nextToken();
                if((t.getToken().equals("tabspace")||(t.getToken().equals("space")))){
                    System.out.print(" ");
                }else if (t.getToken().equals("newline")){
                    System.out.println();
                }else{
                    System.out.print(t);
                }
            }while(!(t.getToken().equals("END")));

        }catch (IOException x){
            System.out.println(x.getMessage());
        }

    }

}

class lexicalAnalyzer {
    private StringBuilder sourceCode;
    private String currentChar;
    private String nextChar;

    private int position = 0;
    private int linenum = 1;
    private final String lineSep = System.getProperty("line.separator");

    //constructor of lexicalAnalyzer
    public lexicalAnalyzer (StringBuilder sourceCode){
        this.sourceCode = sourceCode;
        this.currentChar = "";
    }

    public token nextToken() {
        StringBuilder currentString = new StringBuilder();
        token t = null;
        currentChar = Character.toString(sourceCode.charAt(position));
        if (currentChar.equals("\n")){
            linenum++;
//            position++;
        }
        if (Pattern.matches("\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,31})\\b", currentChar)) {
            currentString = currentString.append(currentChar);
            position++;
            t = maybeId(currentString);
        }
        else if (Pattern.matches("\\b([0-9])\\b", currentChar)){
            currentString = currentString.append(currentChar);
            position++;
            t = maybeNum(currentString);
        }
        else{
            currentString = currentString.append(currentChar);
            t = specialChar(currentString);
            position++;
        }
        return t;
    }


    public String lookAhead(){
        int currentPos = position+1;
        nextChar = Character.toString(sourceCode.charAt(currentPos));
        return nextChar;
        //this met
        // od is used in case of ambiguity. use idea of backtracking
    }


    public token maybeNum(StringBuilder currentString) {
        token t = null;
        String firstChar = currentChar;
        currentChar = Character.toString(sourceCode.charAt(position));
        while (Pattern.matches(currentChar, "\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,19})\\b.*")) {
            currentString.append(currentChar);
            position++;
            //creates a token based on the integer regex otherwise throw error
        }
        if (firstChar == "0"){
            t = new token("invalid", currentString,linenum);
        }
        return t;
    }

    public token maybeId(StringBuilder currentString){
        token t = null;
        currentChar = Character.toString(sourceCode.charAt(position));
        while(Pattern.matches("\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,19})\\b.*", currentChar)){
            currentString.append(currentChar);
            position++;
        }
//        isIDorKD(currentString); // a method that checks if the identifier is a keyword or not
        return t;
    }

    public token specialChar(StringBuilder currentString){

        switch (currentChar){
            case "+" : return (new token( "plus", currentString, linenum));
            case "-" : return (new token( "minus", currentString, linenum));
            case "*" : return (new token( "multiply", currentString, linenum));
            case "/" : return (new token( "divide", currentString, linenum));
            case "(" : return (new token( "openround", currentString, linenum));
            case ")" : return (new token( "closeround", currentString, linenum));
            case "{" : return (new token( "opencurly", currentString, linenum));
            case "}" : return (new token( "closecurly", currentString, linenum));
            case "[" : return (new token( "opensquare", currentString, linenum));
            case "]" : return (new token( "closesquare", currentString, linenum));
            case ";" : return (new token( "semicolon", currentString, linenum));
            case "," : return (new token( "comma", currentString, linenum));
            case "." : return (new token( "period", currentString, linenum));
            case "%" : return (new token( "END", currentString, linenum));
            case " " : return (new token( "space", currentString, linenum));
            case "\n" : return (new token( "newline", currentString, linenum));
            case "\t" : return (new token( "tabspace", currentString, linenum));
            case ":" : nextChar = lookAhead();
                        if (nextChar==":") {
                            currentString = currentString.append(nextChar); position++;
                            return (new token( "coloncolon", currentString, linenum)); }
                        else {return (new token( "colon", currentString, linenum));}
            case "=" : nextChar = lookAhead();
                        if (nextChar.equals("=")){
                            currentString = currentString.append(nextChar); position++;
                            return (new token( "equalequal", currentString, linenum)); }
                        else {return (new token( "equal", currentString, linenum));}
            case ">" : nextChar = lookAhead();
                        if (nextChar=="=") {
                            currentString = currentString.append(nextChar); position++;
                            return (new token( "greaterthanequal", currentString, linenum)); }
                        else {return (new token( "greaterthan", currentString, linenum));}
            case "<" : nextChar = lookAhead();
                        if (nextChar=="=") {
                            currentString = currentString.append(nextChar); position++;
                            return (new token( "lessthanequal", currentString, linenum)); }
                        else if (nextChar==">") {
                            currentString = currentString.append(nextChar); position++;
                            return (new token( "notequal", currentString, linenum)); }
                        else {return (new token( "lessthan", currentString, linenum));}
            default : return (new token("invalidchar", currentString, linenum));
        }
    }

    public enum tokenType{
        Identifier,
        Integer,
        Fraction,
        Flot,
        Plus,
        Minus,
        Multiply,
        Divide,
        Equal,
        Openround,
        Closeround,
        Opencurly,
        Closecurly,
        Opensquare,
        Closesquare,
        Semicolon,
        Comma,
        Period,
        Colon,
        Doublecolon,
        Equalequal,
        Notequal,
        Lessthan,
        Greaterthan,
        Lessthanequal,
        Greaterthanequal,
        If,
        Then,
        Else,
        While,
        Class,
        Do,
        End,
        Public,
        Private,
        Or,
        And,
        Read,
        Write,
        Return,
        Main,
        Inherit,
        Local,
        Blockcmnt,
        Inlinecmnt;
    }

    //a map of stings and pattern; to store the regular expression with their token
    private Hashtable<tokenType, String> regEx = new Hashtable<>();

    public void setRegEx() {
        regEx.put(tokenType.Plus, "(\\+{1})");

    }
}


class token {
    private String token;
    private StringBuilder lexeme;
    private int linenum;

    public token(){
        this.token = "";
        this.lexeme = null;
        this.linenum = -1;
    }

    public token(String token, StringBuilder lexeme, int linenum){
        this.token = token;
        this.lexeme = lexeme;
        this.linenum = linenum;
    }

    @Override
    public String toString() {
        return "[" + token + ", " + lexeme + ", " + linenum + "]";
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLexeme(StringBuilder lexeme) {
        this.lexeme = lexeme;
    }

    public void setLinenum(int linenum) {
        this.linenum = linenum;
    }

    public String getToken() {
        return token;
    }

    public StringBuilder getLexeme() {
        return lexeme;
    }

    public int getLinenum() {
        return linenum;
    }
}