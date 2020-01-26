package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class lexer {
    String lineSep = System.lineSeparator();
    int lineNum = 1;
    int currentIntChar = 0;
    BufferedReader br = null;
    StringBuilder lexeme = new StringBuilder(50);

    public lexer(BufferedReader br){
        this.br = br;
    }

    public void nextToken(){
        try{
            do {
                currentIntChar = br.read();
//                System.out.println(currentIntChar);
                identifyToken(currentIntChar);
            } while(currentIntChar!=(-1));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
//        return (new token("token", lexeme, lineNum));
    }

    public void identifyToken(int currentIntChar){
        String currentStringChar = Character.toString((char)currentIntChar);
//        if (currentStringChar.equals("\n")){
//            lineNum++;
//        }
        if(Pattern.matches("\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,31})\\b", currentStringChar)){
//            currentIntChar = br.read();
//            if (currentIntChar==32){ //this means a space is encountered
//                isKeyword(lexeme);
//            }
//            currentStringChar = Character.toString((char)currentIntChar);
            lexeme.append(currentStringChar);
        }
        else if (Pattern.matches("-?[0-9]*[.]?[0-9]*[e]?[+|-]?[0-9]*", currentStringChar)){
//            currentIntChar = br.read();
//            if (currentIntChar==32){ //this means a space is encountered
//                isNumber(lexeme);
//            }
//            currentStringChar = Character.toString((char)currentIntChar);
            lexeme.append(currentStringChar);
        }
        else{
//            lexeme.append(currentStringChar);
            checkTokenSpecialChar(currentStringChar);
        }
    }

    public void checkTokenSpecialChar(String currentStringChar) {

        if (currentIntChar == 47) {
            lexeme.append(currentStringChar);
            try {
                currentIntChar = br.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (currentIntChar == 47) {
                lexeme.append(Character.toString((char) currentIntChar));
                inLineComntToken();
            } else if (currentIntChar == 42) {
                lexeme.append(Character.toString((char) currentIntChar));
                blockCmntToken();
            } else {
//                token t = new token("divide", lexeme, lineNum);
                createToken("divide");
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
                createToken("equalequal");
//                lexeme = null;
            } else {
//                token t = new token("equal", lexeme, lineNum);
                createToken("equal");
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
                createToken("lessthanequal");
//                lexeme = null;
            } else if (currentIntChar == 62) {
                lexeme.append(Character.toString((char) currentIntChar));
//                token t = new token("notequal", lexeme, lineNum);
                createToken("notequal");
//                lexeme = null;
            } else {
//                token t = new token("lessthan", lexeme, lineNum);
                createToken("lessthan");
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
                createToken("greaterthanequal");
//                lexeme = null;
            } else {
//                token t = new token("greaterthan", lexeme, lineNum);
                createToken("greaterthan");
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
                createToken("coloncolon");
//                lexeme = null;
            } else {
//                token t = new token("colon", lexeme, lineNum);
                createToken("colon");
//                lexeme = null;
            }
        } else if (currentIntChar == 43) {
            lexeme.append(currentStringChar);
//            token t = new token("plus", lexeme, lineNum);
            createToken("plus");
        } else if (currentIntChar == 45) {
            lexeme.append(currentStringChar);
//            token t = new token("minus ", lexeme, lineNum);
            createToken("minus");
        } else if (currentIntChar == 42) {
            lexeme.append(currentStringChar);
//            token t = new token("multiply", lexeme, lineNum);
            createToken("multiply");
        } else if (currentIntChar == 40) {
            lexeme.append(currentStringChar);
//            token t = new token("openround", lexeme, lineNum);
            createToken("openround");
        } else if (currentIntChar == 41) {
            lexeme.append(currentStringChar);
//            token t = new token("closeround", lexeme, lineNum);
            createToken("closeround");
        } else if (currentIntChar == 123) {
            lexeme.append(currentStringChar);
//            token t = new token("opencurly", lexeme, lineNum);
            createToken("opencurly");
        } else if (currentIntChar == 125) {
            lexeme.append(currentStringChar);
//            token t = new token("closecurly", lexeme, lineNum);
            createToken("closecurly");
        } else if (currentIntChar == 91) {
            lexeme.append(currentStringChar);
//            token t = new token("opensquare", lexeme, lineNum);
            createToken("opensquare");
        } else if (currentIntChar == 93) {
            lexeme.append(currentStringChar);
//            token t = new token("closesquare", lexeme, lineNum);
            createToken("closesquare");
        } else if (currentIntChar == 59) {
            lexeme.append(currentStringChar);
//            token t = new token("semicolon", lexeme, lineNum);
            createToken("semicolon");
        } else if (currentIntChar == 44) {
            lexeme.append(currentStringChar);
//            token t = new token("comma", lexeme, lineNum);
            createToken("comma");
        } else if (currentIntChar == 46) {
            lexeme.append(currentStringChar);
//            token t = new token("period", lexeme, lineNum);
            createToken("period");
        } else if (currentIntChar == 32) {
            checkTokenKeywords();
//            createToken("space");
        } else if (currentIntChar == 9) {
            checkTokenKeywords();
        }
        else if (currentIntChar == 13){
            checkTokenKeywords();
        }
        else if (currentIntChar == 10){
            lineNum++;
        }
        else {
            createToken("invalidchar");
        }
    }

    public void checkTokenKeywords(){
        if (lexeme.toString().equals("")){
            createToken("space/tab");
        }
        else if (Pattern.matches("\\bif\\b", lexeme)){
            //create token
            createToken("if");
        }
        else if (Pattern.matches("\\bthen\\b", lexeme)){
            //create token
            createToken("then");
        }
        else if (Pattern.matches("\\belse\\b", lexeme)){
            //create token
            createToken("else");
        }
        else if (Pattern.matches("\\bwhile\\b", lexeme)){
            //create token
            createToken("while");
        }
        else if (Pattern.matches("\\bclass\\b", lexeme)){
            //create token
            createToken("class");
        }
        else if (Pattern.matches("\\bdo\\b", lexeme)){
            //create token
            createToken("do");
        }
        else if (Pattern.matches("\\bend\\b", lexeme)){
            //create token
            createToken("end");
        }
        else if (Pattern.matches("\\bpublic\\b", lexeme)){
            //create token
            createToken("public");
        }
        else if (Pattern.matches("\\bprivate\\b", lexeme)){
            //create token
            createToken("private");
        }
        else if (Pattern.matches("\\bor\\b", lexeme)){
            //create token
            createToken("or");
        }
        else if (Pattern.matches("\\band\\b", lexeme)){
            //create token
            createToken("and");
        }
        else if (Pattern.matches("\\bnot\\b", lexeme)){
            //create token
            createToken("not");
        }
        else if (Pattern.matches("\\bread\\b", lexeme)){
            //create token
            createToken("read");
        }
        else if (Pattern.matches("\\bwrite\\b", lexeme)){
            //create token
            createToken("write");
        }
        else if (Pattern.matches("\\breturn\\b", lexeme)){
            //create token
            createToken("return");
        }
        else if (Pattern.matches("\\bmain\\b", lexeme)){
            //create token
            createToken("main");
        }
        else if (Pattern.matches("\\binherits\\b", lexeme)){
            //create token
            createToken("inherit");
        }
        else if (Pattern.matches("\\blocal\\b", lexeme)){
            //create token
            createToken("local");
        }
        else if (Pattern.matches("[0]", lexeme)){
            createToken("intnum");
        }
        else if (Pattern.matches("-?[1-9][0-9]{0,15}", lexeme)){
            createToken("intnum");
        }
        else if (Pattern.matches("-?[0][0-9]{0,15}", lexeme)){
            createToken("invalidinteger");
        }
        else if (Pattern.matches("-?[1-9][0-9]{0,10}.[0-9]{0,5}[1-9]e?(\\+|-)?[1-9][0-9]{0,10}", lexeme)){
            createToken("float");
        }
        else if (Pattern.matches("-?[0-9]{0,10}[.]?[0-9]{0,5}[e]?(\\+|-)?[0-9]{0,10}", lexeme)){
            createToken("invalidfloat");
        }
        else if (Pattern.matches("\\b([a-zA-Z][0-9a-zA-Z_]{0,19})\\b", lexeme)){
            createToken("id");
        }
        else {
            //invalid token
            createToken("invalidid");
        }
    }

    public void createToken(String token){
        token t = new token(token, lexeme, lineNum);
        System.out.println(t);
        lexeme.replace(0, lexeme.length(), "");
//        backtrack();
    }

    public void backtrack(){
        try {
            br.skip(2*(-1));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void inLineComntToken(){
        do{
            try {
                currentIntChar = br.read();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            lexeme.append(Character.toString((char)currentIntChar));
        }while(currentIntChar!=(13));
        createToken(lexeme.toString());
        lexeme = null;

    }

    public void blockCmntToken(){
        do{
            try {
                currentIntChar = br.read();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            lexeme.append(Character.toString((char)currentIntChar));
            if (currentIntChar==42){
                try {
                    currentIntChar = br.read();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
                lexeme.append(Character.toString((char)currentIntChar));
                if (currentIntChar == 47){
                    createToken(lexeme.toString());
                    lexeme = null;
                    break;
                }
                try {
                    currentIntChar = br.read();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
                lexeme.append(Character.toString((char)currentIntChar));
            }
            try {
                currentIntChar = br.read();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            lexeme.append(Character.toString((char)currentIntChar));
        }while(true);
    }
}
