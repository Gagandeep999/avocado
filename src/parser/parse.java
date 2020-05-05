package parser;

import lexer.token;
import nodes.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class parse {

    private LinkedList<token> tokens;
    private token lookahead;
    private error e;
    private boolean success;
    private int nodeNum;
    public Stack<node> ast;
    int derivationNum;
    ArrayList<String> derivationList;
    PrintWriter pwDerivation;
    PrintWriter pwSyntaxError;

    public parse(LinkedList<token> tokenLinkedList, String outputLocation){
        this.tokens = (LinkedList<token>) tokenLinkedList.clone();
        this.lookahead = tokenLinkedList.getFirst();
        this.e = new error();
        this.e.createTable();
        this.success = true;
        this.nodeNum = 0;
        this.ast = new Stack<>();
        this.derivationList = new ArrayList<>();
        this.derivationNum=0;

        String outderivation = outputLocation.replace(".src", ".outderivation");
        String outsyntaxerrors = outputLocation.replace(".src", ".outsyntaxerrors");
        try {
            this.pwDerivation = new PrintWriter(outderivation, "UTF-8");
            this.pwSyntaxError = new PrintWriter(outsyntaxerrors, "UTF-8");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void nextToken(){
//        System.out.println(lookahead);
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            lookahead = new token("EPSILON" , new StringBuilder(" "), -1);
        else
            lookahead = tokens.getFirst();
    }

    private boolean match(String token){
        if (lookahead.getToken().equals(token)){
            switch (token){
                case "id" : ast.push(new idNode(lookahead.getLexeme().toString()));
                    break;
                case "class" : ast.push(new classNode(token));
                    break;
                case "inherits" : ast.push(new generalNode(token));
                    break;
                case "public" :
                case "private" : ast.push(new typeNode(token));
                    break;
                case "if" :
                case "while" :
                case "read" :
                case "write" :
                case "return" : ast.push(new statBlockNode(token));
                    break;
                case "plus" :
                case "minus" :
                case "or" :
                    ast.push(new addOpNode(lookahead.getLexeme().toString()));
                    break;
                case "eq" :
                case "neq" :
                case "lessthan" :
                case "gt" :
                case "leq" :
                case "geq" :
                    ast.push(new compareOpNode(lookahead.getLexeme().toString()));
                    break;
                case "mult" :
                case "div" :
                case "and" :
                    ast.push(new multOpNode(lookahead.getLexeme().toString()));
                    break;
                case "integer" : A_CREATEADD(lookahead.getToken());
                    break;
                case "float" : A_CREATEADD(lookahead.getToken());
                    break;
                case "not" : A_CREATEADD(lookahead.getToken());
                    break;
                case "main" : ast.push(new mainNode(token));
                    break;
                case "void" : A_CREATEADD(lookahead.getToken());
                    break;
                case "intnum" : ast.push(new numNode(lookahead.getLexeme().toString(), "integer"));
                    break;
                case "floatnum" : ast.push(new numNode(lookahead.getLexeme().toString(), "float"));
                    break;
                case "equal" : ast.push(new assignStatNode(lookahead.getLexeme().toString()));
                    break;
                default: break;
            }
            nextToken();
            return true;
        }else {
            pwSyntaxError.write("SYNTAX ERROR IN LINE NO: "+lookahead.getLinenum()+ " EXPECTED: "+token+" GOT "+lookahead.getToken());
            nextToken();
            return false;
        }
    }

    private boolean skipErrors(String NonTerminal){
        if ( (e.FIRST(NonTerminal).contains(lookahead.getToken()))
                || ( (e.isNULLABLE(NonTerminal)) && (e.FOLLOW(NonTerminal).contains(lookahead.getToken())) ) ){
            return true;
        } else {
            pwSyntaxError.write("SYNTAX ERROR IN LINE NO: "+lookahead.getLinenum()+ " EXPECTED: "+lookahead.getToken()+"" +
                    " IN METHOD: "+NonTerminal);
            while ( (!e.FIRST(NonTerminal).contains(lookahead.getToken()))
                    || (!e.FOLLOW(NonTerminal).contains(lookahead.getToken())) ){
                nextToken();
                if ( (e.isNULLABLE(NonTerminal)) && (e.FOLLOW(NonTerminal).contains(lookahead.getToken())) ){
                    return false;
                }
            }
            return true;
        }
    }

    public boolean parse() {

        if (START())
            derivationList.add(0, "Sucessfuly parsed the source code!\n");

        for (String derivationRule :
                derivationList) {
            pwDerivation.write(derivationRule);
        }

        pwSyntaxError.flush();
        pwDerivation.flush();
        pwSyntaxError.close();
        pwSyntaxError.close();

        return success;
    }

    private boolean START(){
//        START  -> PROGRAM .
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("START")) return false;
        if (e.FIRST("PROGRAM").contains(lookahead.getToken())){
            if (PROGRAM()){
                derivationList.set(d, "START  -> PROGRAM .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean PROGRAM(){
//        PROGRAM -> #1 REPTCLASSDECL #2 REPTFUNCDEF #2 main FUNCBODY #2 #2 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("PROGRAM")) return false;
        if (e.FIRST("REPTCLASSDECL").contains(lookahead.getToken()) || e.isNULLABLE("REPTCLASSDECL")){
            if (A_CREATEADD("program") && REPTCLASSDECL() && A_RIGHTCHILD() && REPTFUNCDEF()
                    && A_RIGHTCHILD() && match("main") && FUNCBODY() && A_RIGHTCHILD() && A_RIGHTCHILD()){ //&& A_RIGHTCHILD()
                derivationList.set(d, "PROGRAM  ->  REPTCLASSDECL REPTFUNCDEF main FUNCBODY .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTCLASSDECL(){
//        REPTCLASSDECL -> #1 CLASSDECL #2 REPTCLASSDECL #4 .
//        REPTCLASSDECL ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("REPTCLASSDECL")) return false;
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken()) ){
            if (A_CREATEADD("class_list") && CLASSDECL() && A_RIGHTCHILD()
                    && REPTCLASSDECL() && A_ADOPTS()){
                derivationList.set(d, "REPTCLASSDECL -> CLASSDECL  REPTCLASSDECL .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTCLASSDECL").contains(lookahead.getToken())){
            if(A_CREATEADD("class_list")){
                derivationList.set(d, "REPTCLASSDECL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean CLASSDECL(){
//        CLASSDECL -> class id #2 OPTCLASSDECL2 #2 lcurbr MEMBER_DECLARATIONS #2 rcurbr semi  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("CLASSDECL")) return false;
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken())){
            if (match("class") && match("id") && A_RIGHTCHILD()
                    && OPTCLASSDECL2() && A_RIGHTCHILD() && match("lcurbr")
                    && MEMBER_DECLARATIONS() && A_RIGHTCHILD() && match("rcurbr") && match("semi")){
                derivationList.set(d, "CLASSDECL  -> class id OPTCLASSDECL2 lcurbr MEMBER_DECLARATIONS rcurbr semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTFUNCDEF(){
//        REPTFUNCDEF -> #1 FUNCDEF #2 REPTFUNCDEF #4  .
//        REPTFUNCDEF ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("REPTFUNCDEF")) return false;
        if (e.FIRST("FUNCDEF").contains(lookahead.getToken()) ){
            if (A_CREATEADD("function_list") && FUNCDEF() && A_RIGHTCHILD()
                    && REPTFUNCDEF() && A_ADOPTS()){
                derivationList.set(d, "REPTFUNCDEF -> FUNCDEF REPTFUNCDEF .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTFUNCDEF").contains(lookahead.getToken())){
            if(A_CREATEADD("function_list")){
                derivationList.set(d, "REPTFUNCDEF ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCDEF(){
//        FUNCDEF  -> #1(function) FUNCTION_SIGNATURE #2 FUNCBODY #2 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCDEF")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE").contains(lookahead.getToken())){
            if (A_CREATEADD("func_def") && FUNCTION_SIGNATURE() && A_RIGHTCHILD()
                    && FUNCBODY() && A_RIGHTCHILD()){
                derivationList.set(d, "FUNCDEF  -> FUNCTION_SIGNATURE FUNCBODY semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean MEMBER_DECLARATIONS(){
//        MEMBER_DECLARATIONS  -> #1 VISIBILITY MEMBER_DECLARATION #3 #2 MEMBER_DECLARATIONS #4 .
//        MEMBER_DECLARATIONS  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("MEMBER_DECLARATIONS")) return false;
        if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            if (A_CREATEADD("var+func decl list") && VISIBILITY() && MEMBER_DECLARATION() && A_LEFTCHILD()
                    && A_RIGHTCHILD() && MEMBER_DECLARATIONS() && A_ADOPTS()){
                derivationList.set(d, "MEMBER_DECLARATIONS  -> VISIBILITY MEMBER_DECLARATION MEMBER_DECLARATIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTCLASSDECL").contains(lookahead.getToken()) || e.isNULLABLE("REPTCLASSDECL") ){ //
            if (A_CREATEADD("var+func decl list")){
                derivationList.set(d, "REPTCLASSDECL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean MEMBER_DECLARATION(){
//        MEMBER_DECLARATION -> TYPE_NON_ID VARIABLE_DECLARATION #3 .
//        MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION #3 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("MEMBER_DECLARATION")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){
            if (TYPE_NON_ID() && VARIABLE_DECLARATION() && A_LEFTCHILD()){
                derivationList.set(d, "MEMBER_DECLARATION -> TYPE_NON_ID VARIABLE_DECLARATION .\n");
            }else success = false;
        }else if (e.FIRST("MEMBER_DECLARATION").contains(lookahead.getToken())){
            if (match("id") && FUNCTION_OR_VARIABLE_DECLARATION() && A_LEFTCHILD()){
                derivationList.set(d, "MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION .\n");
            }else  success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_OR_VARIABLE_DECLARATION(){
//        FUNCTION_OR_VARIABLE_DECLARATION -> FUNCTION_DECLARATION .
//        FUNCTION_OR_VARIABLE_DECLARATION -> VARIABLE_DECLARATION .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_OR_VARIABLE_DECLARATION")) return false;
        if (e.FIRST("FUNCTION_DECLARATION").contains(lookahead.getToken())){
            if (FUNCTION_DECLARATION()){
                derivationList.set(d, "FUNCTION_OR_VARIABLE_DECLARATION -> FUNCTION_DECLARATION .\n");
            }else success = false;
        }else if (e.FIRST("VARIABLE_DECLARATION").contains(lookahead.getToken())){
            if (VARIABLE_DECLARATION()){
                derivationList.set(d, "FUNCTION_OR_VARIABLE_DECLARATION -> VARIABLE_DECLARATION .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VISIBILITY(){
//        VISIBILITY -> public .
//        VISIBILITY -> private .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VISIBILITY")) return false;
        if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            switch (lookahead.getToken()){
                case "public" : match("public");
                    derivationList.set(d, "VISIBILITY -> public .\n");
                    break;
                case "private" : match("private");
                    derivationList.set(d, "VISIBILITY -> private .\n");
                    break;
                default: success = false;
                break;
            }
        }else success = false;
        return success;
    }

    private boolean STATEMENT(){
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("STATEMENT")) return false;
//        STATEMENT  -> ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (e.FIRST("ASSIGN_STATEMENT_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                derivationList.set(d, "STATEMENT  -> ASSIGN_STATEMENT_OR_FUNCTION_CALL .\n");
            }else success = false;
        }
//        STATEMENT  -> if lpar REL_EXPRESSION #2 rpar then STATEMENT_BLOCK #2 else STATEMENT_BLOCK #2 semi  .
//        STATEMENT  -> while lpar REL_EXPRESSION #2 rpar STATEMENT_BLOCK #2 semi  .
//        STATEMENT  -> read lpar STATEMENT_VARIABLE #6 #5 #2 rpar semi  .
//        STATEMENT  -> write lpar EXPRESSION #2 rpar semi  .
//        STATEMENT  -> return lpar EXPRESSION #2 rpar semi  .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "if": if ( match("if") && match("lpar") && REL_EXPRESSION() && A_RIGHTCHILD()
                        && match("rpar") && match("then") && STATEMENT_BLOCK() && A_RIGHTCHILD()
                        && match("else") && STATEMENT_BLOCK() && A_RIGHTCHILD() && match("semi") ){
                    derivationList.set(d, "STATEMENT  -> if lpar REL_EXPRESSION rpar then STATEMENT_BLOCK else STATEMENT_BLOCK semi .\n");
                }break;
                case "while" : if ( match("while") && match("lpar") && REL_EXPRESSION() && A_RIGHTCHILD()
                        && match("rpar") && STATEMENT_BLOCK() && A_RIGHTCHILD() && match("semi") ){
                    derivationList.set(d, "STATEMENT  -> while lpar REL_EXPRESSION rpar STATEMENT_BLOCK semi .\n");
                }break;
                case "read" : if (match("read") && match("lpar") && STATEMENT_VARIABLE() && A_DELETE()
                        && A_GROUP() && A_RIGHTCHILD()  && match("rpar") && match("semi")){
                    derivationList.set(d, "STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .\n");
                }break;
                case "write" : if (match("write") && match("lpar") && EXPRESSION()
                        && A_RIGHTCHILD() && match("rpar") && match("semi")){
                    derivationList.set(d, "STATEMENT  -> write lpar EXPRESSION rpar semi .\n");
                }break;
                case "return" : if (match("return") && match("lpar") && EXPRESSION()
                        && A_RIGHTCHILD() && match("rpar") && match("semi")){
                    derivationList.set(d, "STATEMENT  -> return lpar EXPRESSION rpar semi .\n");
                }break;
                default: success = false;
                break;
            }

        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE(){
//        STATEMENT_VARIABLE -> #1(item) id #2 STATEMENT_VARIABLE_OR_FUNCTION_CALL .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("STATEMENT_VARIABLE")) return false;
        if (e.FIRST("STATEMENT_VARIABLE").contains(lookahead.getToken())){
            if (A_CREATEADD("call") && match("id") && A_RIGHTCHILD()
                    && STATEMENT_VARIABLE_OR_FUNCTION_CALL()){
                derivationList.set(d, "STATEMENT_VARIABLE -> id STATEMENT_VARIABLE_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE_OR_FUNCTION_CALL(){
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES #2 STATEMENT_VARIABLE_EXT .
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS #2 rpar STATEMENT_FUNCTION_CALL .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("STATEMENT_VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.isNULLABLE("INDICES")){ //
            if (INDICES() && A_RIGHTCHILD() && STATEMENT_VARIABLE_EXT()){
                derivationList.set(d, "STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .\n");
            }else success = false;
        }else if (e.FIRST("STATEMENT_VARIABLE_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && A_RIGHTCHILD() && match("rpar")
                    && STATEMENT_FUNCTION_CALL()){
                derivationList.set(d, "STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE_EXT(){
//        STATEMENT_VARIABLE_EXT -> dot STATEMENT_VARIABLE .
//        STATEMENT_VARIABLE_EXT ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("STATEMENT_VARIABLE_EXT")) return false;
        if (e.FIRST("STATEMENT_VARIABLE_EXT").contains(lookahead.getToken())){
            if (match("dot") && STATEMENT_VARIABLE()){
                derivationList.set(d, "STATEMENT_VARIABLE_EXT -> dot STATEMENT_VARIABLE .\n");
            }else success = false;
        }else if (e.FOLLOW("STATEMENT_VARIABLE_EXT").contains(lookahead.getToken())){
            if (A_CREATEADD("STATEMENT_VARIABLE_EXT")){
                derivationList.set(d, "STATEMENT_VARIABLE_EXT ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean STATEMENT_FUNCTION_CALL(){
//        STATEMENT_FUNCTION_CALL  -> dot STATEMENT_VARIABLE .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("STATEMENT_FUNCTION_CALL")) return false;
        if (e.FIRST("STATEMENT_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("dot") && STATEMENT_VARIABLE()){
                derivationList.set(d, "STATEMENT_FUNCTION_CALL  -> dot STATEMENT_VARIABLE .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean ASSIGN_STATEMENT_OR_FUNCTION_CALL(){
//        ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> #1(item) id #2 VARIABLE_OR_FUNCTION_CALL_EXT .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("ASSIGN_STATEMENT_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("ASSIGN_STATEMENT_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (A_CREATEADD("call") && match("id") && A_RIGHTCHILD() && VARIABLE_OR_FUNCTION_CALL_EXT()){
                derivationList.set(d, "ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> id VARIABLE_OR_FUNCTION_CALL_EXT .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL_EXT(){
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> INDICES #2 VARIABLE_EXT .
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> lpar FUNCTION_CALL_PARAMS #2 rpar FUNCTION_CALL_EXT .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.FIRST("VARIABLE_EXT").contains(lookahead.getToken())){ // || e.isNULLABLE("INDICES") || e.FOLLOW("INDICES").contains(lookahead.getToken())
            if (INDICES() && A_RIGHTCHILD() && VARIABLE_EXT()){
                derivationList.set(d, "STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .\n");
            }else success = false;
        }else if (lookahead.getToken().equals("lpar")){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && A_RIGHTCHILD()
                    && match("rpar") && FUNCTION_CALL_EXT()){
                derivationList.set(d, "STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_EXT(){
//        VARIABLE_EXT -> #5 ASSIGNMENT_OP #3 EXPRESSION #2 semi .
//        VARIABLE_EXT -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VARIABLE_EXT")) return false;
        if (e.FIRST("ASSIGNMENT_OP").contains(lookahead.getToken())){
            if (A_GROUP() && ASSIGNMENT_OP() && A_LEFTCHILD() && EXPRESSION() && A_RIGHTCHILD()
                    && match("semi")){
                derivationList.set(d, "VARIABLE_EXT -> ASSIGNMENT_OP EXPRESSION semi .\n");
            }else success = false;
        }else if (e.FIRST("VARIABLE_EXT").contains(lookahead.getToken())){
            if (match("dot") && ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                derivationList.set(d, "VARIABLE_EXT -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_EXT(){
//        FUNCTION_CALL_EXT  -> #5 #1(func_call) #3 semi .
//        FUNCTION_CALL_EXT  -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("FUNCTION_CALL_EXT").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("semi")){
                if (A_GROUP() && A_CREATEADD("FUNC_CALL") && A_LEFTCHILD() && match("semi")){
                    derivationList.set(d, "FUNCTION_CALL_EXT  -> semi .\n");
                }
            }else if (lookahead.getToken().equals("dot"))
                if (match("dot") && ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                    derivationList.set(d, "FUNCTION_CALL_EXT  -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS(){
//        FUNCTION_PARAMS  -> #1 #1(param) TYPE #2 id #2 ARRAY_DIMENSIONS #2 #2 FUNCTION_PARAMS_TAILS #4 .
//        FUNCTION_PARAMS  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_PARAMS")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (A_CREATEADD("fParam list") && A_CREATEADD("fparam") && TYPE() && A_RIGHTCHILD()
                    && match("id") && A_RIGHTCHILD() && ARRAY_DIMENSIONS() && A_RIGHTCHILD() && A_RIGHTCHILD()
                    && FUNCTION_PARAMS_TAILS() && A_ADOPTS()){
                derivationList.set(d, "FUNCTION_PARAMS  -> TYPE id ARRAY_DIMENSIONS FUNCTION_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_PARAMS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_PARAMS")){
                derivationList.set(d, "FUNCTION_PARAMS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ADD_OP(){
//        ADD_OP -> plus . | minus . | or .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("ADD_OP")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            switch (lookahead.getToken()){
                case "plus" : match("plus");
                    derivationList.set(d, "ADD_OP -> plus .\n");
                    break;
                case "minus" : match("minus");
                    derivationList.set(d, "ADD_OP -> minus .\n");
                    break;
                case "or" : match("or");
                    derivationList.set(d, "ADD_OP -> or .\n");
                    break;
                default : success = false;
                break;
            }
        }else success = false;
        return success;
    }

    private boolean OPTCLASSDECL2(){
//        OPTCLASSDECL2  -> inherits id #2 INHERITED_CLASSES #4 .
//        OPTCLASSDECL2  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("OPTCLASSDECL2")) return false;
        if (e.FIRST("OPTCLASSDECL2").contains(lookahead.getToken())){
            if (match("inherits") && match("id") && A_RIGHTCHILD() && INHERITED_CLASSES() && A_ADOPTS()){
                derivationList.set(d, "OPTCLASSDECL2  -> inherits id INHERITED_CLASSES .\n");
            }else success = false;
        }else if (e.FOLLOW("OPTCLASSDECL2").contains(lookahead.getToken())){
            if (A_CREATEADD("inheritance list")){
                derivationList.set(d, "OPTCLASSDECL2 ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION(){
//        REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP #3 ARITH_EXPRESSION #2 .
//        REL_EXPRESSION ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("REL_EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION() && COMPARE_OP() && A_LEFTCHILD() && ARITH_EXPRESSION() && A_RIGHTCHILD()){
                derivationList.set(d, "REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP ARITH_EXPRESSION .\n");
            }else success = false;
        }else if (e.FOLLOW("REL_EXPRESSION").contains(lookahead.getToken())){
            if (A_CREATEADD("REL_EXPRESSION")){
                derivationList.set(d, "REL_EXPRESSION ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCTION_DECLARATION(){
//        FUNCTION_DECLARATION -> #1 lpar FUNCTION_PARAMS #2 rpar colon TYPE_OR_VOID #2 semi  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_DECLARATION")) return false;
        if (e.FIRST("FUNCTION_DECLARATION").contains(lookahead.getToken())){
            if (A_CREATEADD("func decl") && match("lpar") && FUNCTION_PARAMS()
                    && A_RIGHTCHILD() && match("rpar") && match("colon") && TYPE_OR_VOID()
                    && A_RIGHTCHILD() && match("semi")){
                derivationList.set(d, "FUNCTION_DECLARATION -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS_TAILS(){
//        FUNCTION_CALL_PARAMS_TAILS -> #1 FUNCTION_CALL_PARAMS_TAIL #2 FUNCTION_CALL_PARAMS_TAILS #4 .
//        FUNCTION_CALL_PARAMS_TAILS ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS_TAILS") && FUNCTION_CALL_PARAMS_TAIL()
                    && A_RIGHTCHILD() && FUNCTION_CALL_PARAMS_TAILS() && A_ADOPTS()){
                derivationList.set(d, "FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_CALL_PARAMS_TAILS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS_TAILS")){
                derivationList.set(d, "FUNCTION_CALL_PARAMS_TAILS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS_TAIL(){
//        FUNCTION_CALL_PARAMS_TAIL  -> comma EXPRESSION .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAIL")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (match("comma") && EXPRESSION() ){
                derivationList.set(d, "FUNCTION_CALL_PARAMS_TAIL  -> comma EXPRESSION .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS(){
//        FUNCTION_CALL_PARAMS -> #1 EXPRESSION #2 FUNCTION_CALL_PARAMS_TAILS #4 .
//        FUNCTION_CALL_PARAMS ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_CALL_PARAMS")) return false;
        if (e.FIRST("EXPRESSION").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS") && EXPRESSION() && A_RIGHTCHILD()
                    && FUNCTION_CALL_PARAMS_TAILS() && A_ADOPTS()){
                derivationList.set(d, "FUNCTION_CALL_PARAMS -> EXPRESSION FUNCTION_CALL_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_CALL_PARAMS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS")){
                derivationList.set(d, "FUNCTION_CALL_PARAMS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean OPTFUNCBODY0(){
//        OPTFUNCBODY0  -> local VARIABLE_DECLARATIONS .
//        OPTFUNCBODY0  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("OPTFUNCBODY0")) return false;
        if (e.FIRST("OPTFUNCBODY0").contains(lookahead.getToken())){
            if (match("local") && VARIABLE_DECLARATIONS()){
                derivationList.set(d, "OPTFUNCBODY0  -> local VARIABLE_DECLARATIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("OPTFUNCBODY0").contains(lookahead.getToken())){
            if (A_CREATEADD("OPTFUNCBODY0")){
                derivationList.set(d, "OPTFUNCBODY0 ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ARRAY_DIMENSIONS(){
//        ARRAY_DIMENSIONS -> #1 ARRAY_SIZE #2 #7 ARRAY_DIMENSIONS #4 .
//        ARRAY_DIMENSIONS ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("ARRAY_DIMENSIONS")) return false;
        if (e.FIRST("ARRAY_SIZE").contains(lookahead.getToken())){
            if (A_CREATEADD("dim list") && ARRAY_SIZE() && A_RIGHTCHILD() && A_UNIFY()
                    && ARRAY_DIMENSIONS() && A_ADOPTS()){
                derivationList.set(d, "ARRAY_DIMENSIONS -> ARRAY_SIZE ARRAY_DIMENSIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("ARRAY_DIMENSIONS").contains(lookahead.getToken())){
            if (A_CREATEADD("dim list")){
                derivationList.set(d, "ARRAY_DIMENSIONS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean EXPRESSION(){
//        EXPRESSION -> ARITH_EXPRESSION  REL_EXPRESSION_OR_NULL #3 #7 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION() && REL_EXPRESSION_OR_NULL() && A_LEFTCHILD() && A_UNIFY()){
                derivationList.set(d, "EXPRESSION -> ARITH_EXPRESSION REL_EXPRESSION_OR_NULL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION_OR_NULL(){
//        REL_EXPRESSION_OR_NULL -> COMPARE_OP ARITH_EXPRESSION #2 .
//        REL_EXPRESSION_OR_NULL ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("REL_EXPRESSION_OR_NULL")) return false;
        if (e.FIRST("COMPARE_OP").contains(lookahead.getToken())){
            if (COMPARE_OP() && ARITH_EXPRESSION() && A_RIGHTCHILD()){
                derivationList.set(d , "REL_EXPRESSION_OR_NULL -> COMPARE_OP ARITH_EXPRESSION .\n");
            }else success = false;
        }else if (e.FOLLOW("REL_EXPRESSION_OR_NULL").contains(lookahead.getToken())){
            if (A_CREATEADD("REL_EXPRESSION_OR_NULL")){
                derivationList.set(d, "REL_EXPRESSION_OR_NULL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean REPTSTATEMENT(){
//        REPTSTATEMENT -> #1 STATEMENT #2 REPTSTATEMENT #4 .
//        REPTSTATEMENT ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("REPTSTATEMENT")) return false;
        if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (A_CREATEADD("REPTSTATEMENT") && STATEMENT() && A_RIGHTCHILD()
                    && REPTSTATEMENT() && A_ADOPTS()){
                derivationList.set(d, "REPTSTATEMENT -> STATEMENT REPTSTATEMENT .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTSTATEMENT").contains(lookahead.getToken())){
            if (A_CREATEADD("REPTSTATEMENT")){
                derivationList.set(d, "REPTSTATEMENT ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ARITH_EXPRESSION(){
//        ARITH_EXPRESSION -> TERM RIGHT_REC_ARITH_EXPRESSION #6 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("ARITH_EXPRESSION")) return false;
        if (e.FIRST("TERM").contains(lookahead.getToken())){
            if (TERM() && RIGHT_REC_ARITH_EXPRESSION() && A_DELETE()){
                derivationList.set(d, "ARITH_EXPRESSION -> TERM RIGHT_REC_ARITH_EXPRESSION .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean RIGHT_REC_ARITH_EXPRESSION(){
//        RIGHT_REC_ARITH_EXPRESSION -> ADD_OP #3 TERM #2 RIGHT_REC_ARITH_EXPRESSION .
//        RIGHT_REC_ARITH_EXPRESSION ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("RIGHT_REC_ARITH_EXPRESSION")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            if (ADD_OP() && A_LEFTCHILD() && TERM() && A_RIGHTCHILD() && RIGHT_REC_ARITH_EXPRESSION()){
                derivationList.set(d, "RIGHT_REC_ARITH_EXPRESSION -> ADD_OP TERM RIGHT_REC_ARITH_EXPRESSION .\n");
            }else success = false;
        }else if (e.FOLLOW("RIGHT_REC_ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (A_CREATEADD("RIGHT_REC_ARITH_EXPRESSION")){
                derivationList.set(d, "RIGHT_REC_ARITH_EXPRESSION ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE(){
//        FUNCTION_SIGNATURE -> #1 id FUNCTION_SIGNATURE_NAMESPACE #4 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_SIGNATURE")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_SIGNATURE") && match("id") && FUNCTION_SIGNATURE_NAMESPACE()
                    && A_ADOPTS()){
                derivationList.set(d, "FUNCTION_SIGNATURE -> id FUNCTION_SIGNATURE_NAMESPACE .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE_NAMESPACE(){
//        FUNCTION_SIGNATURE_NAMESPACE -> #2 FUNCTION_SIGNATURE_EXT .
//        FUNCTION_SIGNATURE_NAMESPACE -> #1(namespace) #3 #7 #2 coloncolon id #2 FUNCTION_SIGNATURE_EXT .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_SIGNATURE_NAMESPACE")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE_EXT").contains(lookahead.getToken())){
            if (A_RIGHTCHILD() && FUNCTION_SIGNATURE_EXT()){
                derivationList.set(d, "FUNCTION_SIGNATURE_NAMESPACE -> FUNCTION_SIGNATURE_EXT .\n");
            }else success = false;
        }else if (e.FIRST("FUNCTION_SIGNATURE_NAMESPACE").contains(lookahead.getToken())){
            if (A_CREATEADD("NAMESPACE") && A_LEFTCHILD() && A_UNIFY() && A_RIGHTCHILD() && match("sr")
                    && match("id") && A_RIGHTCHILD() && FUNCTION_SIGNATURE_EXT()){
                derivationList.set(d, "FUNCTION_SIGNATURE_NAMESPACE -> sr id FUNCTION_SIGNATURE_EXT .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE_EXT(){
//        FUNCTION_SIGNATURE_EXT -> #1 lpar FUNCTION_PARAMS #2 rpar colon #1(returns) TYPE_OR_VOID #2 #2 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_SIGNATURE_NAMESPACE")) return false;
        if (!skipErrors("FUNCTION_SIGNATURE_EXT")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE_EXT").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_SIGNATURE_EXT") && match("lpar") && FUNCTION_PARAMS()
                    && A_RIGHTCHILD() && match("rpar") && match("colon") //&& A_CREATEADD("RETURNS")
                    && TYPE_OR_VOID() && A_RIGHTCHILD() ){ //&& A_RIGHTCHILD()){
                derivationList.set(d, "FUNCTION_SIGNATURE_EXT -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS_TAILS(){
//        FUNCTION_PARAMS_TAILS  -> #1 FUNCTION_PARAMS_TAIL #2 FUNCTION_PARAMS_TAILS #4 .
//        FUNCTION_PARAMS_TAILS  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_PARAMS_TAIL").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_PARAMS_TAILS") && FUNCTION_PARAMS_TAIL() && A_RIGHTCHILD()
                    && FUNCTION_PARAMS_TAILS() && A_ADOPTS()){
                derivationList.set(d, "FUNCTION_PARAMS_TAILS  -> FUNCTION_PARAMS_TAIL FUNCTION_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_PARAMS_TAILS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_PARAMS_TAILS")){
                derivationList.set(d, "FUNCTION_PARAMS_TAILS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean INHERITED_CLASSES(){
//        INHERITED_CLASSES  -> #1 comma id #2 INHERITED_CLASSES #4 .
//        INHERITED_CLASSES  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("INHERITED_CLASSES")) return false;
        if (e.FIRST("INHERITED_CLASSES").contains(lookahead.getToken())){
            if (A_CREATEADD("INHERITED_CLASSES") && match("comma") && match("id") && A_RIGHTCHILD()
                    && INHERITED_CLASSES() && A_ADOPTS()){
                derivationList.set(d, "INHERITED_CLASSES  -> comma id INHERITED_CLASSES .\n");
            }else success = false;
        }else if (e.FOLLOW("INHERITED_CLASSES").contains(lookahead.getToken())){
            if (A_CREATEADD("INHERITED_CLASSES")){
                derivationList.set(d, "INHERITED_CLASSES ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean SIGN(){
//        SIGN -> plus . | minus .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("SIGN")) return false;
        if (e.FIRST("SIGN").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("plus")){
                if (match("plus")){
                    derivationList.set(d, "SIGN -> plus .\n");
                }
            }else if (lookahead.getToken().equals("minus")){
                if (match("minus")){
                    derivationList.set(d, "SIGN -> minus .\n");
            }else success = false;
            }
        }else success = false;
        return success;
    }

    private boolean COMPARE_OP(){
//        COMPARE_OP -> eq . | neq . | lt . | gt . | leq . | geq .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("COMPARE_OP")) return false;
        if (e.FIRST("COMPARE_OP").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "eq" : match("eq");
                    derivationList.set(d, "SIGN -> eq .\n");
                    break;
                case "neq" : match("neq");
                    derivationList.set(d, "SIGN -> neq .\n");
                    break;
                case "lessthan" : match("lessthan");
                    derivationList.set(d, "SIGN -> lessthan .\n");
                    break;
                case "gt" : match("gt");
                    derivationList.set(d, "SIGN -> gt .\n");
                    break;
                case "leq" : match("leq");
                    derivationList.set(d, "SIGN -> leq .\n");
                    break;
                case "geq" : match("geq");
                    derivationList.set(d, "SIGN -> geq .\n");
                    break;
                default: success = false;
                    break;
            }
        }else success = false;
        return success;
    }

    private boolean INDEX(){
//        INDEX  -> lsqbr ARITH_EXPRESSION rsqbr .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("INDEX")) return false;
        if (e.FIRST("INDEX").contains(lookahead.getToken())){
            if (match("lsqbr") && ARITH_EXPRESSION() && match("rsqbr")){
                derivationList.set(d, "INDEX  -> lsqbr ARITH_EXPRESSION rsqbr .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_DECLARATIONS(){
//        VARIABLE_DECLARATIONS  -> #1 TYPE VARIABLE_DECLARATION #3 #2 VARIABLE_DECLARATIONS #4 .
//        VARIABLE_DECLARATIONS  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VARIABLE_DECLARATIONS")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (A_CREATEADD("VARIABLE_DECLARATIONS") && TYPE() && VARIABLE_DECLARATION() && A_LEFTCHILD()
                    && A_RIGHTCHILD() && VARIABLE_DECLARATIONS() && A_ADOPTS()){
                derivationList.set(d, "VARIABLE_DECLARATIONS  -> TYPE VARIABLE_DECLARATION VARIABLE_DECLARATIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("VARIABLE_DECLARATIONS").contains(lookahead.getToken())){
            if (A_CREATEADD("VARIABLE_DECLARATIONS")){
                derivationList.set(d, "VARIABLE_DECLARATIONS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FACTOR(){
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FACTOR")) return false;
//        FACTOR -> VARIABLE_FUNCTION_CALL #6 #5.
        if (e.FIRST("VARIABLE_FUNCTION_CALL").contains(lookahead.getToken())){
            if (VARIABLE_FUNCTION_CALL() && A_DELETE() && A_GROUP()){
                derivationList.set(d, "FACTOR -> VARIABLE_FUNCTION_CALL .\n");
            }else success = false;
        }
//        FACTOR -> #1 SIGN #2 FACTOR #2 .
        else if (e.FIRST("SIGN").contains(lookahead.getToken())){
            if (A_CREATEADD("FACTOR") && SIGN() && A_RIGHTCHILD() && FACTOR() && A_RIGHTCHILD()){
                derivationList.set(d, "FACTOR -> SIGN FACTOR .\n");
            }else success = false;
        }
//        FACTOR -> lpar ARITH_EXPRESSION rpar .
//        FACTOR -> #1 not #2 FACTOR #2 .
//        FACTOR -> integer .
//        FACTOR -> float .
        else if (e.FIRST("FACTOR").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "lpar" : if (match("lpar") && ARITH_EXPRESSION() && match("rpar")) {
                    derivationList.set(d, "FACTOR -> lpar ARITH_EXPRESSION rpar .\n");
                }break;
                case "not" : if (A_CREATEADD("FACTOR") && match("not") && A_RIGHTCHILD()
                        && FACTOR() && A_RIGHTCHILD()){
                    derivationList.set(d, "FACTOR -> not FACTOR .\n");
                }break;
                case "intnum" : if (match("intnum")){
                    derivationList.set(d, "FACTOR -> intnum .\n");
                }break;
                case "floatnum" : if (match("floatnum")){
                    derivationList.set(d, "FACTOR -> floatnum .\n");
                }break;
                default: success = false;
                break;
            }
        }else success = false;
        return success;
    }

    private boolean VARIABLE_FUNCTION_CALL(){
//        VARIABLE_FUNCTION_CALL -> #1(item) id #2 VARIABLE_OR_FUNCTION_CALL .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VARIABLE_FUNCTION_CALL")) return false;
        if (e.FIRST("VARIABLE_FUNCTION_CALL").contains(lookahead.getToken())){
            if (A_CREATEADD("ITEM") && match("id") && A_RIGHTCHILD() && VARIABLE_OR_FUNCTION_CALL()){
                derivationList.set(d, "VARIABLE_FUNCTION_CALL -> id VARIABLE_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL(){
//        VARIABLE_OR_FUNCTION_CALL  -> INDICES #2 FACTOR_VARIABLE .
//        VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS #2 rpar FACTOR_FUNCTION_CALL .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.FOLLOW("INDICES").contains(lookahead.getToken()) ){ //
            if (INDICES() && A_RIGHTCHILD() && FACTOR_VARIABLE()){
                derivationList.set(d, "VARIABLE_OR_FUNCTION_CALL  -> INDICES FACTOR_VARIABLE .\n");
            }else success = false;
        }else if (e.FIRST("VARIABLE_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && A_RIGHTCHILD() && match("rpar")
                && FACTOR_FUNCTION_CALL()){
                derivationList.set(d, "VARIABLE_OR_FUNCTION_CALL -> lpar FUNCTION_CALL_PARAMS rpar FACTOR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FACTOR_VARIABLE(){
//        FACTOR_VARIABLE  -> dot VARIABLE_FUNCTION_CALL .
//        FACTOR_VARIABLE  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FACTOR_VARIABLE")) return false;
        if (e.FIRST("FACTOR_VARIABLE").contains(lookahead.getToken())){ // || e.isNULLABLE("FACTOR_VARIABLE")
            if (match("dot") && VARIABLE_FUNCTION_CALL()){
                derivationList.set(d, "FACTOR_VARIABLE  -> dot VARIABLE_FUNCTION_CALL .\n");
            }else success = false;
        }else if (e.FOLLOW("FACTOR_VARIABLE").contains(lookahead.getToken())){
            if (A_CREATEADD("FACTOR_VARIABLE")){
                derivationList.set(d, "FACTOR_VARIABLE ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FACTOR_FUNCTION_CALL(){
//        FACTOR_FUNCTION_CALL -> dot VARIABLE_FUNCTION_CALL .
//        FACTOR_FUNCTION_CALL ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FACTOR_FUNCTION_CALL")) return false;
        if (e.FIRST("FACTOR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("dot") && VARIABLE_FUNCTION_CALL()){
                derivationList.set(d, "FACTOR_FUNCTION_CALL  -> dot VARIABLE_FUNCTION_CALL .\n");
            }else success = false;
        }else if (e.FOLLOW("FACTOR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (A_CREATEADD("FACTOR_FUNCTION_CALL")){
                derivationList.set(d, "FACTOR_FUNCTION_CALL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TERM(){
//        TERM -> FACTOR RIGHT_REC_TERM #6 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("TERM")) return false;
        if (e.FIRST("FACTOR").contains(lookahead.getToken())){
            if (FACTOR() && RIGHT_REC_TERM() &&  A_DELETE()){
                derivationList.set(d, "TERM -> FACTOR RIGHT_REC_TERM .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean MULT_OP(){
//        MULT_OP  -> mult . | div . | and .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("MULT_OP")) return false;
        if (e.FIRST("MULT_OP").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "mult" : match("mult");
                    derivationList.set(d, "MULT_OP -> mult .\n");
                    break;
                case "div" : match("div");
                    derivationList.set(d, "MULT_OP -> div .\n");
                    break;
                case "and" : match("and");
                    derivationList.set(d, "MULT_OP -> and .\n");
                    break;
                default: success = false;
                    break;
            }
        }else success = false;
        return success;
    }

    private boolean RIGHT_REC_TERM(){
//        RIGHT_REC_TERM -> MULT_OP #3 FACTOR #2 RIGHT_REC_TERM .
//        RIGHT_REC_TERM ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("RIGHT_REC_TERM")) return false;
        if (e.FIRST("MULT_OP").contains(lookahead.getToken())){
            if (MULT_OP() && A_LEFTCHILD() && FACTOR() && A_RIGHTCHILD() && RIGHT_REC_TERM()){
                derivationList.set(d, "RIGHT_REC_TERM -> MULT_OP FACTOR RIGHT_REC_TERM .\n");
            }else success = false;
        }else if (e.FOLLOW("RIGHT_REC_TERM").contains(lookahead.getToken())){
            if (A_CREATEADD("RIGHT_REC_TERM")){
                derivationList.set(d, "RIGHT_REC_TERM ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE_OR_VOID(){
//        TYPE_OR_VOID -> TYPE .
//        TYPE_OR_VOID -> void .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("TYPE_OR_VOID")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (TYPE()){
                derivationList.set(d, "TYPE_OR_VOID -> TYPE .\n");
            }else success = false;
        }else if (e.FIRST("TYPE_OR_VOID").contains(lookahead.getToken())){
            if (match("void")){
                derivationList.set(d, "TYPE_OR_VOID -> void .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE(){
//        TYPE -> TYPE_NON_ID .
//        TYPE -> id .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("TYPE")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){
            if (TYPE_NON_ID()){
                derivationList.set(d, "TYPE -> TYPE_NON_ID .\n");
            }else success = false;
        }else if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (match("id")){
                derivationList.set(d, "TYPE -> id .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE_NON_ID(){
//        TYPE_NON_ID  -> integer .
//        TYPE_NON_ID  -> float .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("TYPE_NON_ID")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "integer" : match("integer");
                    derivationList.set(d, "TYPE_NON_ID  -> integer .\n");
                    break;
                case "float" : match("float");
                    derivationList.set(d, "TYPE_NON_ID  -> float .\n");
                    break;
                default: success = false;
                    break;
            }
        }else success = false;
        return success;
    }

    private boolean ARRAY_SIZE(){
//        ARRAY_SIZE -> #1 lsqbr OPTIONAL_INT #2 #7 rsqbr .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("ARRAY_SIZE")) return false;
        if (e.FIRST("ARRAY_SIZE").contains(lookahead.getToken())){
            if (A_CREATEADD("ARRAY_SIZE") && match("lsqbr") && OPTIONAL_INT() && A_RIGHTCHILD()
                    && A_UNIFY() && match("rsqbr")){
                derivationList.set(d, "TERM -> FACTOR RIGHT_REC_TERM .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean OPTIONAL_INT(){
//        OPTIONAL_INT -> #1 intnum #2 .
//        OPTIONAL_INT ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("OPTIONAL_INT")) return false;
        if (e.FIRST("OPTIONAL_INT").contains(lookahead.getToken())){
            if (A_CREATEADD("arr_size") && match("intnum") && A_RIGHTCHILD()){
                derivationList.set(d, "OPTIONAL_INT -> intnum .\n");
            }else success = false;
        }else if (e.FOLLOW("OPTIONAL_INT").contains(lookahead.getToken())){
            if (A_CREATEADD("arr_size")){
                derivationList.set(d, "OPTIONAL_INT ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean VARIABLE_DECLARATION(){
//        VARIABLE_DECLARATION -> #1(var) id #2 ARRAY_DIMENSIONS #2 semi  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("VARIABLE_DECLARATION")) return false;
        if (e.FIRST("VARIABLE_DECLARATION").contains(lookahead.getToken())){
            if (A_CREATEADD("var decl") && match("id") && A_RIGHTCHILD() && ARRAY_DIMENSIONS()
                    && A_RIGHTCHILD() && match("semi")){
                derivationList.set(d, "VARIABLE_DECLARATION -> id ARRAY_DIMENSIONS semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCBODY(){
//        FUNCBODY  ->  #1 OPTFUNCBODY0 #2 do REPTSTATEMENT #2 end  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCBODY")) return false;
        if (e.FIRST("OPTFUNCBODY0").contains(lookahead.getToken()) || e.isNULLABLE("OPTFUNCBODY0")){
//            @TODO: 2020-03-07 check what is the effect of taking out A_RIGHTCHILD() here
            if (A_CREATEADD("FUNCBODY") && OPTFUNCBODY0() && A_RIGHTCHILD() && match("do") //&& A_RIGHTCHILD()
                    && REPTSTATEMENT() && A_RIGHTCHILD() && match("end")){
                derivationList.set(d, "FUNCBODY  -> OPTFUNCBODY0 do REPTSTATEMENT end .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_BLOCK(){
//        STATEMENT_BLOCK  -> #1 STATEMENT #2 .
//        STATEMENT_BLOCK  -> #1 do REPTSTATEMENT #4 end  .
//        STATEMENT_BLOCK  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("STATEMENT_BLOCK")) return false;
        if (e.FIRST("STATEMENT").contains(lookahead.getToken())) {
            if (A_CREATEADD("STATEMENT_BLOCK") && STATEMENT() && A_RIGHTCHILD()) {
                derivationList.set(d, "STATEMENT_BLOCK  -> STATEMENT .\n");
            } else success = false;
        }else if (e.FIRST("STATEMENT_BLOCK").contains(lookahead.getToken())){
            if (A_CREATEADD("STATEMENT_BLOCK") && match("do") && REPTSTATEMENT() && A_ADOPTS()
                    && match("end")){
                derivationList.set(d, "STATEMENT_BLOCK  -> do REPTSTATEMENT end .\n");
            }else success = false;
        }else if (e.FOLLOW("STATEMENT_BLOCK").contains(lookahead.getToken())){
            if (A_CREATEADD("STATEMENT_BLOCK")){
                derivationList.set(d, "STATEMENT_BLOCK ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ASSIGNMENT_OP(){
//        ASSIGNMENT_OP  -> eq .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("ASSIGNMENT_OP")) return false;
        if (e.FIRST("ASSIGNMENT_OP").contains(lookahead.getToken())){
            if (match("equal")){
                derivationList.set(d, "ASSIGNMENT_OP  -> eq .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS_TAIL(){
//        FUNCTION_PARAMS_TAIL -> #1(param) comma TYPE #2 id #2 ARRAY_DIMENSIONS #2 .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("FUNCTION_PARAMS_TAIL")) return false;
        if (e.FIRST("FUNCTION_PARAMS_TAIL").contains(lookahead.getToken())){
            if (A_CREATEADD("fparam") && match("comma") && TYPE() && A_RIGHTCHILD() && match("id")
                    && A_RIGHTCHILD() && ARRAY_DIMENSIONS() && A_RIGHTCHILD()){
                derivationList.set(d, "FUNCTION_PARAMS_TAIL -> comma TYPE id ARRAY_DIMENSIONS .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean INDICES(){
//        INDICES  -> #1 INDEX #2 INDICES #4 .
//        INDICES  ->  .
        this.derivationNum++;
        int d = derivationNum;
        derivationList.add(derivationNum, "");
        if (!skipErrors("INDICES")) return false; //|| e.isNULLABLE("INDICES")
        if (e.FIRST("INDEX").contains(lookahead.getToken())){
            if (A_CREATEADD("index list") && INDEX() && A_RIGHTCHILD() && INDICES() && A_ADOPTS()){
                derivationList.set(d, "INDICES  -> INDEX INDICES .\n");
            }else success = false;
        }else if (e.FOLLOW("INDICES").contains(lookahead.getToken())){
            if (A_CREATEADD("index list")){
                derivationList.set(d, "INDICES ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean A_CREATEADD(String name){

        switch (name){
            case "program":
                ast.push(new progNode(name));
                return true;
            case "class_list":
                ast.push(new classListNode(name));
                return true;
            case "function_list":
                ast.push(new funcDefListNode(name));
                return true;
            case "var decl":
                ast.push(new varDeclNode(name));
                return true;
            case "func decl":
                ast.push(new funcDeclNode(name));
                return true;
            case "func_def":
                ast.push(new funcDefNode(name));
                return true;
            case "fparam":
                ast.push(new fparamNode(name));
                return true;
            case "FUNCTION_CALL_PARAMS":
                ast.push(new funcCallNode("funcCallParams", ast.peek()));
                return true;

            default: ast.push(new generalNode(name));
            break;
        }
        return true;
    }

    private boolean A_RIGHTCHILD(){
        node y = ast.pop();
        node x = ast.pop();
        x.makeRightChild(y);
        ast.push(x);
        return true;
    }

    private boolean A_LEFTCHILD(){
        node y = ast.pop();
        node x = ast.pop();
        y.makeLeftChild(x);
        ast.push(y);
        return true;
    }

    private boolean A_ADOPTS(){
        node y = ast.pop();
        node x = ast.pop();
        x.adopt(y);
        ast.push(x);
        return true;
    }

    private boolean A_GROUP(){
        node x = ast.pop();
        nodeNum ++;
        node group = new generalNode(x.getData()+"GROUP");
        nodeNum++;
        group.makeRightChild(x);
        while (ast.peek().getData().equals(x.getData())){
            x = ast.pop();
            group.makeRightChild(x);
        }
        ast.push(group);
        return true;
    }

    private boolean A_DELETE(){
        ast.pop();
        return true;
    }

    private boolean A_UNIFY(){
        node x = ast.pop();
        if (x.getChildren().size()==1){
            x = x.getChildren().get(0);
        }
        ast.push(x);
        return true;
    }

}

