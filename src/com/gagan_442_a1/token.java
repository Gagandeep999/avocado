package com.gagan_442_a1;

public class token {
    private String token;
    private String lexeme;
    private int linenum;

    public token(String token, String lexeme, int linenum){
        this.token = token;
        this.lexeme = lexeme;
        this.linenum = linenum;
    }

    @Override
    public String toString() {
        return "[" + token + ", " + lexeme + ", " + linenum + "]";
    }
}
