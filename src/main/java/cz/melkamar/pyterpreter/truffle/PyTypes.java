package cz.melkamar.pyterpreter.truffle;

import com.oracle.truffle.api.dsl.TypeSystem;
import cz.melkamar.pyterpreter.functions.Function;

@TypeSystem({
        long.class,
        boolean.class,
        double.class,
        String.class,
        Function.class,
        // TODO bigint?
})
public abstract class PyTypes {
}
