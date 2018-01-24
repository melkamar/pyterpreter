package cz.melkamar.pyterpreter.functions.builtin;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.functions.Function;

public class PrintFunction extends Function {
    public PrintFunction() {
        super(new String[]{"text"});
    }

    @Override
    public Object execute(Environment env) {
        if (Environment.DEBUG_MODE)
            System.out.println("OUT: " + String.valueOf(env.getValue("text")));
        else
            System.out.println(String.valueOf(env.getValue("text")));

        return null;
    }
}
