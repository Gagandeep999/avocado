package parser;

import lexer.token;
import nodes.node;

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
    Stack<String> derivationStack;
    PrintWriter pwDerivation;
    PrintWriter pwSyntaxError;

    public parse(LinkedList<token> tokenLinkedList, String outputLocation){
        this.tokens = (LinkedList<token>) tokenLinkedList.clone();
        this.lookahead = tokenLinkedList.getFirst();
        this.e = new error();
        this.e.createTable();
        this.success = true;
        this.nodeNum = 0;
        this.derivationStack = new Stack<>();
        this.ast = new Stack<>();


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
                case "id" : A_CREATEADD(lookahead);
                    break;
                case "class" : A_CREATEADD(lookahead);
                    break;
                case "inherits" : A_CREATEADD(lookahead);
                    break;
                case "public" : A_CREATEADD(lookahead);
                    break;
                case "private" : A_CREATEADD(lookahead);
                    break;
                case "if" : A_CREATEADD(lookahead);
                    break;
                case "while" : A_CREATEADD(lookahead);
                    break;
                case "read" : A_CREATEADD(lookahead);
                    break;
                case "write" : A_CREATEADD(lookahead);
                    break;
                case "return" : A_CREATEADD(lookahead);
                    break;
                case "plus" : A_CREATEADD(lookahead);
                    break;
                case "minus" : A_CREATEADD(lookahead);
                    break;
                case "or" : A_CREATEADD(lookahead);
                    break;
                case "eq" : A_CREATEADD(lookahead);
                    break;
                case "neq" : A_CREATEADD(lookahead);
                    break;
                case "lessthan" : A_CREATEADD(lookahead);
                    break;
                case "gt" : A_CREATEADD(lookahead);
                    break;
                case "leq" : A_CREATEADD(lookahead);
                    break;
                case "geq" : A_CREATEADD(lookahead);
                    break;
                case "mult" : A_CREATEADD(lookahead);
                    break;
                case "div" : A_CREATEADD(lookahead);
                    break;
                case "and" : A_CREATEADD(lookahead);
                    break;
                case "integer" : A_CREATEADD(lookahead);
                    break;
                case "float" : A_CREATEADD(lookahead);
                    break;
                case "not" : A_CREATEADD(lookahead);
                    break;
                case "main" : A_CREATEADD(lookahead);
                    break;
                case "void" : A_CREATEADD(lookahead);
                    break;
                case "intnum" : A_CREATEADD(lookahead);
                    break;
                case "floatnum" : A_CREATEADD(lookahead);
                    break;
                case "equal" : A_CREATEADD(lookahead);
                    break;
//                case "integer" : A_CREATEADD(lookahead);
//                    break;
//                case "integer" : A_CREATEADD(lookahead);
//                    break;
//                case "integer" : A_CREATEADD(lookahead);
//                    break;
//                case "integer" : A_CREATEADD(lookahead);
//                    break;
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
            derivationStack.push("Sucessfuly parsed the source code!\n");
//            pwDerivation.write("Sucessfuly parsed the source code!");

        while ( !derivationStack.isEmpty()){
            String derv = derivationStack.pop();
            pwDerivation.write(derv);
        }
        pwSyntaxError.flush();
        pwDerivation.flush();
        pwSyntaxError.close();
        pwSyntaxError.close();

        return success;
    }

    private boolean START(){
//        START  -> PROGRAM .
        if (!skipErrors("START")) return false;
        if (e.FIRST("PROGRAM").contains(lookahead.getToken())){
            if (PROGRAM()){
                derivationStack.push("START  -> PROGRAM .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean PROGRAM(){
//        PROGRAM -> #1 REPTCLASSDECL #2 REPTFUNCDEF #2 main FUNCBODY #2 #2 .
        if (!skipErrors("PROGRAM")) return false;
        if (e.FIRST("REPTCLASSDECL").contains(lookahead.getToken()) || e.isNULLABLE("REPTCLASSDECL")){
            if (A_CREATEADD("program") && REPTCLASSDECL() && A_RIGHTCHILD() && REPTFUNCDEF()
                    && A_RIGHTCHILD() && match("main") && FUNCBODY() && A_RIGHTCHILD() && A_RIGHTCHILD()){ //&& A_RIGHTCHILD()
                derivationStack.push("PROGRAM  ->  REPTCLASSDECL REPTFUNCDEF main FUNCBODY .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTCLASSDECL(){
//        REPTCLASSDECL -> #1 CLASSDECL #2 REPTCLASSDECL #4 .
//        REPTCLASSDECL ->  .
        if (!skipErrors("REPTCLASSDECL")) return false;
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken()) ){
            if (A_CREATEADD("class_list") && CLASSDECL() && A_RIGHTCHILD()
                    && REPTCLASSDECL() && A_ADOPTS()){
                derivationStack.push("REPTCLASSDECL -> CLASSDECL  REPTCLASSDECL .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTCLASSDECL").contains(lookahead.getToken())){
            if(A_CREATEADD("class_list")){
                derivationStack.push("REPTCLASSDECL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean CLASSDECL(){
//        CLASSDECL -> class id #2 OPTCLASSDECL2 #2 lcurbr MEMBER_DECLARATIONS #2 rcurbr semi  .
        if (!skipErrors("CLASSDECL")) return false;
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken())){
            if (match("class") && match("id") && A_RIGHTCHILD()
                    && OPTCLASSDECL2() && A_RIGHTCHILD() && match("lcurbr")
                    && MEMBER_DECLARATIONS() && A_RIGHTCHILD() && match("rcurbr") && match("semi")){
                derivationStack.push("CLASSDECL  -> class id OPTCLASSDECL2 lcurbr MEMBER_DECLARATIONS rcurbr semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTFUNCDEF(){
//        REPTFUNCDEF -> #1 FUNCDEF #2 REPTFUNCDEF #4  .
//        REPTFUNCDEF ->  .
        if (!skipErrors("REPTFUNCDEF")) return false;
        if (e.FIRST("FUNCDEF").contains(lookahead.getToken()) ){
            if (A_CREATEADD("function_list") && FUNCDEF() && A_RIGHTCHILD()
                    && REPTFUNCDEF() && A_ADOPTS()){
                derivationStack.push("REPTFUNCDEF -> FUNCDEF REPTFUNCDEF .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTFUNCDEF").contains(lookahead.getToken())){
            if(A_CREATEADD("function_list")){
                derivationStack.push("REPTFUNCDEF ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCDEF(){
//        FUNCDEF  -> #1(function) FUNCTION_SIGNATURE #2 FUNCBODY #2 .
        if (!skipErrors("FUNCDEF")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE").contains(lookahead.getToken())){
            if (A_CREATEADD("func_def") && FUNCTION_SIGNATURE() && A_RIGHTCHILD()
                    && FUNCBODY() && A_RIGHTCHILD()){
                derivationStack.push("FUNCDEF  -> FUNCTION_SIGNATURE FUNCBODY semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean MEMBER_DECLARATIONS(){
//        MEMBER_DECLARATIONS  -> #1 VISIBILITY MEMBER_DECLARATION #3 #2 MEMBER_DECLARATIONS #4 .
//        MEMBER_DECLARATIONS  ->  .
        if (!skipErrors("MEMBER_DECLARATIONS")) return false;
        if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            if (A_CREATEADD("var+func decl list") && VISIBILITY() && MEMBER_DECLARATION() && A_LEFTCHILD()
                    && A_RIGHTCHILD() && MEMBER_DECLARATIONS() && A_ADOPTS()){
                derivationStack.push("MEMBER_DECLARATIONS  -> VISIBILITY MEMBER_DECLARATION MEMBER_DECLARATIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTCLASSDECL").contains(lookahead.getToken()) || e.isNULLABLE("REPTCLASSDECL") ){ //
            if (A_CREATEADD("var+func decl list")){
                derivationStack.push("REPTCLASSDECL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean MEMBER_DECLARATION(){
//        MEMBER_DECLARATION -> TYPE_NON_ID VARIABLE_DECLARATION #3 .
//        MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION #3 .
        if (!skipErrors("MEMBER_DECLARATION")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){
            if (TYPE_NON_ID() && VARIABLE_DECLARATION() && A_LEFTCHILD()){
                derivationStack.push("MEMBER_DECLARATION -> TYPE_NON_ID VARIABLE_DECLARATION .\n");
            }else success = false;
        }else if (e.FIRST("MEMBER_DECLARATION").contains(lookahead.getToken())){
            if (match("id") && FUNCTION_OR_VARIABLE_DECLARATION() && A_LEFTCHILD()){
                derivationStack.push("MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION .\n");
            }else  success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_OR_VARIABLE_DECLARATION(){
//        FUNCTION_OR_VARIABLE_DECLARATION -> FUNCTION_DECLARATION .
//        FUNCTION_OR_VARIABLE_DECLARATION -> VARIABLE_DECLARATION .
        if (!skipErrors("FUNCTION_OR_VARIABLE_DECLARATION")) return false;
        if (e.FIRST("FUNCTION_DECLARATION").contains(lookahead.getToken())){
            if (FUNCTION_DECLARATION()){
                derivationStack.push("FUNCTION_OR_VARIABLE_DECLARATION -> FUNCTION_DECLARATION .\n");
            }else success = false;
        }else if (e.FIRST("VARIABLE_DECLARATION").contains(lookahead.getToken())){
            if (VARIABLE_DECLARATION()){
                derivationStack.push("FUNCTION_OR_VARIABLE_DECLARATION -> VARIABLE_DECLARATION .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VISIBILITY(){
//        VISIBILITY -> public .
//        VISIBILITY -> private .
        if (!skipErrors("VISIBILITY")) return false;
        if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            switch (lookahead.getToken()){
                case "public" : match("public");
                    derivationStack.push("VISIBILITY -> public .\n");
                    break;
                case "private" : match("private");
                    derivationStack.push("VISIBILITY -> private .\n");
                    break;
                default: success = false;
                break;
            }
        }else success = false;
        return success;
    }

    private boolean STATEMENT(){
        if (!skipErrors("STATEMENT")) return false;
//        STATEMENT  -> ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (e.FIRST("ASSIGN_STATEMENT_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                derivationStack.push("STATEMENT  -> ASSIGN_STATEMENT_OR_FUNCTION_CALL .\n");
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
                    derivationStack.push("STATEMENT  -> if lpar REL_EXPRESSION rpar then STATEMENT_BLOCK else STATEMENT_BLOCK semi .\n");
                }break;
                case "while" : if ( match("while") && match("lpar") && REL_EXPRESSION() && A_RIGHTCHILD()
                        && match("rpar") && STATEMENT_BLOCK() && A_RIGHTCHILD() && match("semi") ){
                    derivationStack.push("STATEMENT  -> while lpar REL_EXPRESSION rpar STATEMENT_BLOCK semi .\n");
                }break;
                case "read" : if (match("read") && match("lpar") && STATEMENT_VARIABLE() && A_DELETE()
                        && A_GROUP() && A_RIGHTCHILD()  && match("rpar") && match("semi")){
                    derivationStack.push("STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .\n");
                }break;
                case "write" : if (match("write") && match("lpar") && EXPRESSION()
                        && A_RIGHTCHILD() && match("rpar") && match("semi")){
                    derivationStack.push("STATEMENT  -> write lpar EXPRESSION rpar semi .\n");
                }break;
                case "return" : if (match("return") && match("lpar") && EXPRESSION()
                        && A_RIGHTCHILD() && match("rpar") && match("semi")){
                    derivationStack.push("STATEMENT  -> return lpar EXPRESSION rpar semi .\n");
                }break;
                default: success = false;
                break;
            }

        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE(){
//        STATEMENT_VARIABLE -> #1(item) id #2 STATEMENT_VARIABLE_OR_FUNCTION_CALL .
        if (!skipErrors("STATEMENT_VARIABLE")) return false;
        if (e.FIRST("STATEMENT_VARIABLE").contains(lookahead.getToken())){
            if (A_CREATEADD("call") && match("id") && A_RIGHTCHILD()
                    && STATEMENT_VARIABLE_OR_FUNCTION_CALL()){
                derivationStack.push("STATEMENT_VARIABLE -> id STATEMENT_VARIABLE_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE_OR_FUNCTION_CALL(){
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES #2 STATEMENT_VARIABLE_EXT .
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS #2 rpar STATEMENT_FUNCTION_CALL .
        if (!skipErrors("STATEMENT_VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.isNULLABLE("INDICES")){ //
            if (INDICES() && A_RIGHTCHILD() && STATEMENT_VARIABLE_EXT()){
                derivationStack.push("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .\n");
            }else success = false;
        }else if (e.FIRST("STATEMENT_VARIABLE_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && A_RIGHTCHILD() && match("rpar")
                    && STATEMENT_FUNCTION_CALL()){
                derivationStack.push("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE_EXT(){
//        STATEMENT_VARIABLE_EXT -> dot STATEMENT_VARIABLE .
//        STATEMENT_VARIABLE_EXT ->  .
        if (!skipErrors("STATEMENT_VARIABLE_EXT")) return false;
        if (e.FIRST("STATEMENT_VARIABLE_EXT").contains(lookahead.getToken())){
            if (match("dot") && STATEMENT_VARIABLE()){
                derivationStack.push("STATEMENT_VARIABLE_EXT -> dot STATEMENT_VARIABLE .\n");
            }else success = false;
        }else if (e.FOLLOW("STATEMENT_VARIABLE_EXT").contains(lookahead.getToken())){
            if (A_CREATEADD("STATEMENT_VARIABLE_EXT")){
                derivationStack.push("STATEMENT_VARIABLE_EXT ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean STATEMENT_FUNCTION_CALL(){
//        STATEMENT_FUNCTION_CALL  -> dot STATEMENT_VARIABLE .
        if (!skipErrors("STATEMENT_FUNCTION_CALL")) return false;
        if (e.FIRST("STATEMENT_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("dot") && STATEMENT_VARIABLE()){
                derivationStack.push("STATEMENT_FUNCTION_CALL  -> dot STATEMENT_VARIABLE .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean ASSIGN_STATEMENT_OR_FUNCTION_CALL(){
//        ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> #1(item) id #2 VARIABLE_OR_FUNCTION_CALL_EXT .
        if (!skipErrors("ASSIGN_STATEMENT_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("ASSIGN_STATEMENT_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (A_CREATEADD("call") && match("id") && A_RIGHTCHILD() && VARIABLE_OR_FUNCTION_CALL_EXT()){
                derivationStack.push("ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> id VARIABLE_OR_FUNCTION_CALL_EXT .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL_EXT(){
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> INDICES #2 VARIABLE_EXT .
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> lpar FUNCTION_CALL_PARAMS #2 rpar FUNCTION_CALL_EXT .
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.FIRST("VARIABLE_EXT").contains(lookahead.getToken())){ // || e.isNULLABLE("INDICES") || e.FOLLOW("INDICES").contains(lookahead.getToken())
            if (INDICES() && A_RIGHTCHILD() && VARIABLE_EXT()){
                derivationStack.push("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .\n");
            }else success = false;
        }else if (lookahead.getToken().equals("lpar")){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && A_RIGHTCHILD()
                    && match("rpar") && FUNCTION_CALL_EXT()){
                derivationStack.push("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_EXT(){
//        VARIABLE_EXT -> #5 ASSIGNMENT_OP #3 EXPRESSION #2 semi .
//        VARIABLE_EXT -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (!skipErrors("VARIABLE_EXT")) return false;
        if (e.FIRST("ASSIGNMENT_OP").contains(lookahead.getToken())){
            if (A_GROUP() && ASSIGNMENT_OP() && A_LEFTCHILD() && EXPRESSION() && A_RIGHTCHILD()
                    && match("semi")){
                derivationStack.push("VARIABLE_EXT -> ASSIGNMENT_OP EXPRESSION semi .\n");
            }else success = false;
        }else if (e.FIRST("VARIABLE_EXT").contains(lookahead.getToken())){
            if (match("dot") && ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                derivationStack.push("VARIABLE_EXT -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_EXT(){
//        FUNCTION_CALL_EXT  -> FUNCTION_CALL_EXT -> #5 #1(func_call) #3 semi .
//        FUNCTION_CALL_EXT  -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (!skipErrors("FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("FUNCTION_CALL_EXT").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("semi")){
                if (A_GROUP() && A_CREATEADD("FUNC_CALL") && A_LEFTCHILD() && match("semi")){
                    derivationStack.push("FUNCTION_CALL_EXT  -> semi .\n");
                }
            }else if (lookahead.getToken().equals("dot"))
                if (match("dot") && ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                derivationStack.push("FUNCTION_CALL_EXT  -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS(){
//        FUNCTION_PARAMS  -> #1 #1(param) TYPE #2 id #2 ARRAY_DIMENSIONS #2 #2 FUNCTION_PARAMS_TAILS #4 .
//        FUNCTION_PARAMS  ->  .
        if (!skipErrors("FUNCTION_PARAMS")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (A_CREATEADD("fParam list") && A_CREATEADD("fparam") && TYPE() && A_RIGHTCHILD()
                    && match("id") && A_RIGHTCHILD() && ARRAY_DIMENSIONS() && A_RIGHTCHILD() && A_RIGHTCHILD()
                    && FUNCTION_PARAMS_TAILS() && A_ADOPTS()){
                derivationStack.push("FUNCTION_PARAMS  -> TYPE id ARRAY_DIMENSIONS FUNCTION_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_PARAMS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_PARAMS")){
                derivationStack.push("FUNCTION_PARAMS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ADD_OP(){
//        ADD_OP -> plus . | minus . | or .
        if (!skipErrors("ADD_OP")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            switch (lookahead.getToken()){
                case "plus" : match("plus");
                    derivationStack.push("ADD_OP -> plus .\n");
                    break;
                case "minus" : match("minus");
                    derivationStack.push("ADD_OP -> minus .\n");
                    break;
                case "or" : match("or");
                    derivationStack.push("ADD_OP -> or .\n");
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
        if (!skipErrors("OPTCLASSDECL2")) return false;
        if (e.FIRST("OPTCLASSDECL2").contains(lookahead.getToken())){
            if (match("inherits") && match("id") && A_RIGHTCHILD() && INHERITED_CLASSES() && A_ADOPTS()){
                derivationStack.push("OPTCLASSDECL2  -> inherits id INHERITED_CLASSES .\n");
            }else success = false;
        }else if (e.FOLLOW("OPTCLASSDECL2").contains(lookahead.getToken())){
            if (A_CREATEADD("inheritance list")){
                derivationStack.push("OPTCLASSDECL2 ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION(){
//        REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP #3 ARITH_EXPRESSION #2 .
//        REL_EXPRESSION ->  .
        if (!skipErrors("REL_EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION() && COMPARE_OP() && A_LEFTCHILD() && ARITH_EXPRESSION() && A_RIGHTCHILD()){
                derivationStack.push("REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP ARITH_EXPRESSION .\n");
            }else success = false;
        }else if (e.FOLLOW("REL_EXPRESSION").contains(lookahead.getToken())){
            if (A_CREATEADD("REL_EXPRESSION")){
                derivationStack.push("REL_EXPRESSION ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCTION_DECLARATION(){
//        FUNCTION_DECLARATION -> #1 lpar FUNCTION_PARAMS #2 rpar colon TYPE_OR_VOID #2 semi  .
        if (!skipErrors("FUNCTION_DECLARATION")) return false;
        if (e.FIRST("FUNCTION_DECLARATION").contains(lookahead.getToken())){
            if (A_CREATEADD("func decl") && match("lpar") && FUNCTION_PARAMS()
                    && A_RIGHTCHILD() && match("rpar") && match("colon") && TYPE_OR_VOID()
                    && A_RIGHTCHILD() && match("semi")){
                derivationStack.push("FUNCTION_DECLARATION -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS_TAILS(){
//        FUNCTION_CALL_PARAMS_TAILS -> #1 FUNCTION_CALL_PARAMS_TAIL #2 FUNCTION_CALL_PARAMS_TAILS #4 .
//        FUNCTION_CALL_PARAMS_TAILS ->  .
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS_TAILS") && FUNCTION_CALL_PARAMS_TAIL()
                    && A_RIGHTCHILD() && FUNCTION_CALL_PARAMS_TAILS() && A_ADOPTS()){
                derivationStack.push("FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_CALL_PARAMS_TAILS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS_TAILS")){
                derivationStack.push("FUNCTION_CALL_PARAMS_TAILS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS_TAIL(){
//        FUNCTION_CALL_PARAMS_TAIL  -> comma EXPRESSION .
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAIL")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (match("comma") && EXPRESSION() ){
                derivationStack.push("FUNCTION_CALL_PARAMS_TAIL  -> comma EXPRESSION .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS(){
//        FUNCTION_CALL_PARAMS -> #1 EXPRESSION #2 FUNCTION_CALL_PARAMS_TAILS #4 .
//        FUNCTION_CALL_PARAMS ->  .
        if (!skipErrors("FUNCTION_CALL_PARAMS")) return false;
        if (e.FIRST("EXPRESSION").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS") && EXPRESSION() && A_RIGHTCHILD()
                    && FUNCTION_CALL_PARAMS_TAILS() && A_ADOPTS()){
                derivationStack.push("FUNCTION_CALL_PARAMS -> EXPRESSION FUNCTION_CALL_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_CALL_PARAMS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_CALL_PARAMS")){
                derivationStack.push("FUNCTION_CALL_PARAMS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean OPTFUNCBODY0(){
//        OPTFUNCBODY0  -> local VARIABLE_DECLARATIONS .
//        OPTFUNCBODY0  ->  .
        if (!skipErrors("OPTFUNCBODY0")) return false;
        if (e.FIRST("OPTFUNCBODY0").contains(lookahead.getToken())){
            if (match("local") && VARIABLE_DECLARATIONS()){
                derivationStack.push("OPTFUNCBODY0  -> local VARIABLE_DECLARATIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("OPTFUNCBODY0").contains(lookahead.getToken())){
            if (A_CREATEADD("OPTFUNCBODY0")){
                derivationStack.push("OPTFUNCBODY0 ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ARRAY_DIMENSIONS(){
//        ARRAY_DIMENSIONS -> #1 ARRAY_SIZE #2 ARRAY_DIMENSIONS #4 .
//        ARRAY_DIMENSIONS ->  .
        if (!skipErrors("ARRAY_DIMENSIONS")) return false;
        if (e.FIRST("ARRAY_SIZE").contains(lookahead.getToken())){
            if (A_CREATEADD("dim list") && ARRAY_SIZE() && A_RIGHTCHILD()
                    && ARRAY_DIMENSIONS() && A_ADOPTS()){
                derivationStack.push("ARRAY_DIMENSIONS -> ARRAY_SIZE ARRAY_DIMENSIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("ARRAY_DIMENSIONS").contains(lookahead.getToken())){
            if (A_CREATEADD("dim list")){
                derivationStack.push("ARRAY_DIMENSIONS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean EXPRESSION(){
//        EXPRESSION -> ARITH_EXPRESSION  REL_EXPRESSION_OR_NULL #3 #7 .
        if (!skipErrors("EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION() && REL_EXPRESSION_OR_NULL() && A_LEFTCHILD() && A_UNIFY()){
                derivationStack.push("EXPRESSION -> ARITH_EXPRESSION REL_EXPRESSION_OR_NULL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION_OR_NULL(){
//        REL_EXPRESSION_OR_NULL -> COMPARE_OP ARITH_EXPRESSION #2 .
//        REL_EXPRESSION_OR_NULL ->  .
        if (!skipErrors("REL_EXPRESSION_OR_NULL")) return false;
        if (e.FIRST("COMPARE_OP").contains(lookahead.getToken())){
            if (COMPARE_OP() && ARITH_EXPRESSION() && A_RIGHTCHILD()){
                derivationStack.push("REL_EXPRESSION_OR_NULL -> COMPARE_OP ARITH_EXPRESSION .\n");
            }else success = false;
        }else if (e.FOLLOW("REL_EXPRESSION_OR_NULL").contains(lookahead.getToken())){
            if (A_CREATEADD("REL_EXPRESSION_OR_NULL")){
                derivationStack.push("REL_EXPRESSION_OR_NULL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean REPTSTATEMENT(){
//        REPTSTATEMENT -> #1 STATEMENT #2 REPTSTATEMENT #4 .
//        REPTSTATEMENT ->  .
        if (!skipErrors("REPTSTATEMENT")) return false;
        if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (A_CREATEADD("REPTSTATEMENT") && STATEMENT() && A_RIGHTCHILD()
                    && REPTSTATEMENT() && A_ADOPTS()){
                derivationStack.push("REPTSTATEMENT -> STATEMENT REPTSTATEMENT .\n");
            }else success = false;
        }else if (e.FOLLOW("REPTSTATEMENT").contains(lookahead.getToken())){
            if (A_CREATEADD("REPTSTATEMENT")){
                derivationStack.push("REPTSTATEMENT ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ARITH_EXPRESSION(){
//        ARITH_EXPRESSION -> TERM RIGHT_REC_ARITH_EXPRESSION #6 .
        if (!skipErrors("ARITH_EXPRESSION")) return false;
        if (e.FIRST("TERM").contains(lookahead.getToken())){
            if (TERM() && RIGHT_REC_ARITH_EXPRESSION() && A_DELETE()){
                derivationStack.push("ARITH_EXPRESSION -> TERM RIGHT_REC_ARITH_EXPRESSION .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean RIGHT_REC_ARITH_EXPRESSION(){
//        RIGHT_REC_ARITH_EXPRESSION -> ADD_OP #3 TERM #2 RIGHT_REC_ARITH_EXPRESSION .
//        RIGHT_REC_ARITH_EXPRESSION ->  .
        if (!skipErrors("RIGHT_REC_ARITH_EXPRESSION")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            if (ADD_OP() && A_LEFTCHILD() && TERM() && A_RIGHTCHILD() && RIGHT_REC_ARITH_EXPRESSION()){
                derivationStack.push("RIGHT_REC_ARITH_EXPRESSION -> ADD_OP TERM RIGHT_REC_ARITH_EXPRESSION .\n");
            }else success = false;
        }else if (e.FOLLOW("RIGHT_REC_ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (A_CREATEADD("RIGHT_REC_ARITH_EXPRESSION")){
                derivationStack.push("RIGHT_REC_ARITH_EXPRESSION ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE(){
//        FUNCTION_SIGNATURE -> #1 id FUNCTION_SIGNATURE_NAMESPACE #4 .
        if (!skipErrors("FUNCTION_SIGNATURE")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_SIGNATURE") && match("id") && FUNCTION_SIGNATURE_NAMESPACE()
                    && A_ADOPTS()){
                derivationStack.push("FUNCTION_SIGNATURE -> id FUNCTION_SIGNATURE_NAMESPACE .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE_NAMESPACE(){
//        FUNCTION_SIGNATURE_NAMESPACE -> #2 FUNCTION_SIGNATURE_EXT .
//        FUNCTION_SIGNATURE_NAMESPACE -> #1(namespace) #3 #2 coloncolon id #2 FUNCTION_SIGNATURE_EXT .
        if (!skipErrors("FUNCTION_SIGNATURE_NAMESPACE")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE_EXT").contains(lookahead.getToken())){
            if (A_RIGHTCHILD() && FUNCTION_SIGNATURE_EXT()){
                derivationStack.push("FUNCTION_SIGNATURE_NAMESPACE -> FUNCTION_SIGNATURE_EXT .\n");
            }else success = false;
        }else if (e.FIRST("FUNCTION_SIGNATURE_NAMESPACE").contains(lookahead.getToken())){
            if (A_CREATEADD("NAMESPACE") && A_LEFTCHILD() && A_RIGHTCHILD() && match("sr")
                    && match("id") && A_RIGHTCHILD() && FUNCTION_SIGNATURE_EXT())
            derivationStack.push("FUNCTION_SIGNATURE_NAMESPACE -> sr id FUNCTION_SIGNATURE_EXT .\n");
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE_EXT(){
//        FUNCTION_SIGNATURE_EXT -> #1 lpar FUNCTION_PARAMS #2 rpar colon #1(returns) TYPE_OR_VOID #2 #2 .
        if (!skipErrors("FUNCTION_SIGNATURE_EXT")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE_EXT").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_SIGNATURE_EXT") && match("lpar") && FUNCTION_PARAMS()
                    && A_RIGHTCHILD() && match("rpar") && match("colon") //&& A_CREATEADD("RETURNS")
                    && TYPE_OR_VOID() && A_RIGHTCHILD() ){ //&& A_RIGHTCHILD()){
                derivationStack.push("FUNCTION_SIGNATURE_EXT -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS_TAILS(){
//        FUNCTION_PARAMS_TAILS  -> #1 FUNCTION_PARAMS_TAIL #2 FUNCTION_PARAMS_TAILS #4 .
//        FUNCTION_PARAMS_TAILS  ->  .
        if (!skipErrors("FUNCTION_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_PARAMS_TAIL").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_PARAMS_TAILS") && FUNCTION_PARAMS_TAIL() && A_RIGHTCHILD()
                    && FUNCTION_PARAMS_TAILS() && A_ADOPTS()){
                derivationStack.push("FUNCTION_PARAMS_TAILS  -> FUNCTION_PARAMS_TAIL FUNCTION_PARAMS_TAILS .\n");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_PARAMS_TAILS").contains(lookahead.getToken())){
            if (A_CREATEADD("FUNCTION_PARAMS_TAILS")){
                derivationStack.push("FUNCTION_PARAMS_TAILS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean INHERITED_CLASSES(){
//        INHERITED_CLASSES  -> #1 comma id #2 INHERITED_CLASSES #4 .
//        INHERITED_CLASSES  ->  .
        if (!skipErrors("INHERITED_CLASSES")) return false;
        if (e.FIRST("INHERITED_CLASSES").contains(lookahead.getToken())){
            if (A_CREATEADD("INHERITED_CLASSES") && match("comma") && match("id") && A_RIGHTCHILD()
                    && INHERITED_CLASSES() && A_ADOPTS()){
                derivationStack.push("INHERITED_CLASSES  -> comma id INHERITED_CLASSES .\n");
            }else success = false;
        }else if (e.FOLLOW("INHERITED_CLASSES").contains(lookahead.getToken())){
            if (A_CREATEADD("INHERITED_CLASSES")){
                derivationStack.push("INHERITED_CLASSES ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean SIGN(){
//        SIGN -> plus . | minus .
        if (!skipErrors("SIGN")) return false;
        if (e.FIRST("SIGN").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("plus")){
                if (match("plus")){
                    derivationStack.push("SIGN -> plus .\n");
                }
            }else if (lookahead.getToken().equals("minus")){
                if (match("minus")){
                derivationStack.push("SIGN -> minus .\n");
            }else success = false;
            }
        }else success = false;
        return success;
    }

    private boolean COMPARE_OP(){
//        COMPARE_OP -> eq . | neq . | lt . | gt . | leq . | geq .
        if (!skipErrors("COMPARE_OP")) return false;
        if (e.FIRST("COMPARE_OP").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "eq" : match("eq");
                    derivationStack.push("SIGN -> eq .\n");
                    break;
                case "neq" : match("neq");
                    derivationStack.push("SIGN -> neq .\n");
                    break;
                case "lessthan" : match("lessthan");
                    derivationStack.push("SIGN -> lessthan .\n");
                    break;
                case "gt" : match("gt");
                    derivationStack.push("SIGN -> gt .\n");
                    break;
                case "leq" : match("leq");
                    derivationStack.push("SIGN -> leq .\n");
                    break;
                case "geq" : match("geq");
                    derivationStack.push("SIGN -> geq .\n");
                    break;
                default: success = false;
                    break;
            }
        }else success = false;
        return success;
    }

    private boolean INDEX(){
//        INDEX  -> lsqbr ARITH_EXPRESSION rsqbr .
        if (!skipErrors("INDEX")) return false;
        if (e.FIRST("INDEX").contains(lookahead.getToken())){
            if (match("lsqbr") && ARITH_EXPRESSION() && match("rsqbr")){
                derivationStack.push("INDEX  -> lsqbr ARITH_EXPRESSION rsqbr .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_DECLARATIONS(){
//        VARIABLE_DECLARATIONS  -> #1 TYPE VARIABLE_DECLARATION #3 #2 VARIABLE_DECLARATIONS #4 .
//        VARIABLE_DECLARATIONS  ->  .
        if (!skipErrors("VARIABLE_DECLARATIONS")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (A_CREATEADD("VARIABLE_DECLARATIONS") && TYPE() && VARIABLE_DECLARATION() && A_LEFTCHILD()
                    && A_RIGHTCHILD() && VARIABLE_DECLARATIONS() && A_ADOPTS()){
                derivationStack.push("VARIABLE_DECLARATIONS  -> TYPE VARIABLE_DECLARATION VARIABLE_DECLARATIONS .\n");
            }else success = false;
        }else if (e.FOLLOW("VARIABLE_DECLARATIONS").contains(lookahead.getToken())){
            if (A_CREATEADD("VARIABLE_DECLARATIONS")){
                derivationStack.push("VARIABLE_DECLARATIONS ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FACTOR(){
        if (!skipErrors("FACTOR")) return false;
//        FACTOR -> VARIABLE_FUNCTION_CALL #6 #5 .
        if (e.FIRST("VARIABLE_FUNCTION_CALL").contains(lookahead.getToken())){
            if (VARIABLE_FUNCTION_CALL() && A_DELETE() && A_GROUP()){
                derivationStack.push("FACTOR -> VARIABLE_FUNCTION_CALL .\n");
            }else success = false;
        }
//        FACTOR -> #1 SIGN #2 FACTOR #2 .
        else if (e.FIRST("SIGN").contains(lookahead.getToken())){
            if (A_CREATEADD("FACTOR") && SIGN() && A_RIGHTCHILD() && FACTOR() && A_RIGHTCHILD()){
                derivationStack.push("FACTOR -> SIGN FACTOR .\n");
            }else success = false;
        }
//        FACTOR -> lpar ARITH_EXPRESSION rpar .
//        FACTOR -> #1 not #2 FACTOR #2 .
//        FACTOR -> integer .
//        FACTOR -> float .
        else if (e.FIRST("FACTOR").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "lpar" : if (match("lpar") && ARITH_EXPRESSION() && match("rpar")) {
                    derivationStack.push("FACTOR -> lpar ARITH_EXPRESSION rpar .\n");
                }break;
                case "not" : if (A_CREATEADD("FACTOR") && match("not") && A_RIGHTCHILD()
                        && FACTOR() && A_RIGHTCHILD()){
                    derivationStack.push("FACTOR -> not FACTOR .\n");
                }break;
                case "intnum" : if (match("intnum")){
                    derivationStack.push("FACTOR -> intnum .\n");
                }break;
                case "floatnum" : if (match("floatnum")){
                    derivationStack.push("FACTOR -> floatnum .\n");
                }break;
                default: success = false;
                break;
            }
        }else success = false;
        return success;
    }

    private boolean VARIABLE_FUNCTION_CALL(){
//        VARIABLE_FUNCTION_CALL -> #1(item) id #2 VARIABLE_OR_FUNCTION_CALL .
        if (!skipErrors("VARIABLE_FUNCTION_CALL")) return false;
        if (e.FIRST("VARIABLE_FUNCTION_CALL").contains(lookahead.getToken())){
            if (A_CREATEADD("ITEM") && match("id") && A_RIGHTCHILD() && VARIABLE_OR_FUNCTION_CALL()){
                derivationStack.push("VARIABLE_FUNCTION_CALL -> id VARIABLE_OR_FUNCTION_CALL .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL(){
//        VARIABLE_OR_FUNCTION_CALL  -> INDICES #2 FACTOR_VARIABLE .
//        VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS #2 rpar FACTOR_FUNCTION_CALL .
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.FOLLOW("INDICES").contains(lookahead.getToken()) ){ //
            if (INDICES() && A_RIGHTCHILD() && FACTOR_VARIABLE()){
                derivationStack.push("VARIABLE_OR_FUNCTION_CALL  -> INDICES FACTOR_VARIABLE .\n");
            }else success = false;
        }else if (e.FIRST("VARIABLE_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && A_RIGHTCHILD() && match("rpar")
                && FACTOR_FUNCTION_CALL())
                derivationStack.push("VARIABLE_OR_FUNCTION_CALL -> lpar FUNCTION_CALL_PARAMS rpar FACTOR_FUNCTION_CALL .\n");
        }else success = false;
        return success;
    }

    private boolean FACTOR_VARIABLE(){
//        FACTOR_VARIABLE  -> dot VARIABLE_FUNCTION_CALL .
//        FACTOR_VARIABLE  ->  .
        if (!skipErrors("FACTOR_VARIABLE")) return false;
        if (e.FIRST("FACTOR_VARIABLE").contains(lookahead.getToken())){ // || e.isNULLABLE("FACTOR_VARIABLE")
            if (match("dot") && VARIABLE_FUNCTION_CALL()){
                derivationStack.push("FACTOR_VARIABLE  -> dot VARIABLE_FUNCTION_CALL .\n");
            }else success = false;
        }else if (e.FOLLOW("FACTOR_VARIABLE").contains(lookahead.getToken())){
            if (A_CREATEADD("FACTOR_VARIABLE")){
                derivationStack.push("FACTOR_VARIABLE ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean FACTOR_FUNCTION_CALL(){
//        FACTOR_FUNCTION_CALL -> dot VARIABLE_FUNCTION_CALL .
//        FACTOR_FUNCTION_CALL ->  .
        if (!skipErrors("FACTOR_FUNCTION_CALL")) return false;
        if (e.FIRST("FACTOR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("dot") && VARIABLE_FUNCTION_CALL()){
                derivationStack.push("FACTOR_FUNCTION_CALL  -> dot VARIABLE_FUNCTION_CALL .\n");
            }else success = false;
        }else if (e.FOLLOW("FACTOR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (A_CREATEADD("FACTOR_FUNCTION_CALL")){
                derivationStack.push("FACTOR_FUNCTION_CALL ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TERM(){
//        TERM -> FACTOR RIGHT_REC_TERM #6 .
        if (!skipErrors("TERM")) return false;
        if (e.FIRST("FACTOR").contains(lookahead.getToken())){
            if (FACTOR() && RIGHT_REC_TERM() &&  A_DELETE()){
                derivationStack.push("TERM -> FACTOR RIGHT_REC_TERM .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean MULT_OP(){
//        MULT_OP  -> mult . | div . | and .
        if (!skipErrors("MULT_OP")) return false;
        if (e.FIRST("MULT_OP").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "mult" : match("mult");
                    derivationStack.push("MULT_OP -> mult .\n");
                    break;
                case "div" : match("div");
                    derivationStack.push("MULT_OP -> div .\n");
                    break;
                case "and" : match("and");
                    derivationStack.push("MULT_OP -> and .\n");
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
        if (!skipErrors("RIGHT_REC_TERM")) return false;
        if (e.FIRST("MULT_OP").contains(lookahead.getToken())){
            if (MULT_OP() && A_LEFTCHILD() && FACTOR() && A_RIGHTCHILD() && RIGHT_REC_TERM()){
                derivationStack.push("RIGHT_REC_TERM -> MULT_OP FACTOR RIGHT_REC_TERM .\n");
            }else success = false;
        }else if (e.FOLLOW("RIGHT_REC_TERM").contains(lookahead.getToken())){
            if (A_CREATEADD("RIGHT_REC_TERM")){
                derivationStack.push("RIGHT_REC_TERM ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE_OR_VOID(){
//        TYPE_OR_VOID -> TYPE .
//        TYPE_OR_VOID -> void .
        if (!skipErrors("TYPE_OR_VOID")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (TYPE()){
                derivationStack.push("TYPE_OR_VOID -> TYPE .\n");
            }else success = false;
        }else if (e.FIRST("TYPE_OR_VOID").contains(lookahead.getToken())){
            if (match("void")){
                derivationStack.push("TYPE_OR_VOID -> void .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE(){
//        TYPE -> TYPE_NON_ID .
//        TYPE -> id .
        if (!skipErrors("TYPE")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){
            if (TYPE_NON_ID()){
                derivationStack.push("TYPE -> TYPE_NON_ID .\n");
            }else success = false;
        }else if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (match("id")){
                derivationStack.push("TYPE -> id .\n");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE_NON_ID(){
//        TYPE_NON_ID  -> integer .
//        TYPE_NON_ID  -> float .
        if (!skipErrors("TYPE_NON_ID")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){

            switch (lookahead.getToken()){
                case "integer" : match("integer");
                    derivationStack.push("TYPE_NON_ID  -> integer .\n");
                    break;
                case "float" : match("float");
                    derivationStack.push("TYPE_NON_ID  -> float .\n");
                    break;
                default: success = false;
                    break;
            }
        }else success = false;
        return success;
    }

    private boolean ARRAY_SIZE(){
//        ARRAY_SIZE -> #1 lsqbr OPTIONAL_INT #2 rsqbr .
        if (!skipErrors("ARRAY_SIZE")) return false;
        if (e.FIRST("ARRAY_SIZE").contains(lookahead.getToken())){
            if (A_CREATEADD("ARRAY_SIZE") && match("lsqbr") && OPTIONAL_INT() && A_RIGHTCHILD()
                    && match("rsqbr")){
                derivationStack.push("TERM -> FACTOR RIGHT_REC_TERM .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean OPTIONAL_INT(){
//        OPTIONAL_INT -> #1 intnum #2 .
//        OPTIONAL_INT ->  .
        if (!skipErrors("OPTIONAL_INT")) return false;
        if (e.FIRST("OPTIONAL_INT").contains(lookahead.getToken())){
            if (A_CREATEADD("OPTIONAL_INT") && match("intnum") && A_RIGHTCHILD()){
                derivationStack.push("OPTIONAL_INT -> intnum .\n");
            }else success = false;
        }else if (e.FOLLOW("OPTIONAL_INT").contains(lookahead.getToken())){
            if (A_CREATEADD("OPTIONAL_INT")){
                derivationStack.push("OPTIONAL_INT ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean VARIABLE_DECLARATION(){
//        VARIABLE_DECLARATION -> #1(var) id #2 ARRAY_DIMENSIONS #2 semi  .
        if (!skipErrors("VARIABLE_DECLARATION")) return false;
        if (e.FIRST("VARIABLE_DECLARATION").contains(lookahead.getToken())){
            if (A_CREATEADD("var decl") && match("id") && A_RIGHTCHILD() && ARRAY_DIMENSIONS()
                    && A_RIGHTCHILD() && match("semi")){
                derivationStack.push("VARIABLE_DECLARATION -> id ARRAY_DIMENSIONS semi .\n");
            }else success = false;
        }else success = false;
        return success;
    }


    private boolean FUNCBODY(){

//        FUNCBODY  ->  #1 OPTFUNCBODY0 #2 do REPTSTATEMENT #2 end  .
        if (!skipErrors("FUNCBODY")) return false;
        if (e.FIRST("OPTFUNCBODY0").contains(lookahead.getToken()) || e.isNULLABLE("OPTFUNCBODY0")){
//            @TODO: 2020-03-07 check what is the effect of taking out A_RIGHTCHILD() here
            if (A_CREATEADD("FUNCBODY") && OPTFUNCBODY0() && A_RIGHTCHILD() && match("do") //&& A_RIGHTCHILD()
                    && REPTSTATEMENT() && A_RIGHTCHILD() && match("end")){
                derivationStack.push("FUNCBODY  -> OPTFUNCBODY0 do REPTSTATEMENT end .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_BLOCK(){
//        STATEMENT_BLOCK  -> #1 STATEMENT #2 .
//        STATEMENT_BLOCK  -> #1 do REPTSTATEMENT #4 end  .
//        STATEMENT_BLOCK  ->  .
        if (!skipErrors("STATEMENT_BLOCK")) return false;
        if (e.FIRST("STATEMENT").contains(lookahead.getToken())) {
            if (A_CREATEADD("STATEMENT_BLOCK") && STATEMENT() && A_RIGHTCHILD()) {
                derivationStack.push("STATEMENT_BLOCK  -> STATEMENT .\n");
            } else success = false;
        }else if (e.FIRST("STATEMENT_BLOCK").contains(lookahead.getToken())){
            if (A_CREATEADD("STATEMENT_BLOCK") && match("do") && REPTSTATEMENT() && A_ADOPTS()
                    && match("end")){
                derivationStack.push("STATEMENT_BLOCK  -> do REPTSTATEMENT end .\n");
            }else success = false;
        }else if (e.FOLLOW("STATEMENT_BLOCK").contains(lookahead.getToken())){
            if (A_CREATEADD("STATEMENT_BLOCK")){
                derivationStack.push("STATEMENT_BLOCK ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean ASSIGNMENT_OP(){
//        ASSIGNMENT_OP  -> eq .
        if (!skipErrors("ASSIGNMENT_OP")) return false;
        if (e.FIRST("ASSIGNMENT_OP").contains(lookahead.getToken())){
            if (match("equal")){
                derivationStack.push("ASSIGNMENT_OP  -> eq .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS_TAIL(){
//        FUNCTION_PARAMS_TAIL -> #1(param) comma TYPE #2 id #2 ARRAY_DIMENSIONS #2 .
        if (!skipErrors("FUNCTION_PARAMS_TAIL")) return false;
        if (e.FIRST("FUNCTION_PARAMS_TAIL").contains(lookahead.getToken())){
            if (A_CREATEADD("fparam") && match("comma") && TYPE() && A_RIGHTCHILD() && match("id")
                    && A_RIGHTCHILD() && ARRAY_DIMENSIONS() && A_RIGHTCHILD()){
                derivationStack.push("FUNCTION_PARAMS_TAIL -> comma TYPE id ARRAY_DIMENSIONS .\n");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean INDICES(){
//        INDICES  -> #1 INDEX #2 INDICES #4 .
//        INDICES  ->  .
        if (!skipErrors("INDICES")) return false; //|| e.isNULLABLE("INDICES")
        if (e.FIRST("INDEX").contains(lookahead.getToken())){
            if (A_CREATEADD("index list") && INDEX() && A_RIGHTCHILD() && INDICES() && A_ADOPTS()){
                derivationStack.push("INDICES  -> INDEX INDICES .\n");
            }else success = false;
        }else if (e.FOLLOW("INDICES").contains(lookahead.getToken())){
            if (A_CREATEADD("index list")){
                derivationStack.push("INDICES ->  .\n");
            }
        }else success = false;
        return success;
    }

    private boolean A_CREATEADD(String name){
        node x = new node(name, nodeNum);
        ast.push(x);
        nodeNum ++;
        return true;
    }

    private boolean A_CREATEADD(token t){
        node x = new node(t, nodeNum);
        ast.push(x);
        nodeNum ++;
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
        node group = new node((x.getName()+"GROUP"), nodeNum);
        nodeNum++;
        group.makeRightChild(x);
        while (ast.peek().getName().equals(x.getName())){
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
            x = x.getChildren().pop();
        }
        ast.push(x);
        return true;
    }

}

