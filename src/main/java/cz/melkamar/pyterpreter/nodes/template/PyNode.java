package cz.melkamar.pyterpreter.nodes.template;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import cz.melkamar.pyterpreter.Environment;

import java.util.ArrayList;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:09.
 */
public abstract class PyNode extends Node {
    public ArrayList<PyNode> children;

    public void addChildNode(PyNode node){
        children.add(node);
    }

    public abstract Object execute(Environment env);
}
