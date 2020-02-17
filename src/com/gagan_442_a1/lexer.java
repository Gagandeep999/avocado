package com.gagan_442_a1;

import java.io.*;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class lexer {
    private int lineNum;
    private int currentIntChar;
    private StringBuilder lexeme;
    private PrintWriter pwTokens;
    private PrintWriter pwErrors;
    private boolean isNum;
    private LinkedList<token> tokenList;

    /**
     * constructor for initializing the printwriter for a specific file
     * @param outputLocation: filepath of the file where the outlexors will be printed.
     */
    public lexer(String outputLocation){
        this.lineNum = 1;
        this.currentIntChar = 0;
        this.lexeme = new StringBuilder(50);
        this.isNum = false;
        this.tokenList = new LinkedList<>();
        String outlextokens = outputLocation.replace(".src", ".outlextokens");
        String outlexerrors = outputLocation.replace(".src", ".outlexerrors");
        try {
            this.pwTokens = new PrintWriter(outlextokens, "UTF-8");
            this.pwErrors = new PrintWriter(outlexerrors, "UTF-8");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @return a linked list of tokens
     */
    public LinkedList<token> getTokenList() {
        return tokenList;
    }

    /**
     * Read the buffered reader character by character and calls the identifyToken() method
     * which takes the integer values of that character. It checks if the character against
     * regular expressions to categorize it into one of the bags: identifier, number, specialChar, other.
     * Once the character is identified it calls corresponding methods.
     * @param br: this is the buffered reader object passed to the method to return tokens/write to buffer writer
     */
    public void nextToken(BufferedReader br){
        try{
            while(currentIntChar!=(-1)) {
                currentIntChar = br.read();
                identifyToken(currentIntChar, br);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Converts the integer value of the currentChar to String and uses Pattern class to match it
     * against the regular expressions.
     * @param br: buffered reader object containing the source code
     * @param currentIntChar: integer value of the character read.
     */
    private void identifyToken(int currentIntChar, BufferedReader br){
        String currentStringChar = Character.toString((char)currentIntChar);

        if (currentIntChar==(-1)){
            checkTokenSpecialChar(currentStringChar, br);
        }

        if (Pattern.matches("-?[0-9]+[.]?[0-9]*", lexeme) || Pattern.matches("[0-9]", currentStringChar)){
            isNum = true;
        }

        if ((Pattern.matches("[+|-]", currentStringChar)) && Pattern.matches("-?[0-9]*[.][0-9]*[e]", lexeme))
            lexeme.append(currentStringChar);
        else if ((currentStringChar.equals("e")) && (Pattern.matches("-?[0-9]*[.][0-9]*", lexeme)))
            lexeme.append(currentStringChar);
            //regEx to match an identifier;; keywords are detected later
        else if(Pattern.matches("\\b([a-zA-Z][0-9a-zA-Z_]{0,31})\\b", currentStringChar) && !isNum){
            lexeme.append(currentStringChar);
            isNum = false;
        }
        //regEx to match numbers; float and integer are classified later
        else if (Pattern.matches("-?[0-9]*[.]?[0-9]*", currentStringChar) && isNum){ //[e]?[+|-]?[0-9]*
            lexeme.append(currentStringChar);
            isNum = false;
        }
        //this checks for any character that are not in the language
        else if (!(Pattern.matches("==|<>|<|>|<=|>=|\\+|-|\\*|/|=|\\(|\\)|\\{|\\}|\\[|\\]|;|,|\\.|:|::|\\n|\\r|\\t|\\s|" , currentStringChar))){
            lexeme.append(currentStringChar);
            isNum = false;
        }
        //all the other special characters of the language
        else{
            checkTokenSpecialChar(currentStringChar, br);
            isNum = false;
        }
    }

    /**
     * This method checks for all the special characters of the language and creates token based
     * the lexical specifications of the language
     * @param  br buffered reader object containing the source code
     * @param currentStringChar value of current character as a String object
     */
    private void checkTokenSpecialChar(String currentStringChar, BufferedReader br) {

        if (currentStringChar.equals("/")) { //divide symbol
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            String newStringChar = Character.toString((char)currentIntChar);

            if (newStringChar.equals("/")) { //divide symbol
                lexeme.append(Character.toString((char) currentIntChar));
                inLineComntToken(br);
            } else if (newStringChar.equals("*")) {
                lexeme.append(Character.toString((char) currentIntChar));
                blockCmntToken(br);
            } else {
                createToken("div", lexeme, lineNum);

                if (Pattern.matches("[a-zA-z0-9_]", newStringChar)){
                    lexeme.append(newStringChar);
                }
                else{
                    checkTokenSpecialChar(newStringChar, br);
                }
            }
        } else if (currentStringChar.equals("=")) { //equal than symbol
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            String newStringChar = Character.toString((char)currentIntChar);

            if (newStringChar.equals("=")) {
                lexeme.append(Character.toString((char) currentIntChar));
                createToken("eq", lexeme, lineNum);
            } else {
                createToken("equal", lexeme, lineNum);

                if (Pattern.matches("[a-zA-z0-9_]", newStringChar)){
                    lexeme.append(newStringChar);
                }
                else{
                    checkTokenSpecialChar(newStringChar, br);
                }
            }
        } else if (currentStringChar.equals("<")) { //less than symbol
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            String newStringChar = Character.toString((char)currentIntChar);

            if (newStringChar.equals("=")) {
                lexeme.append(Character.toString((char) currentIntChar));
                createToken("leq", lexeme, lineNum);
            } else if (newStringChar.equals(">")) {
                lexeme.append(Character.toString((char) currentIntChar));
                createToken("neq", lexeme, lineNum);
            } else {
                createToken("lessthan", lexeme, lineNum);

                if (Pattern.matches("[a-zA-z0-9_]", newStringChar)){
                    lexeme.append(newStringChar);
                }
                else{
                    checkTokenSpecialChar(newStringChar, br);
                }
            }
        } else if (currentStringChar.equals(">")) { // greater than symbol
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            String newStringChar = Character.toString((char)currentIntChar);

            if (newStringChar.equals("=")) {
                lexeme.append(Character.toString((char) currentIntChar));
                createToken("geq", lexeme, lineNum);
            } else {
                createToken("gt", lexeme, lineNum);

                if (Pattern.matches("[a-zA-z0-9_]", newStringChar)){
                    lexeme.append(newStringChar);
                }
                else{
                    checkTokenSpecialChar(newStringChar, br);
                }
            }
        } else if (currentStringChar.equals(":")) { // colon symbol
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            String newStringChar = Character.toString((char)currentIntChar);

            if (newStringChar.equals(":")) {
                lexeme.append(Character.toString((char) currentIntChar));
                createToken("sr", lexeme, lineNum);
            } else {
                createToken("colon", lexeme, lineNum);

                if (Pattern.matches("[a-zA-z0-9_]", newStringChar)){
                    lexeme.append(newStringChar);
                }
                else{
                    checkTokenSpecialChar(newStringChar, br);
                }
            }
        } else if (currentIntChar == 46){ // Period, dot or full stop
            if ( !lexeme.toString().equals("") && !isNum){
                checkTokenKeywords();
                lexeme.append(currentStringChar);
                createToken("period", lexeme, lineNum);
            }else{
                lexeme.append(currentStringChar);
                createToken("period", lexeme, lineNum);
            }
        }
        else if (currentStringChar.equals("+")) {
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("plus", lexeme, lineNum);
//            TODO: to include negative numbers check the next character after encountering minus
        } else if (currentIntChar == 45) { // Hyphen
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("minus", lexeme, lineNum);
        } else if (currentIntChar == 42) { // Asterisk
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("mult", lexeme, lineNum);
        } else if (currentIntChar == 40) { // Open parenthesis
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("lpar", lexeme, lineNum);
        } else if (currentIntChar == 41) { // Close parenthesis
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("rpar", lexeme, lineNum);
        } else if (currentIntChar == 123) { // Opening brace
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("lcurbr", lexeme, lineNum);
        } else if (currentIntChar == 125) { // Closing brace
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("rcurbr", lexeme, lineNum);
        } else if (currentIntChar == 91) { // Opening bracket
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("lsqbr", lexeme, lineNum);
        } else if (currentIntChar == 93) { // Closing bracket
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("rsqbr", lexeme, lineNum);
        } else if (currentIntChar == 59) { // Semicolon
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("semi", lexeme, lineNum);
        } else if (currentIntChar == 44) { // Comma
            checkTokenKeywords();
            lexeme.append(currentStringChar);
            createToken("comma", lexeme, lineNum);
        } else if (currentIntChar == 32) { // Space
            checkTokenKeywords();
        } else if (currentIntChar == 9) { // Horizontal Tab
            checkTokenKeywords();
        }else if (currentIntChar == 13){ // Carriage Return
            checkTokenKeywords();
        }else if (currentIntChar == 10){ // Line Feed
            checkTokenKeywords();
        }else if (currentIntChar==(-1)){ // EOF
            checkTokenKeywords();
        }else {
            lexeme.append(currentStringChar);
            createToken("invalidid", lexeme, lineNum);
        }
    }

    /**
     * This method is called after all checks have been made for special character.
     * This is for matching all the identifiers, keywords, integers and floats.
     * Positive and negative integer are supported. Any number starting with 0 is either
     * invalid integer or invalid float.
     */
    private void checkTokenKeywords(){
        if (lexeme.toString().equals("") || lexeme.toString().equals(" ")){
            createToken("space/tab/newline", lexeme, lineNum);
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
        else if (Pattern.matches("\\bvoid\\b", lexeme)){
            createToken("void", lexeme, lineNum);
        }
        else if (Pattern.matches("[.]?[0]", lexeme)){
            createToken("intnum", lexeme, lineNum);
        }
        else if (Pattern.matches("-?[1-9][0-9]{0,15}", lexeme)){
            createToken("intnum", lexeme, lineNum);
            isNum = false;
        }
        else if (Pattern.matches("-?[0][0-9]{0,15}", lexeme)){
            createToken("invalidinteger", lexeme, lineNum);
            isNum = false;
        }else if (Pattern.matches("[1-9]*[.][0]", lexeme)){
            createToken("floatnum", lexeme, lineNum);
            isNum = false;
        }else if (Pattern.matches("-?[1-9][0-9]{0,10}[.]?[0-9]{0,5}[1-9]e?[+|-]?[1-9]{0,10}", lexeme)){
            createToken("floatnum", lexeme, lineNum);
            isNum = false;
        }
        else if (Pattern.matches("-?[0-9]{0,10}[.]?[0-9]{0,5}[e]?[+|-]?[0-9]{0,10}", lexeme)){
            createToken("invalidfloat", lexeme, lineNum);
        }
        else if (Pattern.matches("\\b([a-zA-Z][0-9a-zA-Z_]{0,19})\\b", lexeme)){
            createToken("id", lexeme, lineNum);
        }
        else {
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
        StringBuilder lex = new StringBuilder(lexeme);
        token t = new token(token, lex, lineNum);

        if (!token.contains("space/tab/newline")){
            if(token.contains("invalid")){
                pwErrors.print(t+" ");
                pwErrors.flush();
            }
            else{
                pwTokens.print(t+" ");
                pwTokens.flush();
                this.tokenList.add(t);
            }
        }
        if(currentIntChar==10){
            this.lineNum++;
            if(token.contains("invalid")){
                pwErrors.print("\n");
                pwErrors.flush();
            }
            else{
                pwTokens.print("\n");
                pwTokens.flush();
            }
        }
        lexeme.replace(0, lexeme.length(), "");
    }
}
