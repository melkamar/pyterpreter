package cz.melkamar.pyterpreter.nodes.functions;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.template.PyNode;

public class FuncCodeNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        throw new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented("code", indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
