package com.gagan_442_a1;

import java.io.*;
import java.util.regex.Pattern;

public class lexer {
    private int lineNum;
    private int currentIntChar;
    private StringBuilder lexeme;
    private PrintWriter pw;
    private boolean isNum;


    /**
     * constructor for lexer class, initializes buffered reader
     */
    public lexer() {
        this.lineNum = 1;
        this.currentIntChar = 0;
        this.lexeme = new StringBuilder(50);
        this.isNum = false;
        try {
            this.pw = new PrintWriter("src/test/outlexors", "UTF-8");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * constructor for initializing the printwriter for a specific file
     * @param testOutputLocation: filepath of the file where the outlexors will be printed.
     */
    public lexer(String testOutputLocation){
        this.lineNum = 1;
        this.currentIntChar = 0;
        this.lexeme = new StringBuilder(50);
        this.isNum = false;
        try {
            this.pw = new PrintWriter(testOutputLocation, "UTF-8");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * Read the buffered reader character by character and calls the identifyToken() method
     * which takes the integer values of that character. It checks if the character against
     * regular expressions to categorize it into one of the bags: identifier, number, specialChar, other.
     * Once the character is identified it calls corresponding methods.
     * MIGHT return toekn objects
     * @param br: this is the buffered reader object passed to the method to return tokens/write to buffer writer
     */
    public void nextToken(BufferedReader br){
        try{
            while(currentIntChar!=(-1)) {
                currentIntChar = br.read();
//                System.out.println(currentIntChar);
                identifyToken(currentIntChar, br);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Converts the integer value of the currentChar to String and uses Pattern class to match it
     * against the regular expressions.
     * MIGHT return the token object
     * @param br: buffered reader object containing the source code
     * @param currentIntChar: integer value of the character read.
     */
    private void identifyToken(int currentIntChar, BufferedReader br){
        String currentStringChar = Character.toString((char)currentIntChar);

        if (currentIntChar==(-1)){
            checkTokenSpecialChar(currentStringChar, br);
        }

        if (Pattern.matches("-?[0-9]+", lexeme)){
            isNum = true;
        }

        //regEx to match an identifier;; keywords are detected later
        if(Pattern.matches("\\b([a-zA-Z][0-9a-zA-Z_]{0,31})\\b", currentStringChar) && !isNum){
            lexeme.append(currentStringChar);
        }
        //regEx to match numbers; float and integer are classified later
        else if (Pattern.matches("-?[0-9]*[.]?[0-9]*[e]?[+|-]?[0-9]*", currentStringChar) && isNum){
                lexeme.append(currentStringChar);
        }
        //this checks for any character that are not in the language
        else if (!(Pattern.matches("==|<>|<|>|<=|>=|\\+|-|\\*|/|=|\\(|\\)|\\{|\\}|\\[|\\]|;|,|\\.|:|::|\\n|\\r|\\t|\\s" , currentStringChar))){ //first symbol is not part of the language symbols
            lexeme.append(currentStringChar);
        }
        //all the other special characters of the language
        else{
            checkTokenSpecialChar(currentStringChar, br);
        }
    }

    /**
     * This method checks for all the special characters of the language and creates token based
     * the lexical specifications of the language
     * MAYBE return object
     * @param  br buffered reader object containing the source code
     * @param currentStringChar value of current character as a String object
     */
    private void checkTokenSpecialChar(String currentStringChar, BufferedReader br) {

        if (currentIntChar == 47) {
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (currentIntChar == 47) {
                lexeme.append(Character.toString((char) currentIntChar));
                inLineComntToken(br);
            } else if (currentIntChar == 42) {
                lexeme.append(Character.toString((char) currentIntChar));
                blockCmntToken(br);
            } else {
//                token t = new token("divide", lexeme, lineNum);
                createToken("divide", lexeme, lineNum);
//                lexeme = null;
            }
//            createToken(lexeme);
        } else if (currentIntChar == 61) {
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (currentIntChar == 61) {
                lexeme.append(Character.toString((char) currentIntChar));
//                token t = new token("equalequal", lexeme, lineNum);
                createToken("equalequal", lexeme, lineNum);
//                lexeme = null;
            } else {
//                token t = new token("equal", lexeme, lineNum);
                createToken("equal", lexeme, lineNum);
//                lexeme = null;
            }
        } else if (currentIntChar == 60) {
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (currentIntChar == 61) {
                lexeme.append(Character.toString((char) currentIntChar));
//                token t = new token("lessthanequal", lexeme, lineNum);
                createToken("lessthanequal", lexeme, lineNum);
//                lexeme = null;
            } else if (currentIntChar == 62) {
                lexeme.append(Character.toString((char) currentIntChar));
//                token t = new token("notequal", lexeme, lineNum);
                createToken("notequal", lexeme, lineNum);
//                lexeme = null;
            } else {
//                token t = new token("lessthan", lexeme, lineNum);
                createToken("lessthan", lexeme, lineNum);
//                lexeme = null;
            }
        } else if (currentIntChar == 62) {
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (currentIntChar == 61) {
                lexeme.append(Character.toString((char) currentIntChar));
//                token t = new token("greaterthanequal", lexeme, lineNum);
                createToken("greaterthanequal", lexeme, lineNum);
//                lexeme = null;
            } else {
//                token t = new token("greaterthan", lexeme, lineNum);
                createToken("greaterthan", lexeme, lineNum);
//                lexeme = null;
            }
        } else if (currentIntChar == 58) {
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (currentIntChar == 58) {
                lexeme.append(Character.toString((char) currentIntChar));
//                token t = new token("coloncolon", lexeme, lineNum);
                createToken("coloncolon", lexeme, lineNum);
//                lexeme = null;
            } else {
//                token t = new token("colon", lexeme, lineNum);
                createToken("colon", lexeme, lineNum);
//                lexeme = null;
            }
        } else if (currentIntChar == 46){
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (currentIntChar == 48) {
                lexeme.append(Character.toString((char) currentIntChar));
                createToken("float", lexeme, lineNum);
            }else {
                createToken("period", lexeme, lineNum);
            }
        }
        else if (currentIntChar == 43) {
            lexeme.append(currentStringChar);
            createToken("plus", lexeme, lineNum);
        } else if (currentIntChar == 45) {
            lexeme.append(currentStringChar);
            createToken("minus", lexeme, lineNum);
        } else if (currentIntChar == 42) {
            lexeme.append(currentStringChar);
            createToken("multiply", lexeme, lineNum);
        } else if (currentIntChar == 40) {
            lexeme.append(currentStringChar);
            createToken("openround", lexeme, lineNum);
        } else if (currentIntChar == 41) {
            lexeme.append(currentStringChar);
            createToken("closeround", lexeme, lineNum);
        } else if (currentIntChar == 123) {
            lexeme.append(currentStringChar);
            createToken("opencurly", lexeme, lineNum);
        } else if (currentIntChar == 125) {
            lexeme.append(currentStringChar);
            createToken("closecurly", lexeme, lineNum);
        } else if (currentIntChar == 91) {
            lexeme.append(currentStringChar);
            createToken("opensquare", lexeme, lineNum);
        } else if (currentIntChar == 93) {
            lexeme.append(currentStringChar);
            createToken("closesquare", lexeme, lineNum);
        } else if (currentIntChar == 59) {
            lexeme.append(currentStringChar);
            createToken("semicolon", lexeme, lineNum);
        } else if (currentIntChar == 44) {
            lexeme.append(currentStringChar);
            createToken("comma", lexeme, lineNum);
        } else if (currentIntChar == 32) {
            checkTokenKeywords();
        } else if (currentIntChar == 9) {
            checkTokenKeywords();
        }
        else if (currentIntChar == 13){
            checkTokenKeywords();
        }
        else if (currentIntChar == 10){
            checkTokenKeywords();
            lineNum++;
        }
        else if (currentIntChar==(-1)){
            checkTokenKeywords();
        }
        else {
            lexeme.append(currentStringChar);
            createToken("invalidchar", lexeme, lineNum);
        }
    }

    /**
     * This method is called after all checks have been made for special character.
     * This is for matching all the identifiers, keywords, integers and floats.
     * Positive and negative integer are supported. Any number starting with 0 is either
     * invalid integer or invalid float.
     *MAYBE retuen token object
     */
    private void checkTokenKeywords(){
        if (lexeme.toString().equals("")){
            createToken("space/tab", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bif\\b", lexeme)){
            createToken("if", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bthen\\b", lexeme)){
            createToken("then", lexeme, lineNum);
        }
        else if (Pattern.matches("\\belse\\b", lexeme)){
            createToken("else", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bwhile\\b", lexeme)){
            createToken("while", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bclass\\b", lexeme)){
            createToken("class", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bdo\\b", lexeme)){
            createToken("do", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bend\\b", lexeme)){
            createToken("end", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bpublic\\b", lexeme)){
            createToken("public", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bprivate\\b", lexeme)){
            createToken("private", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bor\\b", lexeme)){
            createToken("or", lexeme, lineNum);
        }
        else if (Pattern.matches("\\band\\b", lexeme)){
            createToken("and", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bnot\\b", lexeme)){
            createToken("not", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bread\\b", lexeme)){
            createToken("read", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bwrite\\b", lexeme)){
            createToken("write", lexeme, lineNum);
        }
        else if (Pattern.matches("\\breturn\\b", lexeme)){
            createToken("return", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bmain\\b", lexeme)){
            createToken("main", lexeme, lineNum);
        }
        else if (Pattern.matches("\\binherits\\b", lexeme)){
            createToken("inherits", lexeme, lineNum);
        }
        else if (Pattern.matches("\\blocal\\b", lexeme)){
            createToken("local", lexeme, lineNum);
        }
        else if (Pattern.matches("\\binteger\\b", lexeme)){
            createToken("integer", lexeme, lineNum);
        }
        else if (Pattern.matches("\\bfloat\\b", lexeme)){
            createToken("float", lexeme, lineNum);
        }
        else if (Pattern.matches("[.]?[0]", lexeme)){
            createToken("intnum", lexeme, lineNum);
        }
        else if (Pattern.matches("-?[1-9][0-9]{0,15}", lexeme)){
            createToken("intnum", lexeme, lineNum);
        }
        else if (Pattern.matches("-?[0][0-9]{0,15}", lexeme)){
            createToken("invalidinteger", lexeme, lineNum);
        }
        else if (Pattern.matches("-?[1-9][0-9]{0,10}[.]?[0-9]{0,5}[1-9]e?[+|-]?[1-9]{0,10}", lexeme)){
            createToken("float", lexeme, lineNum);
        }
        else if (Pattern.matches("-?[0-9]{0,10}[.]?[0-9]{0,5}[e]?[+|-]?[0-9]{0,10}", lexeme)){
            createToken("invalidfloat", lexeme, lineNum);
        }
        else if (Pattern.matches("\\b([a-zA-Z][0-9a-zA-Z_]{0,19})\\b", lexeme)){
            createToken("id", lexeme, lineNum);
        }
        else {
            //invalid token
            createToken("invalidid", lexeme, lineNum);
        }
    }

    /**
     * Special case to handle comments; reads everything once comment found
     * @param br: buffered reader object to read everything inside the comment
     */
    private void inLineComntToken(BufferedReader br){
        do{
            try {
                currentIntChar = br.read();
                if (currentIntChar==-1) break;
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            if (currentIntChar!=10){
                lexeme.append(Character.toString((char)currentIntChar));
            }

        }while( (currentIntChar!=10));
        createToken("inlinecmnt", lexeme, lineNum);
//        lexeme = null;
    }

    /**
     * Special case to handle comments; reads everything including the "\n" symbol once comment is found
     * @param br: buffered reader object to read everything inside the comment
     */
    private void blockCmntToken(BufferedReader br){
        do{
            try {
                currentIntChar = br.read();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            if (currentIntChar!=10 && currentIntChar!=13){
                lexeme.append(Character.toString((char)currentIntChar));
            }
            else{
                lexeme.append("\\n");
            }
            if (currentIntChar == 42){
                try {
                    currentIntChar = br.read();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
                if (currentIntChar!=10  || currentIntChar!=13){
                    lexeme.append(Character.toString((char)currentIntChar));
                }
                else{
                    lexeme.append("\\n");
                }
                if (currentIntChar == 47){
                    createToken("blockcmnt", lexeme, lineNum);
                    break;
                }
                try {
                    currentIntChar = br.read();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
                if (currentIntChar!=10){
                    lexeme.append(Character.toString((char)currentIntChar));
                }
                else{
                    lexeme.append("\\n");
                }
            }
        }while(true);
    }

    /**
     * This method the token as a parameter and creates a token. The lexeme is a global variable of the
     * class which identifies the type of token.
     * MAYBE return token object
     * @param token name used to create the token
     */
    private void createToken(String token, StringBuilder lexeme, int lineNum){
        token t = new token(token, lexeme, lineNum);
        if (!token.equals("space/tab")){
//            System.out.println(t);
            pw.print(t);
            pw.flush();
        }
        lexeme.replace(0, lexeme.length(), "");
    }

    public void backtrack(BufferedReader br){
        try {
            br.skip(2*(-1));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
