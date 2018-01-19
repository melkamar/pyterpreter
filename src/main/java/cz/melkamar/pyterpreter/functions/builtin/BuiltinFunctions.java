package cz.melkamar.pyterpreter.functions.builtin;

import cz.melkamar.pyterpreter.Environment;

public class BuiltinFunctions {
    public static void fillEnv(Environment env){
        env.putValue("print", new PrintFunction());
    }
}
