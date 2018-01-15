package cz.melkamar.pyterpreter.types;

import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.internal.DSLOptions;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:39.
 */
@TypeSystem({
        long.class,
        boolean.class,
        PyFunction.class,
        PySymbol.class
})
public class PyTypes {
}
