package visitor;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenHelper {
    public static final String FALSE = "0";
    public static final String TRUE = "1";

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int EXECUTED = 12;

    public static MoonResult executeMoonProgram(String fileName) {
        String command = "src/test/CodeGenTest/moon ";

        if(fileName != null && !fileName.isEmpty()) {
            command += " " + fileName;
        }

        Process moonProcess = null;
        try {
            moonProcess = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        Scanner scanner = new Scanner(moonProcess.getInputStream());
        List<String> moonOutput = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            moonOutput.add(string);
        }
        int exitValue = Integer.MIN_VALUE;
        try {
            exitValue = moonProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        scanner.close();

        return new MoonResult(exitValue, moonOutput);
    }

    public static class MoonResult{
        public final int returnCode;
        public final List<String> output;

        public List<String> getUsefulOutput(){
            List<String> usefulOutput =  output.subList(2, output.size()-1); //The output without first message, last newline, and number of cycles
//            usefulOutput.removeIf((String str)->{return str.isBlank();}); //Removes all whitespace output

            return usefulOutput;
        }

        public MoonResult(int returnCode, List<String> output) {
            this.returnCode = returnCode;
            this.output = new ArrayList<>(output);
        }

        @Override
        public String toString() {
            String string = "Return Code: " + returnCode;
            string += ". Moon output:\n";
            string += output;
            return string;
        }
    }
}
