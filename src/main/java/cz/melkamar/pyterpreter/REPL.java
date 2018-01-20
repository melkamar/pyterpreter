package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.util.Scanner;

public class REPL {

    /**
     * Start a REPL loop.
     *
     * Not every enter will be interpreted as running a command - if we are indented, then we will
     * have to wait to dedent to run the whole thing.
     */
    public static void startRepl() {
        Environment env = Environment.getDefaultEnvironment();
        Scanner sc = new Scanner(System.in);

        int indentLevel = 0;
        StringBuilder inputBuffer = new StringBuilder();

        while (true) {
            if (indentLevel == 0) {
                System.out.print(">>> ");
            } else {
                System.out.print("    ");
                for (int i = 0; i < indentLevel - 1; i++) {
                    System.out.print("....");
                    inputBuffer.append("    ");
                }
                System.out.print("... ");
                inputBuffer.append("    ");
            }

            String input = sc.nextLine();

            if (input.isEmpty() && indentLevel > 0) {
                indentLevel--;
                inputBuffer.append("\n");
                continue;
            }

            inputBuffer.append(input);
            inputBuffer.append("\n");

            if (input.trim().endsWith(":")) {
                indentLevel++;
                continue;
            }

            if (indentLevel > 0) continue;

            String code = inputBuffer.toString();
            inputBuffer.delete(0, inputBuffer.length());

            try {
                PyRootNode rootNode = SimpleParseTree.astFromCode(code);
                Object result = rootNode.execute(env);
                if (result != null) {
                    // TODO how to handle case when I actually do want to return null? E.g. x=None; x?
                    System.out.println(result);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
