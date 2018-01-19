package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

import java.util.Arrays;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:17.
 */
public class PyDefFuncNode extends PyNode {
    private String name;
    private String[] args;

    public PyDefFuncNode(String name, String[]args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public Object execute(Environment env) {
        FunctionNode functionNode = new FunctionNode(args);
        functionNode.children.addAll(this.children);
        env.putValue(name, functionNode);

        return null; // result of def cannot be assigned anywhere anyway
    }

    @Override
    public void print(int indent) {
        printIndented("def " + name+ " "+Arrays.toString(args), indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}