package com.gagan_442_a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class error {

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
            "      ADD_OP      NO      NO      plus minus or      id lpar plus minus intnum floatnum not\n" +
            "      OPTCLASSDECL2      YES      NO      inherits      lcurbr\n" +
            "      REL_EXPRESSION      NO      NO      id lpar plus minus intnum floatnum not      rpar\n" +
            "      FUNCTION_DECLARATION      NO      NO      lpar      rcurbr public private\n" +
            "      FUNCTION_CALL_PARAMS_TAILS      YES      NO      comma      rpar\n" +
            "      FUNCTION_CALL_PARAMS_TAIL      NO      NO      comma      rpar comma\n" +
            "      FUNCTION_CALL_PARAMS      YES      NO      id lpar plus minus intnum floatnum not      rpar\n" +
            "      OPTFUNCBODY0      YES      NO      local      do\n" +
            "      ARRAY_DIMENSIONS      YES      NO      lsqbr      semi rpar comma\n" +
            "      EXPRESSION      NO      NO      id lpar plus minus intnum floatnum not      semi rpar comma\n" +
            "      REL_EXPRESSION_OR_NULL      YES      NO      eq neq lessthan gt leq geq      semi rpar comma\n" +
            "      REPTSTATEMENT      YES      NO      id if while read write return      end\n" +
            "      ARITH_EXPRESSION      NO      NO      id lpar plus minus intnum floatnum not      semi rpar comma eq neq lessthan gt leq geq rsqbr\n" +
            "      RIGHT_REC_ARITH_EXPRESSION      YES      NO      plus minus or      semi rpar comma eq neq lessthan gt leq geq rsqbr\n" +
            "      FUNCTION_SIGNATURE      NO      NO      id      local do\n" +
            "      FUNCTION_SIGNATURE_NAMESPACE      NO      NO      lpar sr      local do\n" +
            "      FUNCTION_SIGNATURE_EXT      NO      NO      lpar      local do\n" +
            "      FUNCTION_PARAMS_TAILS      YES      NO      comma      rpar\n" +
            "      INHERITED_CLASSES      YES      NO      comma      lcurbr\n" +
            "      SIGN      NO      NO      plus minus      id lpar plus minus intnum floatnum not\n" +
            "      COMPARE_OP      NO      NO      eq neq lessthan gt leq geq      id lpar plus minus intnum floatnum not\n" +
            "      INDEX      NO      NO      lsqbr      semi rpar dot plus minus or comma eq neq lessthan gt leq geq lsqbr rsqbr mult div and equal\n" +
            "      VARIABLE_DECLARATIONS      YES      NO      id integer float      do\n" +
            "      FACTOR      NO      NO      id lpar plus minus intnum floatnum not      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
            "      VARIABLE_FUNCTION_CALL      NO      NO      id      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
            "      VARIABLE_OR_FUNCTION_CALL      YES      NO      lpar dot lsqbr      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
            "      FACTOR_VARIABLE      YES      NO      dot      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
            "      FACTOR_FUNCTION_CALL      YES      NO      dot      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr mult div and\n" +
            "      TERM      NO      NO      id lpar plus minus intnum floatnum not      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr\n" +
            "      MULT_OP      NO      NO      mult div and      id lpar plus minus intnum floatnum not\n" +
            "      RIGHT_REC_TERM      YES      NO      mult div and      semi rpar plus minus or comma eq neq lessthan gt leq geq rsqbr\n" +
            "      TYPE_OR_VOID      NO      NO      id void integer float      semi local do\n" +
            "      TYPE      NO      NO      id integer float      id semi local do\n" +
            "      TYPE_NON_ID      NO      NO      integer float      id semi local do\n" +
            "      ARRAY_SIZE      NO      NO      lsqbr      semi rpar comma lsqbr\n" +
            "      OPTIONAL_INT      YES      NO      intnum      rsqbr\n" +
            "      VARIABLE_DECLARATION      NO      NO      id      id rcurbr public private integer float do\n" +
            "      FUNCBODY      NO      YES      local do      main id $\n" +
            "      STATEMENT_BLOCK      YES      NO      id if while read write return do      semi else\n" +
            "      ASSIGNMENT_OP      NO      NO      equal      id lpar plus minus intnum floatnum not\n" +
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

    public boolean isNULLABLE(String LHS) {
        List<String> info = neff.get(LHS);
        String nullable = info.get(0);
        return (nullable.equals("YES"));
    }

    public boolean isENDABLE(String LHS) {
        List<String> info = neff.get(LHS);
        String endable = info.get(1);
        return (endable.equals("YES"));
    }

    public String FIRST(String LHS) {
        List<String> info = neff.get(LHS);
        return info.get(2);
    }

    public String FOLLOW(String LHS) {
        List<String> info = neff.get(LHS);
        return info.get(3);
    }

}
