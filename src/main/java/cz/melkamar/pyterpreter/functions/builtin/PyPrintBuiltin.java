package cz.melkamar.pyterpreter.functions.builtin;

import com.oracle.truffle.api.RootCallTarget;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.functions.PyBuiltinFunction;

public class PyPrintBuiltin extends PyBuiltinFunction {
    public PyPrintBuiltin(String name, RootCallTarget callTarget) {
        super(name, callTarget);
    }
//    TODO builtins can be nodes to call direcly, not just regular predefined functions
//    public PyPrintFunction() {
//        super(new String[]{"text"});
//    }

    public Object executeG(Environment env) {
        if (Environment.DEBUG_MODE)
//            System.out.println("OUT: " + String.valueOf(env.getValue("text")));
            throw new NotImplementedException();
        else
//            System.out.println(String.valueOf(env.getValue("text")));
            throw new NotImplementedException();
    }
}
