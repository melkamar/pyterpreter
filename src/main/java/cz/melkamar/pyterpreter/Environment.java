package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;

import java.util.HashMap;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:13.
 */
public class Environment {
    private final HashMap<String, Object> env = new HashMap<>();
    private final Environment parent;
    private boolean returnFlag = false;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public Object getValue(String name) {
        if (env.containsKey(name)) return env.get(name);

        if (parent != null) return parent.getValue(name);
        throw new UndefinedVariableException(name);
    }

    public void putValue(String name, Object value) {
        env.put(name, value);
    }

    public boolean contains(String name) {
        try {
            getValue(name);
        } catch (UndefinedVariableException ex) {
            return false;
        }
        return true;
    }

    public void setReturnFlag(){
        returnFlag = true;
    }

    public boolean isReturnFlag(){
        return returnFlag;
    }

    @Override
    public String toString() {
        return nestToString(1);
    }

    private String spacesFromLevel(int level){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<level; i++) builder.append("  ");
        return builder.toString();
    }
    /**
     * Print this env's map and recursively call parent's print, if parent exists.
     *
     * Example output:
     * ENV (1): {x=6, y=6}   (current)
     *   ENV (2): {}         (parent)
     *     ENV (3): {}       (parent's parent)
     *
     * @param level
     * @return
     */
    public String nestToString(int level) {
        return "ENV (" + level + "): " + this.env.toString() + (parent != null ? "\n" +spacesFromLevel(level)+ parent.nestToString(level + 1) : "");
    }

    public static Environment getDefaultEnvironment() {
        return new Environment();
    }
}
