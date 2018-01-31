package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.Frame;
import cz.melkamar.pyterpreter.exceptions.ParseException;
import cz.melkamar.pyterpreter.exceptions.SystemExitException;
import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.util.Scanner;

public class REPL {

    /**
     * Start a REPL loop.
     * <p>
     * Not every enter will be interpreted as running a command - if we are indented, then we will
     * have to wait to dedent to run the whole thing.
     */
    public static void startRepl() {
        Scanner sc = new Scanner(System.in);
        Environment environment = new EnvironmentBuilder().createEnvironment();
        Frame lastFrame = environment.getBaseFrame();

        int indentLevel = 0;
        StringBuilder inputBuffer = new StringBuilder();

        while (true) {
            if (indentLevel == 0) {
                System.out.print(">>> ");
            } else {
                System.out.print("... ");
                for (int i = 0; i < indentLevel; i++) {
                    System.out.print("    ");
                    inputBuffer.append("    ");
                }
            }

            String input = sc.nextLine();

            if (input.isEmpty() && indentLevel > 0) {
                indentLevel--;
                inputBuffer.append("\n");
//                continue;
            } else {
                inputBuffer.append(input);
                inputBuffer.append("\n");

                if (input.trim().endsWith(":")) {
                    indentLevel++;
                    continue;
                }
            }


            if (indentLevel > 0) continue;

            String code = inputBuffer.toString();
            inputBuffer.delete(0, inputBuffer.length());

            try {
                PyRootNode rootNode = SimpleParseTree.astFromCode(code, environment);
                CallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
                Object result = target.call(lastFrame);

                if (result != null) {
                    System.out.println(result);
                }

                lastFrame = rootNode.lastExecutionFrame;


            } catch (UndefinedVariableException e) {
                System.err.println(e.toString());
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            } catch (SystemExitException e) {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
