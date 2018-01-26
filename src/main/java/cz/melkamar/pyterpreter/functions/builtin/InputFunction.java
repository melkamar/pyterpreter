package cz.melkamar.pyterpreter.functions.builtin;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.functions.Function;

import java.io.InputStream;
import java.util.Scanner;

public class InputFunction extends Function {
    private static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    /**
     * Change input stream to read from. Useful for unit tests - input can be a predefined string.
     *
     * @param stdin
     */
    public static void setStdin(InputStream stdin) {
        sc = new Scanner(stdin);
    }

    public InputFunction() {
        super(new String[]{});
    }

    @Override
    public Object execute(Environment env) {
        String input = sc.nextLine();
        if (Environment.DEBUG_MODE)
            System.out.println("IN: " + input);
        return input;
    }
}
