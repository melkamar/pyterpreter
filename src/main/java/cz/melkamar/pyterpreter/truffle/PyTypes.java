package cz.melkamar.pyterpreter.truffle;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;
import cz.melkamar.pyterpreter.types.PyNoneType;

import java.math.BigInteger;

@TypeSystem({
        long.class,
        boolean.class,
        double.class,
        BigInteger.class,
        String.class,
        PyNoneType.class
})
public abstract class PyTypes {
    @ImplicitCast
    @CompilerDirectives.TruffleBoundary
    public static BigInteger castBigInteger(long value) {
        return BigInteger.valueOf(value);
    }
}
