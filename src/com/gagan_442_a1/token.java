package com.gagan_442_a1;

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