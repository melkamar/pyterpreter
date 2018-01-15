package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;

import java.util.HashMap;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:13.
 */
public class Environment {
    private final HashMap<String, Object> env = new HashMap<>();
    private final Environment parent;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public Object getValue(String name){
        if (env.containsKey(name)) return env.get(name);

        if (parent!=null) return parent.getValue(name);
        throw new UndefinedVariableException(name);
    }

    public void putValue(String name, Object value){
        env.put(name, value);
    }
}
