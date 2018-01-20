package cz.melkamar.pyterpreter.functions.builtin;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.functions.Function;

public class ExitFunction extends Function {
    @Override
    public Object execute(Environment env) {
        System.exit(0);
        return null;
    }
}
