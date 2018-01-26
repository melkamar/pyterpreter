package cz.melkamar.pyterpreter.truffle;

import com.oracle.truffle.api.dsl.TypeSystem;

import java.math.BigInteger;

@TypeSystem({
        long.class,
        boolean.class,
        double.class,
        BigInteger.class,
        String.class,
})
public abstract class PyTypes {
}
