package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class parser {

    class Node{
        String name;
        Node child;
        Node parent;

        public Node(String name){
            this.name = name;
        }

        @Override
        public String toString() {
            return "Node{" +"name='" + name + '\'' +'}';
        }
    }

    class error {
        Map<String, List<String>> neff = new HashMap<>();
        final String six_space = "      ";
        final String table = "      START      NO      YES      main class id      $\n" +
                "      PROGRAM      NO      YES      main class id      $\n" +
                "      REPTCLASSDECL      YES      NO      class      main id\n" +
                "      CLASSDECL      NO      NO      class      main class id\n" +
                "      REPTFUNCDEF      YES      NO      id      main\n" +
                "      FUNCDEF      NO      NO      id      main id\n" +
                "      MEMBER_DECLARATIONS      YES      NO      public private      rcurbr\n" +
                "      MEMBER_DECLARATION      NO      NO      id integer float      rcurbr public private\n" +
                "      FUNCTION_OR_VARIABLE_DECLARATION      NO      NO      id lpar      rcurbr public private\n" +
                "      VISIBILITY      NO      NO      public private      id integer float\n" +
                "      STATEMENT      NO      NO      id if while read write return      id semi if else while read write return end\n" +
                "      STATEMENT_VARIABLE      NO      NO      id      rpar\n" +
                "      STATEMENT_VARIABLE_OR_FUNCTION_CALL      YES      NO      lpar dot lsqbr      rpar\n" +
                "      STATEMENT_VARIABLE_EXT      YES      NO      dot      rpar\n" +
                "      STATEMENT_FUNCTION_CALL      NO      NO      dot      rpar\n" +
                "      ASSIGN_STATEMENT_OR_FUNCTION_CALL      NO      NO      id      id semi if else while read write return end\n" +
                "      VARIABLE_OR_FUNCTION_CALL_EXT      NO      NO      lpar dot eq lsqbr      id semi if else while read write return end\n" +
                "      VARIABLE_EXT      NO      NO      dot eq      id semi if else while read write return end\n" +
                "      FUNCTION_CALL_EXT      NO      NO      semi dot      id semi if else while read write return end\n" +
                "      FUNCTION_PARAMS      YES      NO      id integer float      rpar\n" +
                "      ADD_OP      NO      NO      plus minus or      id lpar plus minus integer float not\n" +
                "      OPTCLASSDECL2      YES      NO      inherits      lcurbr\n" +
                "      REL_EXPRESSION      YES      NO      id lpar plus minus integer float not      rpar\n" +
                "      FUNCTION_DECLARATION      NO      NO      lpar      rcurbr public private\n" +
                "      FUNCTION_CALL_PARAMS_TAILS      YES      NO      comma      rpar\n" +
                "      FUNCTION_CALL_PARAM_TAILS      YES      NO      NO      rpar\n" +
                "      FUNCTION_CALL_PARAMS_TAIL      NO      NO      comma      rpar\n" +
                "      FUNCTION_CALL_PARAMS      YES      NO      id lpar plus minus integer float not      rpar\n" +
                "      OPTFUNCBODY0      YES      NO      local      do\n" +
                "      ARRAY_DIMENSIONS      YES      NO      lsqbr      semi rpar comma\n" +
                "      EXPRESSION      NO      NO      id lpar plus minus integer float not      semi rpar comma\n" +
                "      REL_EXPRESSION_OR_NULL      YES      NO      eq neq lt gt leq geq      semi rpar comma\n" +
                "      REPTSTATEMENT      YES      NO      id if while read write return      end\n" +
                "      ARITH_EXPRESSION      NO      NO      id lpar plus minus integer float not      semi rpar comma eq neq lt gt leq geq rsqbr\n" +
                "      RIGHT_REC_ARITH_EXPRESSION      YES      NO      plus minus or      semi rpar comma eq neq lt gt leq geq rsqbr\n" +
                "      FUNCTION_SIGNATURE      NO      NO      id      local do\n" +
                "      FUNCTION_SIGNATURE_NAMESPACE      NO      NO      lpar sr      local do\n" +
                "      FUNCTION_SIGNATURE_EXT      NO      NO      lpar      local do\n" +
                "      FUNCTION_PARAMS_TAILS      YES      NO      comma      rpar\n" +
                "      INHERITED_CLASSES      YES      NO      comma      lcurbr\n" +
                "      SIGN      NO      NO      plus minus      id lpar plus minus integer float not\n" +
                "      COMPARE_OP      NO      NO      eq neq lt gt leq geq      id lpar plus minus integer float not\n" +
                "      INDEX      NO      NO      lsqbr      semi rpar dot plus minus or comma eq neq lt gt leq geq lsqbr rsqbr mult div and\n" +
                "      VARIABLE_DECLARATIONS      YES      NO      id integer float      do\n" +
                "      FACTOR      NO      NO      id lpar plus minus integer float not      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr mult div and\n" +
                "      VARIABLE_FUNCTION_CALL      NO      NO      id      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr mult div and\n" +
                "      VARIABLE_OR_FUNCTION_CALL      YES      NO      lpar dot lsqbr      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr mult div and\n" +
                "      FACTOR_VARIABLE      YES      NO      dot      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr mult div and\n" +
                "      FACTOR_FUNCTION_CALL      YES      NO      dot      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr mult div and\n" +
                "      TERM      NO      NO      id lpar plus minus integer float not      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr\n" +
                "      MULT_OP      NO      NO      mult div and      id lpar plus minus integer float not\n" +
                "      RIGHT_REC_TERM      YES      NO      mult div and      semi rpar plus minus or comma eq neq lt gt leq geq rsqbr\n" +
                "      TYPE_OR_VOID      NO      NO      id integer float void      semi local do\n" +
                "      TYPE      NO      NO      id integer float      id semi local do\n" +
                "      TYPE_NON_ID      NO      NO      integer float      id semi local do\n" +
                "      ARRAY_SIZE      NO      NO      lsqbr      semi rpar comma lsqbr\n" +
                "      OPTIONAL_INT      YES      NO      integer      rsqbr\n" +
                "      VARIABLE_DECLARATION      NO      NO      id      id rcurbr public private integer float do\n" +
                "      FUNCBODY      NO      YES      local do      semi $\n" +
                "      STATEMENT_BLOCK      YES      NO      id if while read write return do      semi else\n" +
                "      ASSIGNMENT_OP      NO      NO      eq      id lpar plus minus integer float not\n" +
                "      FUNCTION_PARAMS_TAIL      NO      NO      comma      rpar comma\n" +
                "      INDICES      YES      NO      lsqbr      semi rpar dot plus minus or comma eq neq lt gt leq geq rsqbr mult div and\n";


        public void createTable() {
            try {
                BufferedReader TSVFile = new BufferedReader(new StringReader(table));
                String dataRow = TSVFile.readLine(); // Read first line.
                while (dataRow != null) {
                    String[] parts = dataRow.split(six_space);
                    String NUllABLE = parts[2];
                    String ENDABLE = parts[3];
                    String FIRST = parts[4];
                    String FOLLOW = parts[5];
                    List<String> values = new ArrayList<String>();
                    values.add(NUllABLE);
                    values.add(ENDABLE);
                    values.add(FIRST);
                    values.add(FOLLOW);
                    this.neff.put(parts[1], values);
                    dataRow = TSVFile.readLine(); // Read next line of data.
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        private boolean isNULLABLE(String LHS) {
            List<String> info = neff.get(LHS);
            String nullable = info.get(0);
            return (nullable.equals("YES"));
        }

        private String ENDABLE(String LHS) {
            List<String> info = neff.get(LHS);
            return info.get(1);
        }

        private String FIRST(String LHS) {
            List<String> info = neff.get(LHS);
            return info.get(2);
        }

        private String FOLLOW(String LHS) {
            List<String> info = neff.get(LHS);
            return info.get(3);
        }
    }

    LinkedList<token> tokens;
    token lookahead;
    error e;
    boolean success;

    private void nextToken(){
        System.out.println(lookahead);
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            lookahead = new token("EPSILON" , new StringBuilder(" "), -1);
        else
            lookahead = tokens.getFirst();
    }

    private boolean match(String token){
        if (lookahead.getToken().equals(token)){
            nextToken();
            return true;
        }else {
            System.out.println("syntax error at "+lookahead.getLinenum()+ "expected: "+token);
            nextToken();
            return false;
        }
    }

    private boolean skipErrors(String LHS){
        if ( (e.FIRST(LHS).contains(lookahead.getToken()))
                || ( (e.isNULLABLE(LHS)) && (e.FOLLOW(LHS).contains(lookahead.getToken())) ) ){
            return true;
        } else {
            System.out.println("syntax error at line "+ lookahead.getLinenum()+" in method "+ LHS);
            while ( (!e.FIRST(LHS).contains(lookahead.getToken()))
                    || (!e.FOLLOW(LHS).contains(lookahead.getToken())) ){
                nextToken();
                if ( (e.isNULLABLE(LHS)) && (e.FOLLOW(LHS).contains(lookahead.getToken())) ){
                    return false;
                }
            }
            return true;
        }
    }

    public parser(LinkedList<token> tokenLinkedList){
        this.tokens = (LinkedList<token>) tokenLinkedList.clone();
        this.lookahead = tokenLinkedList.getFirst();
        this.e = new error();
        this.e.createTable();
        this.success = true;
    }

    public void parse() {

        if (START())
            System.out.println("all good!!!");
//        if (lookahead.getToken() != "EPSILON")
//            throw new ParserException("Parser Exception");
    }

    private boolean START(){
//        START  -> PROGRAM .
        if (!skipErrors("START")) return false;
        if (e.FIRST("PROGRAM").contains(lookahead.getToken())){
            if (PROGRAM()){
                System.out.println("START  -> PROGRAM .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean PROGRAM(){
//        PROGRAM  ->  REPTCLASSDECL REPTFUNCDEF main FUNCBODY .
        if (!skipErrors("PROGRAM")) return false;
        if (e.FIRST("REPTCLASSDECL").contains(lookahead.getToken())){
            if (REPTCLASSDECL() && REPTFUNCDEF() && match("main") && FUNCBODY()){
                System.out.println("PROGRAM  ->  REPTCLASSDECL REPTFUNCDEF main FUNCBODY .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTCLASSDECL(){
//        REPTCLASSDECL -> CLASSDECL  REPTCLASSDECL .
//        REPTCLASSDECL ->  .
        if (!skipErrors("REPTCLASSDECL")) return false;
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken())){
            if (CLASSDECL() && REPTCLASSDECL()){
                System.out.println("REPTCLASSDECL -> CLASSDECL  REPTCLASSDECL .");
            }else success = false;
        }else if (e.isNULLABLE("REPTCLASSDECL")){
            System.out.println("REPTCLASSDECL ->  .");
        }else success = false;
        return success;
    }

    private boolean CLASSDECL(){
//        CLASSDECL  -> class id OPTCLASSDECL2 lcurbr MEMBER_DECLARATIONS rcurbr semi .
        if (!skipErrors("CLASSDECL")) return false;
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken())){
            if (match("class") && match("id") && OPTCLASSDECL2() && match("lcurbr")
                    && MEMBER_DECLARATIONS() && match("rcurbr") && match("semi")){
                System.out.println("CLASSDECL  -> class id OPTCLASSDECL2 lcurbr MEMBER_DECLARATIONS rcurbr semi .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTFUNCDEF(){
//        REPTFUNCDEF -> FUNCDEF REPTFUNCDEF .
//        REPTFUNCDEF ->  .
        if (!skipErrors("REPTFUNCDEF")) return false;
        if (e.FIRST("FUNCDEF").contains(lookahead.getToken())){
            if (FUNCDEF() && REPTFUNCDEF()){
                System.out.println("REPTFUNCDEF -> FUNCDEF REPTFUNCDEF .");
            }else success = false;
        }else if (e.isNULLABLE("REPTFUNCDEF")){
            System.out.println("REPTFUNCDEF ->  .");
        }else success = false;
        return success;
    }

    private boolean FUNCDEF(){
//        FUNCDEF  -> FUNCTION_SIGNATURE FUNCBODY semi .
        if (!skipErrors("FUNCDEF")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE").contains(lookahead.getToken())){
            if (FUNCTION_SIGNATURE() && FUNCBODY() && match("semi")){
                System.out.println("FUNCDEF  -> FUNCTION_SIGNATURE FUNCBODY semi .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean MEMBER_DECLARATIONS(){
//        MEMBER_DECLARATIONS  -> VISIBILITY MEMBER_DECLARATION MEMBER_DECLARATIONS .
//        MEMBER_DECLARATIONS  ->  .
        if (!skipErrors("MEMBER_DECLARATIONS")) return false;
        if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            if (VISIBILITY() && MEMBER_DECLARATION() && MEMBER_DECLARATIONS()){
                System.out.println("MEMBER_DECLARATIONS  -> VISIBILITY MEMBER_DECLARATION MEMBER_DECLARATIONS .");
            }else success = false;
        }else if (e.isNULLABLE("REPTCLASSDECL")){
            System.out.println("REPTCLASSDECL ->  .");
        }else success = false;
        return success;
    }

    private boolean MEMBER_DECLARATION(){
//        MEMBER_DECLARATION -> TYPE_NON_ID VARIABLE_DECLARATION .
//        MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION .
        if (!skipErrors("MEMBER_DECLARATION")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){
            if (TYPE_NON_ID() && VARIABLE_DECLARATION()){
                System.out.println("MEMBER_DECLARATION -> TYPE_NON_ID VARIABLE_DECLARATION .");
            }else success = false;
        }else if (e.FIRST("MEMBER_DECLARATION").contains(lookahead.getToken())){
            if (match("id") && FUNCTION_OR_VARIABLE_DECLARATION()){
                System.out.println("MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION .");
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
                System.out.println("FUNCTION_OR_VARIABLE_DECLARATION -> FUNCTION_DECLARATION .");
            }else success = false;
        }else if (e.FIRST("VARIABLE_DECLARATION").contains(lookahead.getToken())){
            if (VARIABLE_DECLARATION()){
                System.out.println("FUNCTION_OR_VARIABLE_DECLARATION -> VARIABLE_DECLARATION .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VISIBILITY(){
//        VISIBILITY -> public .
//        VISIBILITY -> private .
        if (!skipErrors("VISIBILITY")) return false;
        if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            if (match("public")){
                System.out.println("VISIBILITY -> public .");
            }else success = false;
        }else if (e.FIRST("VISIBILITY").contains(lookahead.getToken())){
            if (match("private")){
                System.out.println("VISIBILITY -> private .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT(){
        if (!skipErrors("STATEMENT")) return false;
//        STATEMENT  -> ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (e.FIRST("ASSIGN_STATEMENT_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                System.out.println("STATEMENT  -> ASSIGN_STATEMENT_OR_FUNCTION_CALL .");
            }else success = false;
        }
//        STATEMENT  -> if lpar REL_EXPRESSION rpar then STATEMENT_BLOCK else STATEMENT_BLOCK semi .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if ( match("if") && match("lpar") && REL_EXPRESSION() && match("rpar")
                    && match("then") && STATEMENT_BLOCK() && match("else") && STATEMENT_BLOCK()
                    && match("semi") ){
                System.out.println("STATEMENT  -> if lpar REL_EXPRESSION rpar then STATEMENT_BLOCK else STATEMENT_BLOCK semi .");
            }
        }
//        STATEMENT  -> while lpar REL_EXPRESSION rpar STATEMENT_BLOCK semi .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if ( match("while") && match("lpar") && REL_EXPRESSION() && match("rpar")
                    && STATEMENT_BLOCK() && match("semi") ){
                System.out.println("STATEMENT  -> while lpar REL_EXPRESSION rpar STATEMENT_BLOCK semi .");
            }
        }
//        STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (match("read") && match("lpar") && STATEMENT_VARIABLE() && match("rpar")
                    && match("semi")){
                System.out.println("STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .");
            }
        }
//        STATEMENT  -> write lpar EXPRESSION rpar semi .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (match("write") && match("lpar") && EXPRESSION() && match("rpar")
                    && match("semi")){
                System.out.println("STATEMENT  -> write lpar EXPRESSION rpar semi .");
            }
        }
//        STATEMENT  -> return lpar EXPRESSION rpar semi .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (match("return") && match("lpar") && EXPRESSION() && match("rpar")
                    && match("semi")){
                System.out.println("STATEMENT  -> return lpar EXPRESSION rpar semi .");
            }
        }
        return success;
    }

    private boolean STATEMENT_VARIABLE(){
//        STATEMENT_VARIABLE -> id STATEMENT_VARIABLE_OR_FUNCTION_CALL .
        if (!skipErrors("STATEMENT_VARIABLE")) return false;
        if (e.FIRST("STATEMENT_VARIABLE").contains(lookahead.getToken())){
            if (match("id") && STATEMENT_VARIABLE_OR_FUNCTION_CALL){
                System.out.println("STATEMENT_VARIABLE -> id STATEMENT_VARIABLE_OR_FUNCTION_CALL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE_OR_FUNCTION_CALL(){
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .
        if (!skipErrors("STATEMENT_VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken())){
            if (INDICES() && STATEMENT_VARIABLE_EXT()){
                System.out.println("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .");
            }else success = false;
        }else if (e.FIRST("STATEMENT_VARIABLE_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && match("rpar") && STATEMENT_FUNCTION_CALL()){
                System.out.println("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .");
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
                System.out.println("STATEMENT_VARIABLE_EXT -> dot STATEMENT_VARIABLE .");
            }else success = false;
        }else if (e.isNULLABLE("STATEMENT_VARIABLE_EXT")){
            System.out.println("STATEMENT_VARIABLE_EXT ->  .");
        }else success = false;
        return success;
    }

    private boolean STATEMENT_FUNCTION_CALL(){
//        STATEMENT_FUNCTION_CALL  -> dot STATEMENT_VARIABLE .
        if (!skipErrors("STATEMENT_FUNCTION_CALL")) return false;
        if (e.FIRST("STATEMENT_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("dot") && STATEMENT_VARIABLE()){
                System.out.println("STATEMENT_FUNCTION_CALL  -> dot STATEMENT_VARIABLE .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean ASSIGN_STATEMENT_OR_FUNCTION_CALL(){
//        ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> id VARIABLE_OR_FUNCTION_CALL_EXT .
        if (!skipErrors("ASSIGN_STATEMENT_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("ASSIGN_STATEMENT_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("if") && VARIABLE_OR_FUNCTION_CALL_EXT()){
                System.out.println("ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> id VARIABLE_OR_FUNCTION_CALL_EXT .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL_EXT(){
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> INDICES VARIABLE_EXT .
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> lpar FUNCTION_CALL_PARAMS rpar FUNCTION_CALL_EXT .
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken())){
            if (INDICES() && VARIABLE_EXT()){
                System.out.println("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .");
            }else success = false;
        }else if (e.FIRST("VARIABLE_OR_FUNCTION_CALL_EXT").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && match("rpar") && FUNCTION_CALL_EXT()){
                System.out.println("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_EXT(){
//        VARIABLE_EXT -> ASSIGNMENT_OP EXPRESSION semi .
//        VARIABLE_EXT -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (!skipErrors("VARIABLE_EXT")) return false;
        if (e.FIRST("ASSIGNMENT_OP").contains(lookahead.getToken())){
            if (ASSIGNMENT_OP() && EXPRESSION() && match("semi")){
                System.out.println("VARIABLE_EXT -> ASSIGNMENT_OP EXPRESSION semi .");
            }else success = false;
        }else if (e.FIRST("VARIABLE_EXT").contains(lookahead.getToken())){
            if (match("dot") && ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                System.out.println("VARIABLE_EXT -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_EXT(){
//        FUNCTION_CALL_EXT  -> semi .
//        FUNCTION_CALL_EXT  -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .
        if (!skipErrors("FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("FUNCTION_CALL_EXT").contains(lookahead.getToken())){
            if (match("semi")){
                System.out.println("FUNCTION_CALL_EXT  -> semi .");
            }else success = false;
        }else if (e.FIRST("FUNCTION_CALL_EXT").contains(lookahead.getToken())){
            if (match("dot") && ASSIGN_STATEMENT_OR_FUNCTION_CALL()){
                System.out.println("FUNCTION_CALL_EXT  -> dot ASSIGN_STATEMENT_OR_FUNCTION_CALL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS(){
//        FUNCTION_PARAMS  -> TYPE id ARRAY_DIMENSIONS FUNCTION_PARAMS_TAILS .
//        FUNCTION_PARAMS  ->  .
        if (!skipErrors("FUNCTION_PARAMS")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (TYPE() && match("id") && ARRAY_DIMENSIONS() && FUNCTION_PARAMS_TAILS()){
                System.out.println("FUNCTION_PARAMS  -> TYPE id ARRAY_DIMENSIONS FUNCTION_PARAMS_TAILS .");
            }else success = false;
        }else if (e.isNULLABLE("FUNCTION_PARAMS")){
            System.out.println("FUNCTION_PARAMS ->  .");
        }else success = false;
        return success;
    }

    private boolean ADD_OP(){
//        ADD_OP -> plus . | minus . | or .
        if (!skipErrors("ADD_OP")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            if (match("plus")){
                System.out.println("ADD_OP -> plus .");
            }else if (match("minus")){
                System.out.println("ADD_OP -> minus .");
            }else if (match("or")){
                System.out.println("ADD_OP -> or .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean OPTCLASSDECL2(){
//        OPTCLASSDECL2  -> inherits id INHERITED_CLASSES .
//        OPTCLASSDECL2  ->  .
        if (!skipErrors("OPTCLASSDECL2")) return false;
        if (e.FIRST("OPTCLASSDECL2").contains(lookahead.getToken())){
            if (match("inherits") && match("id") && INHERITED_CLASSES()){
                System.out.println("OPTCLASSDECL2  -> inherits id INHERITED_CLASSES .");
            }else success = false;
        }else if (e.isNULLABLE("OPTCLASSDECL2")){
            System.out.println("OPTCLASSDECL2 ->  .");
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION(){
//        REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP ARITH_EXPRESSION .
//        REL_EXPRESSION ->  .
        if (!skipErrors("REL_EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION && COMPARE_OP && ARITH_EXPRESSION){
                System.out.println("REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP ARITH_EXPRESSION .");
            }else success = false;
        }else if (e.isNULLABLE("REL_EXPRESSION")){
            System.out.println("REL_EXPRESSION ->  .");
        }else success = false;
        return success;
    }

    private boolean FUNCTION_DECLARATION(){
//        FUNCTION_DECLARATION -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID semi .
        if (!skipErrors("FUNCTION_DECLARATION")) return false;
        if (e.FIRST("FUNCTION_DECLARATION").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_PARAMS() && match() && match("rpar") && TYPE_OR_VOID()
                    && match("semi")){
                System.out.println("FUNCTION_DECLARATION -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID semi .");
            }else success = false;
        }else success = false;
        return success;
    }

    //CHANGE AFTER THIS ONE

    private boolean FUNCTION_CALL_PARAMS_TAILS(){
//        FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAMS_TAILS .
//        FUNCTION_CALL_PARAMS_TAILS ->  .
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (FUNCTION_CALL_PARAMS_TAIL() && FUNCTION_CALL_PARAMS_TAILS()){
                System.out.println("FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAMS_TAILS .");
            }else success = false;
        }else if (e.isNULLABLE("FUNCTION_CALL_PARAMS_TAILS")){
            System.out.println("FUNCTION_CALL_PARAMS_TAILS ->  .");
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS_TAIL(){
//        FUNCTION_CALL_PARAMS_TAIL  -> comma EXPRESSION .
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAIL")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (match("comma") && EXPRESSION() ){
                System.out.println("FUNCTION_CALL_PARAMS_TAIL  -> comma EXPRESSION .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS(){
//        FUNCTION_CALL_PARAMS -> EXPRESSION FUNCTION_CALL_PARAMS_TAILS .
//        FUNCTION_CALL_PARAMS ->  .
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (FUNCTION_CALL_PARAMS_TAIL() && FUNCTION_CALL_PARAM_TAILS()){
                System.out.println("FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAM_TAILS .");
            }else success = false;
        }else if (e.isNULLABLE("FUNCTION_CALL_PARAMS_TAILS")){
            System.out.println("FUNCTION_CALL_PARAMS_TAILS ->  .");
        }else success = false;
        return success;
    }

    private boolean OPTFUNCBODY0(){
//        OPTFUNCBODY0  -> local VARIABLE_DECLARATIONS .
//        OPTFUNCBODY0  ->  .
        if (!skipErrors("OPTFUNCBODY0")) return false;
        if (e.FIRST("OPTFUNCBODY0").contains(lookahead.getToken())){
            if (match("local") && VARIABLE_DECLARATIONS()){
                System.out.println("OPTFUNCBODY0  -> local VARIABLE_DECLARATIONS .");
            }else success = false;
        }else if (e.isNULLABLE("OPTFUNCBODY0")){
            System.out.println("OPTFUNCBODY0 ->  .");
        }else success = false;
        return success;
    }

    private boolean ARRAY_DIMENSIONS(){
//        ARRAY_DIMENSIONS -> ARRAY_SIZE ARRAY_DIMENSIONS .
//        ARRAY_DIMENSIONS ->  .
        if (!skipErrors("ARRAY_DIMENSIONS")) return false;
        if (e.FIRST("ARRAY_SIZE").contains(lookahead.getToken())){
            if (ARRAY_SIZE() && ARRAY_DIMENSIONS()){
                System.out.println("ARRAY_DIMENSIONS -> ARRAY_SIZE ARRAY_DIMENSIONS .");
            }else success = false;
        }else if (e.isNULLABLE("ARRAY_DIMENSIONS")){
            System.out.println("ARRAY_DIMENSIONS ->  .");
        }else success = false;
        return success;
    }

    private boolean EXPRESSION(){
//        EXPRESSION -> ARITH_EXPRESSION REL_EXPRESSION_OR_NULL .
        if (!skipErrors("EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION() && REL_EXPRESSION_OR_NULL()){
                System.out.println("EXPRESSION -> ARITH_EXPRESSION REL_EXPRESSION_OR_NULL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION_OR_NULL(){
//        REL_EXPRESSION_OR_NULL -> COMPARE_OP ARITH_EXPRESSION .
//        REL_EXPRESSION_OR_NULL ->  .
        if (!skipErrors("REL_EXPRESSION_OR_NULL")) return false;
        if (e.FIRST("COMPARE_OP").contains(lookahead.getToken())){
            if (COMPARE_OP() && ARITH_EXPRESSION()){
                System.out.println("REL_EXPRESSION_OR_NULL -> COMPARE_OP ARITH_EXPRESSION .");
            }else success = false;
        }else if (e.isNULLABLE("REL_EXPRESSION_OR_NULL")){
            System.out.println("REL_EXPRESSION_OR_NULL ->  .");
        }else success = false;
        return success;
    }

    private boolean REPTSTATEMENT(){
//        REPTSTATEMENT -> STATEMENT REPTSTATEMENT .
//        REPTSTATEMENT ->  .
        if (!skipErrors("REPTSTATEMENT")) return false;
        if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (STATEMENT() && REPTSTATEMENT()){
                System.out.println("REPTSTATEMENT -> STATEMENT REPTSTATEMENT .");
            }else success = false;
        }else if (e.isNULLABLE("REPTSTATEMENT")){
            System.out.println("REPTSTATEMENT ->  .");
        }else success = false;
        return success;
    }


}

