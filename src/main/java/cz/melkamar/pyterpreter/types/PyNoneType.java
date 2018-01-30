package cz.melkamar.pyterpreter.types;

public class PyNoneType {
    final public static PyNoneType NONE_SINGLETON;
    static {
        NONE_SINGLETON = new PyNoneType();
    }

    @Override
    public String toString() {
        return "NoneType";
    }
}
