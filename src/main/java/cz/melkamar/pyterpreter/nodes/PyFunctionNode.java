package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 05.11.2017 22:17.
 */
public class PyFunctionNode extends PyNode {
    @Override
    public Object execute(Environment env) {
        return this;
    }
}
