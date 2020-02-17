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
            if (match("id") && FUNCTION_OR_VARIABLE_DECLARATION())
            System.out.println("MEMBER_DECLARATION -> id FUNCTION_OR_VARIABLE_DECLARATION .");
        }else success = false;
        return success;
    }

}

