package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class parser {

    class Node{
        String name;
        String type;
        Node child;
        Node parent;
        List<Node> children;

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
                "      VARIABLE_OR_FUNCTION_CALL_EXT      NO      NO      lpar dot lsqbr equal      id semi if else while read write return end\n" +
                "      VARIABLE_EXT      NO      NO      dot equal      id semi if else while read write return end\n" +
                "      FUNCTION_CALL_EXT      NO      NO      semi dot      id semi if else while read write return end\n" +
                "      FUNCTION_PARAMS      YES      NO      id integer float      rpar\n" +
                "      ADD_OP      NO      NO      plus minus or      id lpar plus minus not intnum floatnum\n" +
                "      OPTCLASSDECL2      YES      NO      inherits      lcurbr\n" +
                "      REL_EXPRESSION      YES      NO      id lpar plus minus not intnum floatnum      rpar\n" +
                "      FUNCTION_DECLARATION      NO      NO      lpar      rcurbr public private\n" +
                "      FUNCTION_CALL_PARAMS_TAILS      YES      NO      comma      rpar\n" +
                "      FUNCTION_CALL_PARAMS_TAIL      NO      NO      comma      rpar comma\n" +
                "      FUNCTION_CALL_PARAMS      YES      NO      id lpar plus minus not intnum floatnum      rpar\n" +
                "      OPTFUNCBODY0      YES      NO      local      do\n" +
                "      ARRAY_DIMENSIONS      YES      NO      lsqbr      semi rpar comma\n" +
                "      EXPRESSION      NO      NO      id lpar plus minus not intnum floatnum      semi rpar comma\n" +
                "      REL_EXPRESSION_OR_NULL      YES      NO      eq neq lessthan gt leq geq      semi rpar comma\n" +
                "      REPTSTATEMENT      YES      NO      id if while read write return      end\n" +
                "      ARITH_EXPRESSION      NO      NO      id lpar plus minus not intnum floatnum      semi rpar comma eq neq lessthan gt leq geq rsqbr\n" +
                "      RIGHT_REC_ARITH_EXPRESSION      YES      NO      plus minus or      semi rpar comma eq neq lessthan gt leq geq rsqbr\n" +
                "      FUNCTION_SIGNATURE      NO      NO      id      local do\n" +
                "      FUNCTION_SIGNATURE_NAMESPACE      NO      NO      lpar sr      local do\n" +
                "      FUNCTION_SIGNATURE_EXT      NO      NO      lpar      local do\n" +
                "      FUNCTION_PARAMS_TAILS      YES      NO      comma      rpar\n" +
                "      INHERITED_CLASSES      YES      NO      comma      lcurbr\n" +
                "      SIGN      NO      NO      plus minus      id lpar plus minus not intnum floatnum\n" +
                "      COMPARE_OP      NO      NO      eq neq lessthan gt leq geq      id lpar plus minus not intnum floatnum\n" +
                "      INDEX      NO      NO      lsqbr      semi rpar dot plus minus or comma eq neq lessthan gt leq geq lsqbr rsqbr mult div and equal\n" +
                "      VARIABLE_DECLARATIONS      YES      NO      id integer float      do\n" +
                "      FACTOR      NO      NO      id lpar plus minus not intnum floatnum      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
                "      VARIABLE_FUNCTION_CALL      NO      NO      id      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
                "      VARIABLE_OR_FUNCTION_CALL      YES      NO      lpar dot lsqbr      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
                "      FACTOR_VARIABLE      YES      NO      dot      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
                "      FACTOR_FUNCTION_CALL      YES      NO      dot      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
                "      TERM      NO      NO      id lpar plus minus not intnum floatnum      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr\n" +
                "      MULT_OP      NO      NO      mult div and      id lpar plus minus not intnum floatnum\n" +
                "      RIGHT_REC_TERM      YES      NO      mult div and      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr\n" +
                "      TYPE_OR_VOID      NO      NO      id void integer float      semi local do\n" +
                "      TYPE      NO      NO      id integer float      id semi local do\n" +
                "      TYPE_NON_ID      NO      NO      integer float      id semi local do\n" +
                "      ARRAY_SIZE      NO      NO      lsqbr      semi rpar comma lsqbr\n" +
                "      OPTIONAL_INT      YES      NO      intnum      rsqbr\n" +
                "      VARIABLE_DECLARATION      NO      NO      id      id rcurbr public private integer float do\n" +
                "      FUNCBODY      NO      YES      local do      semi $\n" +
                "      STATEMENT_BLOCK      YES      NO      id if while read write return do      semi else\n" +
                "      ASSIGNMENT_OP      NO      NO      equal      id lpar plus minus not intnum floatnum\n" +
                "      FUNCTION_PARAMS_TAIL      NO      NO      comma      rpar comma\n" +
                "      INDICES      YES      NO      lsqbr      semi rpar dot plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and equal\n";


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

    private LinkedList<token> tokens;
    private token lookahead;
    private error e;
    private boolean success;

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
            //evertime the token matches put it in a stack
            nextToken();
            return true;
        }else {
            System.out.println("SYNTAX ERROR IN LINE NO: "+lookahead.getLinenum()+ " EXPECTED: "+token+" GOT "+lookahead.getToken());
            nextToken();
            return false;
        }
    }

    private boolean skipErrors(String LHS){
        if ( (e.FIRST(LHS).contains(lookahead.getToken()))
                || ( (e.isNULLABLE(LHS)) && (e.FOLLOW(LHS).contains(lookahead.getToken())) ) ){
            return true;
        } else {
            System.out.println("SYNTAX ERROR IN LINE NO: "+lookahead.getLinenum()+ " EXPECTED: "+lookahead.getToken()+"" +
                    " IN METHOD: "+LHS);
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
        //have a stack as the member which will contain nodes
        this.tokens = (LinkedList<token>) tokenLinkedList.clone();
        this.lookahead = tokenLinkedList.getFirst();
        this.e = new error();
        this.e.createTable();
        this.success = true;
    }

    public boolean parse() {

        //create the AST node and push it in the stack

        if (START())
            System.out.println("");
//        if (lookahead.getToken() != "EPSILON")
//            throw new ParserException("Parser Exception");
        return success;
    }

    private boolean START(){
//        START  -> PROGRAM .
        if (!skipErrors("START")) return false;
        //if no errors deteced then pop the node from the stack ASTNode StartNode = pop from stack
        if (e.FIRST("PROGRAM").contains(lookahead.getToken())){
            //new ProgNode for PROGRAM() and push it in stack
            if (PROGRAM() /* && ADD2PARENT() */){
                System.out.println("START  -> PROGRAM .");
                //StartNode = pop from stack
                //push StartNode to stack which goes back to start method
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean PROGRAM(){
//        PROGRAM  ->  REPTCLASSDECL REPTFUNCDEF main FUNCBODY .
        if (!skipErrors("PROGRAM")) return false;
        //pop ProgNode from stack
        if (e.FIRST("REPTCLASSDECL").contains(lookahead.getToken()) || e.isNULLABLE("REPTCLASSDECL")){
            //create nodes ClassListNode() ; FuncDefNode() ; StatBlockNode()
            //push all three to stack -> StatBlockNode() -> FuncDefNode() -> ReptClassNode()
            if (REPTCLASSDECL() /*MAKE SIBLING()*/ && REPTFUNCDEF() /*MAKE SIBLING()*/
                    && match("main") && FUNCBODY() /*MAKE SIBLING*/ /* */){
                // ProgNode = pop from stack
                // ProgNode back to stack so that it is send up the tree
                System.out.println("PROGRAM  ->  REPTCLASSDECL REPTFUNCDEF main FUNCBODY .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTCLASSDECL(){
//        REPTCLASSDECL -> CLASSDECL  REPTCLASSDECL .
//        REPTCLASSDECL ->  .
        if (!skipErrors("REPTCLASSDECL")) return false;
        //pop ClassListNode from the stack
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken()) ){
            //create a ClassDeclNode() that contains id,inheritlist,memberlist
            if (CLASSDECL() && /* add sibing */ REPTCLASSDECL()){
                //ClassListNode = pop from stack
                //push back to stack to send it up
                System.out.println("REPTCLASSDECL -> CLASSDECL  REPTCLASSDECL .");
            }else success = false;
        }else if (e.FOLLOW("REPTCLASSDECL").contains(lookahead.getToken())){
            //here make all the classes that are sibling of each other; the child of
            System.out.println("REPTCLASSDECL ->  .");
        }else success = false;
        return success;
    }

    private boolean CLASSDECL(){
//        CLASSDECL  -> class id OPTCLASSDECL2 lcurbr MEMBER_DECLARATIONS rcurbr semi .
        if (!skipErrors("CLASSDECL")) return false;
        //pop the ClassDeclNode node
        if (e.FIRST("CLASSDECL").contains(lookahead.getToken())){
            if (match("class") && match("id") && OPTCLASSDECL2() && match("lcurbr")
                    && MEMBER_DECLARATIONS() && match("rcurbr") && match("semi")){
                    //at this point class is at bottom of tokenStack() then is "id" then maybe id inherits
                    //then is "lcurbr" then all member declrations then rcurbr then semi.
                //HOW TO PARSE THIS THING TO CREATE TREE???
                System.out.println("CLASSDECL  -> class id OPTCLASSDECL2 lcurbr MEMBER_DECLARATIONS rcurbr semi .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean REPTFUNCDEF(){
//        REPTFUNCDEF -> FUNCDEF REPTFUNCDEF .
//        REPTFUNCDEF ->  .
        if (!skipErrors("REPTFUNCDEF")) return false;
        if (e.FIRST("FUNCDEF").contains(lookahead.getToken()) ){
            if (FUNCDEF() && REPTFUNCDEF()){
                System.out.println("REPTFUNCDEF -> FUNCDEF REPTFUNCDEF .");
            }else success = false;
        }else if (e.FOLLOW("REPTFUNCDEF").contains(lookahead.getToken())){
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
        }else if (e.FOLLOW("REPTCLASSDECL").contains(lookahead.getToken()) || e.isNULLABLE("REPTCLASSDECL") ){ //
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
            if (lookahead.getToken().equals("public")){
                if (match("public")) {
                    System.out.println("VISIBILITY -> public .");
                }
            }else if (lookahead.getToken().equals("private")) {
                if (match("private")) {
                    System.out.println("VISIBILITY -> private .");
                }
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
//        STATEMENT  -> while lpar REL_EXPRESSION rpar STATEMENT_BLOCK semi .
//        STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .
//        STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .
//        STATEMENT  -> write lpar EXPRESSION rpar semi .
//        STATEMENT  -> return lpar EXPRESSION rpar semi .
        else if (e.FIRST("STATEMENT").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("if")){
                if ( match("if") && match("lpar") && REL_EXPRESSION() && match("rpar")
                        && match("then") && STATEMENT_BLOCK() && match("else") && STATEMENT_BLOCK()
                        && match("semi") ){
                    System.out.println("STATEMENT  -> if lpar REL_EXPRESSION rpar then STATEMENT_BLOCK else STATEMENT_BLOCK semi .");
                }
            }else if (lookahead.getToken().equals("while")){
                if ( match("while") && match("lpar") && REL_EXPRESSION() && match("rpar")
                        && STATEMENT_BLOCK() && match("semi") ){
                    System.out.println("STATEMENT  -> while lpar REL_EXPRESSION rpar STATEMENT_BLOCK semi .");
                }
            }else if (lookahead.getToken().equals("read")){
                if (match("read") && match("lpar") && STATEMENT_VARIABLE() && match("rpar")
                        && match("semi")){
                    System.out.println("STATEMENT  -> read lpar STATEMENT_VARIABLE rpar semi .");
                }
            }else if (lookahead.getToken().equals("write")){
                if (match("write") && match("lpar") && EXPRESSION() && match("rpar")
                        && match("semi")){
                    System.out.println("STATEMENT  -> write lpar EXPRESSION rpar semi .");
                }
            }else if (lookahead.getToken().equals("return")){
                if (match("return") && match("lpar") && EXPRESSION() && match("rpar")
                        && match("semi")){
                    System.out.println("STATEMENT  -> return lpar EXPRESSION rpar semi .");
                }
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE(){
//        STATEMENT_VARIABLE -> id STATEMENT_VARIABLE_OR_FUNCTION_CALL .
        if (!skipErrors("STATEMENT_VARIABLE")) return false;
        if (e.FIRST("STATEMENT_VARIABLE").contains(lookahead.getToken())){
            if (match("id") && STATEMENT_VARIABLE_OR_FUNCTION_CALL()){
                System.out.println("STATEMENT_VARIABLE -> id STATEMENT_VARIABLE_OR_FUNCTION_CALL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_VARIABLE_OR_FUNCTION_CALL(){
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .
//        STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar STATEMENT_FUNCTION_CALL .
        if (!skipErrors("STATEMENT_VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) ){ //|| e.isNULLABLE("INDICES")
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
        }else if (e.FOLLOW("STATEMENT_VARIABLE_EXT").contains(lookahead.getToken())){
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
            if (match("id") && VARIABLE_OR_FUNCTION_CALL_EXT()){
                System.out.println("ASSIGN_STATEMENT_OR_FUNCTION_CALL  -> id VARIABLE_OR_FUNCTION_CALL_EXT .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL_EXT(){
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> INDICES VARIABLE_EXT .
//        VARIABLE_OR_FUNCTION_CALL_EXT  -> lpar FUNCTION_CALL_PARAMS rpar FUNCTION_CALL_EXT .
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL_EXT")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.FIRST("VARIABLE_EXT").contains(lookahead.getToken())){ // || e.isNULLABLE("INDICES") || e.FOLLOW("INDICES").contains(lookahead.getToken())
            if (INDICES() && VARIABLE_EXT()){
                System.out.println("STATEMENT_VARIABLE_OR_FUNCTION_CALL  -> INDICES STATEMENT_VARIABLE_EXT .");
            }else success = false;
        }else if (lookahead.getToken().equals("lpar")){
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
            if (lookahead.getToken().equals("semi")){
                if (match("semi")){
                    System.out.println("FUNCTION_CALL_EXT  -> semi .");
                }
            }else if (lookahead.getToken().equals("dot"))
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
        }else if (e.FOLLOW("FUNCTION_PARAMS").contains(lookahead.getToken())){
            System.out.println("FUNCTION_PARAMS ->  .");
        }else success = false;
        return success;
    }

    private boolean ADD_OP(){
//        ADD_OP -> plus . | minus . | or .
        if (!skipErrors("ADD_OP")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("plus")){
                if (match("plus")) {
                    System.out.println("ADD_OP -> plus .");
                }
            }else if (lookahead.getToken().equals("minus")){
                if (match("minus")) {
                    System.out.println("ADD_OP -> minus .");
                }
            }else if (lookahead.getToken().equals("or")){
                if (match("or")) {
                    System.out.println("ADD_OP -> or .");
                }
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
        }else if (e.FOLLOW("OPTCLASSDECL2").contains(lookahead.getToken())){
            System.out.println("OPTCLASSDECL2 ->  .");
        }else success = false;
        return success;
    }

    private boolean REL_EXPRESSION(){
//        REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP ARITH_EXPRESSION .
//        REL_EXPRESSION ->  .
        if (!skipErrors("REL_EXPRESSION")) return false;
        if (e.FIRST("ARITH_EXPRESSION").contains(lookahead.getToken())){
            if (ARITH_EXPRESSION() && COMPARE_OP() && ARITH_EXPRESSION()){
                System.out.println("REL_EXPRESSION -> ARITH_EXPRESSION COMPARE_OP ARITH_EXPRESSION .");
            }else success = false;
        }else if (e.FOLLOW("REL_EXPRESSION").contains(lookahead.getToken())){
            System.out.println("REL_EXPRESSION ->  .");
        }else success = false;
        return success;
    }

    private boolean FUNCTION_DECLARATION(){
//        FUNCTION_DECLARATION -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID semi .
        if (!skipErrors("FUNCTION_DECLARATION")) return false;
        if (e.FIRST("FUNCTION_DECLARATION").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_PARAMS() && match("rpar") && match("colon") && TYPE_OR_VOID()
                    && match("semi")){
                System.out.println("FUNCTION_DECLARATION -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID semi .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_CALL_PARAMS_TAILS(){
//        FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAMS_TAILS .
//        FUNCTION_CALL_PARAMS_TAILS ->  .
        if (!skipErrors("FUNCTION_CALL_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_CALL_PARAMS_TAIL").contains(lookahead.getToken())){
            if (FUNCTION_CALL_PARAMS_TAIL() && FUNCTION_CALL_PARAMS_TAILS()){
                System.out.println("FUNCTION_CALL_PARAMS_TAILS -> FUNCTION_CALL_PARAMS_TAIL FUNCTION_CALL_PARAMS_TAILS .");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_CALL_PARAMS_TAILS").contains(lookahead.getToken())){
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
        if (!skipErrors("FUNCTION_CALL_PARAMS")) return false;
        if (e.FIRST("EXPRESSION").contains(lookahead.getToken())){
            if (EXPRESSION() && FUNCTION_CALL_PARAMS_TAILS()){
                System.out.println("FUNCTION_CALL_PARAMS -> EXPRESSION FUNCTION_CALL_PARAMS_TAILS .");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_CALL_PARAMS").contains(lookahead.getToken())){
            System.out.println("FUNCTION_CALL_PARAMS ->  .");
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
        }else if (e.FOLLOW("OPTFUNCBODY0").contains(lookahead.getToken())){
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
        }else if (e.FOLLOW("ARRAY_DIMENSIONS").contains(lookahead.getToken())){
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
        }else if (e.FOLLOW("REL_EXPRESSION_OR_NULL").contains(lookahead.getToken())){
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
        }else if (e.FOLLOW("REPTSTATEMENT").contains(lookahead.getToken())){
            System.out.println("REPTSTATEMENT ->  .");
        }else success = false;
        return success;
    }

    private boolean ARITH_EXPRESSION(){
//        ARITH_EXPRESSION -> TERM RIGHT_REC_ARITH_EXPRESSION .
        if (!skipErrors("ARITH_EXPRESSION")) return false;
        if (e.FIRST("TERM").contains(lookahead.getToken())){
            if (TERM() && RIGHT_REC_ARITH_EXPRESSION()){
                System.out.println("ARITH_EXPRESSION -> TERM RIGHT_REC_ARITH_EXPRESSION .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean RIGHT_REC_ARITH_EXPRESSION(){
//        RIGHT_REC_ARITH_EXPRESSION -> ADD_OP TERM RIGHT_REC_ARITH_EXPRESSION .
//        RIGHT_REC_ARITH_EXPRESSION ->  .
        if (!skipErrors("RIGHT_REC_ARITH_EXPRESSION")) return false;
        if (e.FIRST("ADD_OP").contains(lookahead.getToken())){
            if (ADD_OP() && TERM() && RIGHT_REC_ARITH_EXPRESSION()){
                System.out.println("RIGHT_REC_ARITH_EXPRESSION -> ADD_OP TERM RIGHT_REC_ARITH_EXPRESSION .");
            }else success = false;
        }else if (e.FOLLOW("RIGHT_REC_ARITH_EXPRESSION").contains(lookahead.getToken())){
            System.out.println("RIGHT_REC_ARITH_EXPRESSION ->  .");
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE(){
//        FUNCTION_SIGNATURE -> id FUNCTION_SIGNATURE_NAMESPACE .
        if (!skipErrors("FUNCTION_SIGNATURE")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE").contains(lookahead.getToken())){
            if (match("id") && FUNCTION_SIGNATURE_NAMESPACE()){
                System.out.println("FUNCTION_SIGNATURE -> id FUNCTION_SIGNATURE_NAMESPACE .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE_NAMESPACE(){
//        FUNCTION_SIGNATURE_NAMESPACE -> FUNCTION_SIGNATURE_EXT .
//        FUNCTION_SIGNATURE_NAMESPACE -> sr id FUNCTION_SIGNATURE_EXT .
        if (!skipErrors("FUNCTION_SIGNATURE_NAMESPACE")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE_EXT").contains(lookahead.getToken())){
            if (FUNCTION_SIGNATURE_EXT()){
                System.out.println("FUNCTION_SIGNATURE_NAMESPACE -> FUNCTION_SIGNATURE_EXT .");
            }else success = false;
        }else if (e.FIRST("FUNCTION_SIGNATURE_NAMESPACE").contains(lookahead.getToken())){
            if (match("sr") && match("id") && FUNCTION_SIGNATURE_EXT())
            System.out.println("FUNCTION_SIGNATURE_NAMESPACE -> sr id FUNCTION_SIGNATURE_EXT .");
        }else success = false;
        return success;
    }

    private boolean FUNCTION_SIGNATURE_EXT(){
//        FUNCTION_SIGNATURE_EXT -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID .
        if (!skipErrors("FUNCTION_SIGNATURE_EXT")) return false;
        if (e.FIRST("FUNCTION_SIGNATURE_EXT").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_PARAMS() && match("rpar")
                    && match("colon") && TYPE_OR_VOID()){
                System.out.println("FUNCTION_SIGNATURE_EXT -> lpar FUNCTION_PARAMS rpar colon TYPE_OR_VOID .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS_TAILS(){
//        FUNCTION_PARAMS_TAILS  -> FUNCTION_PARAMS_TAIL FUNCTION_PARAMS_TAILS .
//        FUNCTION_PARAMS_TAILS  ->  .
        if (!skipErrors("FUNCTION_PARAMS_TAILS")) return false;
        if (e.FIRST("FUNCTION_PARAMS_TAIL").contains(lookahead.getToken())){
            if (FUNCTION_PARAMS_TAIL() && FUNCTION_PARAMS_TAILS()){
                System.out.println("FUNCTION_PARAMS_TAILS  -> FUNCTION_PARAMS_TAIL FUNCTION_PARAMS_TAILS .");
            }else success = false;
        }else if (e.FOLLOW("FUNCTION_PARAMS_TAILS").contains(lookahead.getToken())){
            System.out.println("FUNCTION_PARAMS_TAILS ->  .");
        }else success = false;
        return success;
    }

    private boolean INHERITED_CLASSES(){
//        INHERITED_CLASSES  -> comma id INHERITED_CLASSES .
//        INHERITED_CLASSES  ->  .
        if (!skipErrors("INHERITED_CLASSES")) return false;
        if (e.FIRST("INHERITED_CLASSES").contains(lookahead.getToken())){
            if (match("comma") && match("id") && INHERITED_CLASSES()){
                System.out.println("INHERITED_CLASSES  -> comma id INHERITED_CLASSES .");
            }else success = false;
        }else if (e.FOLLOW("INHERITED_CLASSES").contains(lookahead.getToken())){
            System.out.println("INHERITED_CLASSES ->  .");
        }else success = false;
        return success;
    }

    private boolean SIGN(){
//        SIGN -> plus . | minus .
        if (!skipErrors("SIGN")) return false;
        if (e.FIRST("SIGN").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("plus")){
                if (match("plus")){
                    System.out.println("SIGN -> plus .");
                }
            }else if (lookahead.getToken().equals("minus")){
                if (match("minus")){
                System.out.println("SIGN -> minus .");
            }else success = false;
            }
        }else success = false;
        return success;
    }

    private boolean COMPARE_OP(){
//        COMPARE_OP -> eq . | neq . | lt . | gt . | leq . | geq .
        if (!skipErrors("COMPARE_OP")) return false;
        if (e.FIRST("COMPARE_OP").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("eq")) {
                if (match("eq")) {
                    System.out.println("SIGN -> eq .");
                }
            }else if (lookahead.getToken().equals("neq")){
                    if (match("neq")){
                    System.out.println("SIGN -> neq .");
                }
            }else if (lookahead.getToken().equals("lessthan")){
                    if (match("lessthan")){
                    System.out.println("SIGN -> lt .");
                }
            }else if (lookahead.getToken().equals("gt")){
                    if (match("gt")){
                    System.out.println("SIGN -> gt .");
                }
            }else if (lookahead.getToken().equals("leq")){
                    if (match("leq")){
                    System.out.println("SIGN -> leq .");
                }
            }else if (lookahead.getToken().equals("geq")){
                    if (match("geq")){
                    System.out.println("SIGN -> geq .");
                }
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean INDEX(){
//        INDEX  -> lsqbr ARITH_EXPRESSION rsqbr .
        if (!skipErrors("INDEX")) return false;
        if (e.FIRST("INDEX").contains(lookahead.getToken())){
            if (match("lsqbr") && ARITH_EXPRESSION() && match("rsqbr")){
                System.out.println("INDEX  -> lsqbr ARITH_EXPRESSION rsqbr .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_DECLARATIONS(){
//        VARIABLE_DECLARATIONS  -> TYPE VARIABLE_DECLARATION VARIABLE_DECLARATIONS .
//        VARIABLE_DECLARATIONS  ->  .
        if (!skipErrors("VARIABLE_DECLARATIONS")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (TYPE() && VARIABLE_DECLARATION() && VARIABLE_DECLARATIONS()){
                System.out.println("VARIABLE_DECLARATIONS  -> TYPE VARIABLE_DECLARATION VARIABLE_DECLARATIONS .");
            }else success = false;
        }else if (e.FOLLOW("VARIABLE_DECLARATIONS").contains(lookahead.getToken())){
            System.out.println("VARIABLE_DECLARATIONS ->  .");
        }else success = false;
        return success;
    }

    private boolean FACTOR(){
        if (!skipErrors("FACTOR")) return false;
//        FACTOR -> VARIABLE_FUNCTION_CALL .
        if (e.FIRST("VARIABLE_FUNCTION_CALL").contains(lookahead.getToken())){
            if (VARIABLE_FUNCTION_CALL()){
                System.out.println("FACTOR -> VARIABLE_FUNCTION_CALL .");
            }else success = false;
        }
//        FACTOR -> SIGN FACTOR .
        else if (e.FIRST("SIGN").contains(lookahead.getToken())){
            if (SIGN() && FACTOR()){
                System.out.println("FACTOR -> SIGN FACTOR .");
            }else success = false;
        }
//        FACTOR -> lpar ARITH_EXPRESSION rpar .
//        FACTOR -> not FACTOR .
//        FACTOR -> integer .
//        FACTOR -> float .
        else if (e.FIRST("FACTOR").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("lpar")) {
                if (match("lpar") && ARITH_EXPRESSION() && match("rpar")) {
                    System.out.println("FACTOR -> lpar ARITH_EXPRESSION rpar .");
                }
            }else if (lookahead.getToken().equals("not")){
                    if (match("not") && FACTOR()){
                    System.out.println("FACTOR -> not FACTOR .");
                }
            }else if (lookahead.getToken().equals("intnum")){
                    if (match("intnum")){
                    System.out.println("FACTOR -> intnum .");
                }
            }else if (lookahead.getToken().equals("floatnum")){
                    if (match("floatnum")){
                    System.out.println("FACTOR -> intnum .");
                }
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_FUNCTION_CALL(){
//        VARIABLE_FUNCTION_CALL -> id VARIABLE_OR_FUNCTION_CALL .
        if (!skipErrors("VARIABLE_FUNCTION_CALL")) return false;
        if (e.FIRST("VARIABLE_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("id") && VARIABLE_OR_FUNCTION_CALL()){
                System.out.println("VARIABLE_FUNCTION_CALL -> id VARIABLE_OR_FUNCTION_CALL .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean VARIABLE_OR_FUNCTION_CALL(){
//        VARIABLE_OR_FUNCTION_CALL  -> INDICES FACTOR_VARIABLE .
//        VARIABLE_OR_FUNCTION_CALL  -> lpar FUNCTION_CALL_PARAMS rpar FACTOR_FUNCTION_CALL .
        if (!skipErrors("VARIABLE_OR_FUNCTION_CALL")) return false;
        if (e.FIRST("INDICES").contains(lookahead.getToken()) || e.FOLLOW("INDICES").contains(lookahead.getToken()) ){ //
            if (INDICES() && FACTOR_VARIABLE()){
                System.out.println("VARIABLE_OR_FUNCTION_CALL  -> INDICES FACTOR_VARIABLE .");
            }else success = false;
        }else if (e.FIRST("VARIABLE_OR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("lpar") && FUNCTION_CALL_PARAMS() && match("rpar")
                && FACTOR_FUNCTION_CALL())
                System.out.println("VARIABLE_OR_FUNCTION_CALL -> lpar FUNCTION_CALL_PARAMS rpar FACTOR_FUNCTION_CALL .");
        }else success = false;
        return success;
    }

    private boolean FACTOR_VARIABLE(){
//        FACTOR_VARIABLE  -> dot VARIABLE_FUNCTION_CALL .
//        FACTOR_VARIABLE  ->  .
        if (!skipErrors("FACTOR_VARIABLE")) return false;
        if (e.FIRST("FACTOR_VARIABLE").contains(lookahead.getToken())){ // || e.isNULLABLE("FACTOR_VARIABLE")
            if (match("dot") && VARIABLE_FUNCTION_CALL()){
                System.out.println("FACTOR_VARIABLE  -> dot VARIABLE_FUNCTION_CALL .");
            }else success = false;
        }else if (e.FOLLOW("FACTOR_VARIABLE").contains(lookahead.getToken())){
            System.out.println("FACTOR_VARIABLE ->  .");
        }else success = false;
        return success;
    }

    private boolean FACTOR_FUNCTION_CALL(){
//        FACTOR_FUNCTION_CALL -> dot VARIABLE_FUNCTION_CALL .
//        FACTOR_FUNCTION_CALL ->  .
        if (!skipErrors("FACTOR_FUNCTION_CALL")) return false;
        if (e.FIRST("FACTOR_FUNCTION_CALL").contains(lookahead.getToken())){
            if (match("dot") && VARIABLE_FUNCTION_CALL()){
                System.out.println("FACTOR_FUNCTION_CALL  -> dot VARIABLE_FUNCTION_CALL .");
            }else success = false;
        }else if (e.FOLLOW("FACTOR_FUNCTION_CALL").contains(lookahead.getToken())){
            System.out.println("FACTOR_FUNCTION_CALL ->  .");
        }else success = false;
        return success;
    }

    private boolean TERM(){
//        TERM -> FACTOR RIGHT_REC_TERM .
        if (!skipErrors("TERM")) return false;
        if (e.FIRST("FACTOR").contains(lookahead.getToken())){
            if (FACTOR() && RIGHT_REC_TERM()){
                System.out.println("TERM -> FACTOR RIGHT_REC_TERM .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean MULT_OP(){
//        MULT_OP  -> mult . | div . | and .
        if (!skipErrors("MULT_OP")) return false;
        if (e.FIRST("MULT_OP").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("mult")){
                if (match("mult")){
                System.out.println("MULT_OP -> mult .");
                }
            }else if (lookahead.getToken().equals("div")) {
                if (match("div")){
                System.out.println("MULT_OP -> div .");
                }
            }else if(lookahead.getToken().equals("and")){
                if (match("and")){
                System.out.println("MULT_OP -> and .");
            }
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean RIGHT_REC_TERM(){
//        RIGHT_REC_TERM -> MULT_OP FACTOR RIGHT_REC_TERM .
//        RIGHT_REC_TERM ->  .
        if (!skipErrors("RIGHT_REC_TERM")) return false;
        if (e.FIRST("MULT_OP").contains(lookahead.getToken())){
            if (MULT_OP() && FACTOR() && RIGHT_REC_TERM()){
                System.out.println("RIGHT_REC_TERM -> MULT_OP FACTOR RIGHT_REC_TERM .");
            }else success = false;
        }else if (e.FOLLOW("RIGHT_REC_TERM").contains(lookahead.getToken())){
            System.out.println("RIGHT_REC_TERM ->  .");
        }else success = false;
        return success;
    }

    private boolean TYPE_OR_VOID(){
//        TYPE_OR_VOID -> TYPE .
//        TYPE_OR_VOID -> void .
        if (!skipErrors("TYPE_OR_VOID")) return false;
        if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (TYPE()){
                System.out.println("TYPE_OR_VOID -> TYPE .");
            }else success = false;
        }else if (e.FIRST("TYPE_OR_VOID").contains(lookahead.getToken())){
            if (match("void")){
                System.out.println("TYPE_OR_VOID -> void .");
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
                System.out.println("TYPE -> TYPE_NON_ID .");
            }else success = false;
        }else if (e.FIRST("TYPE").contains(lookahead.getToken())){
            if (match("id")){
                System.out.println("TYPE -> id .");
            }
        }else success = false;
        return success;
    }

    private boolean TYPE_NON_ID(){
//        TYPE_NON_ID  -> integer .
//        TYPE_NON_ID  -> float .
        if (!skipErrors("TYPE_NON_ID")) return false;
        if (e.FIRST("TYPE_NON_ID").contains(lookahead.getToken())){
            if (lookahead.getToken().equals("integer")){
                if (match("integer")){
                    System.out.println("TYPE_NON_ID  -> integer .");
                }
            }else if (lookahead.getToken().equals("float")) {
                if (match("float")) {
                    System.out.println("TYPE_NON_ID  -> float .");
                }
            }else success = false;

        }else success = false;
        return success;
    }

    private boolean ARRAY_SIZE(){
//        ARRAY_SIZE -> lsqbr OPTIONAL_INT rsqbr .
        if (!skipErrors("ARRAY_SIZE")) return false;
        if (e.FIRST("ARRAY_SIZE").contains(lookahead.getToken())){
            if (match("lsqbr") && OPTIONAL_INT() && match("rsqbr")){
                System.out.println("TERM -> FACTOR RIGHT_REC_TERM .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean OPTIONAL_INT(){
//        OPTIONAL_INT -> integer .
//        OPTIONAL_INT ->  .
        if (!skipErrors("OPTIONAL_INT")) return false;
        if (e.FIRST("OPTIONAL_INT").contains(lookahead.getToken())){
            if (match("intnum")){
                System.out.println("OPTIONAL_INT -> integer .");
            }else success = false;
        }else if (e.FOLLOW("OPTIONAL_INT").contains(lookahead.getToken())){
            System.out.println("OPTIONAL_INT ->  .");
        }else success = false;
        return success;
    }

    private boolean VARIABLE_DECLARATION(){
//        VARIABLE_DECLARATION -> id ARRAY_DIMENSIONS semi .
        if (!skipErrors("VARIABLE_DECLARATION")) return false;
        if (e.FIRST("VARIABLE_DECLARATION").contains(lookahead.getToken())){
            if (match("id") && ARRAY_DIMENSIONS() && match("semi")){
                System.out.println("VARIABLE_DECLARATION -> id ARRAY_DIMENSIONS semi .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCBODY(){
//        FUNCBODY  -> OPTFUNCBODY0 do REPTSTATEMENT end .
        if (!skipErrors("FUNCBODY")) return false;
        if (e.FIRST("OPTFUNCBODY0").contains(lookahead.getToken()) || e.isNULLABLE("OPTFUNCBODY0")){
            if (OPTFUNCBODY0() && match("do") && REPTSTATEMENT() && match("end")){
                System.out.println("FUNCBODY  -> OPTFUNCBODY0 do REPTSTATEMENT end .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean STATEMENT_BLOCK(){
//        STATEMENT_BLOCK  -> STATEMENT .
//        STATEMENT_BLOCK  -> do REPTSTATEMENT end .
//        STATEMENT_BLOCK  ->  .
        if (!skipErrors("STATEMENT_BLOCK")) return false;
        if (e.FIRST("STATEMENT").contains(lookahead.getToken())) {
            if (STATEMENT()) {
                System.out.println("STATEMENT_BLOCK  -> STATEMENT .");
            } else success = false;
        }else if (e.FIRST("STATEMENT_BLOCK").contains(lookahead.getToken())){
            if (match("do") && REPTSTATEMENT() && match("end")){
                System.out.println("STATEMENT_BLOCK  -> do REPTSTATEMENT end .");
            }else success = false;
        }else if (e.FOLLOW("STATEMENT_BLOCK").contains(lookahead.getToken())){
            System.out.println("STATEMENT_BLOCK ->  .");
        }else success = false;
        return success;
    }

    private boolean ASSIGNMENT_OP(){
//        ASSIGNMENT_OP  -> eq .
        if (!skipErrors("ASSIGNMENT_OP")) return false;
        if (e.FIRST("ASSIGNMENT_OP").contains(lookahead.getToken())){
            if (match("equal")){
                System.out.println("ASSIGNMENT_OP  -> equal .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean FUNCTION_PARAMS_TAIL(){
//        FUNCTION_PARAMS_TAIL -> comma TYPE id ARRAY_DIMENSIONS .
        if (!skipErrors("FUNCTION_PARAMS_TAIL")) return false;
        if (e.FIRST("FUNCTION_PARAMS_TAIL").contains(lookahead.getToken())){
            if (match("comma") && TYPE() && match("id") && ARRAY_DIMENSIONS()){
                System.out.println("FUNCTION_PARAMS_TAIL -> comma TYPE id ARRAY_DIMENSIONS .");
            }else success = false;
        }else success = false;
        return success;
    }

    private boolean INDICES(){
//        INDICES  -> INDEX INDICES .
//        INDICES  ->  .
        if (!skipErrors("INDICES")) return false; //|| e.isNULLABLE("INDICES")
        if (e.FIRST("INDEX").contains(lookahead.getToken())){
            if (INDEX() && INDICES()){
                System.out.println("INDICES  -> INDEX INDICES .");
            }else success = false;
        }else if (e.FOLLOW("INDICES").contains(lookahead.getToken())){
            System.out.println("INDICES ->  .");
        }else success = false;
        return success;
    }

}

