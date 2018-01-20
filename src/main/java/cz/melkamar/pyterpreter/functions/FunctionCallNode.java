package cz.melkamar.pyterpreter.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.WrongParameterCountException;
import cz.melkamar.pyterpreter.nodes.PyNode;

public class FunctionCallNode extends PyNode {
    private String funcName;

    public FunctionCallNode(String funcName) {
        this.funcName = funcName;
    }

    @Override
    public Object execute(Environment env) {
        Function func = (Function) env.getValue(funcName);
        Environment newEnv = new Environment(env);

        if (children.size() != func.getArgNames().length){
            throw new WrongParameterCountException("Function "+funcName+" expects "+func.getArgNames().length+" " +
                    "parameters, but "+children.size()+" provided.");
        }

        for (int i = 0; i < func.getArgNames().length; i++) {
            newEnv.putValue(func.getArgNames()[i], children.get(i).execute(env));
        }
        return func.execute(newEnv);
    }

    @Override
    public void print(int indent) {
        printIndented("call " + funcName, indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }
}
