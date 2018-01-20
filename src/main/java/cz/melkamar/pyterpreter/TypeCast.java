package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.exceptions.NotImplementedException;

public class TypeCast {
    public static Boolean castToBoolean(Object object) {
        if (object instanceof Boolean) return (Boolean) object;
        if (object instanceof Long) return ((Long) object) != 0;
        throw new NotImplementedException("cast to bool not implemented for "+object);
    }
}
