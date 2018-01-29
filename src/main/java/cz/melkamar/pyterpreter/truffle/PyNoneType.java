package cz.melkamar.pyterpreter.truffle;

public class PyNoneType {
    public static PyNoneType NONE_SINGLETON;
    {
        NONE_SINGLETON = new PyNoneType();
    }

    @Override
    public String toString() {
        return "NoneType";
    }
}
