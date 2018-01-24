package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.truffle.PyTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:09.
 */
@NodeInfo(language = "Python", description = "Abstract base node")
//public abstract class PyNode extends Node {
public abstract class PyNode {
    public ArrayList<PyNode> children = new ArrayList<>();

    public void addChild(PyNode node) {
        children.add(node);
    }

    public void addChildren(Collection<? extends PyNode> collection) {
        children.addAll(collection);
    }

    public PyNode getChild(int index) {
        return children.get(index);
    }

    public List<PyNode> getChildNodes() {
        return children;
    }

    public void print(int indent) {
        printIndented(this.toString(), indent);
        for (PyNode child : children) {
            child.print(indent + 1);
        }
    }

    protected void printIndented(String text, int indent) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            builder.append("  ");
        }
        builder.append(text);
        System.out.println(builder.toString());
    }

//    public abstract Object execute(VirtualFrame frame);
    public abstract Object execute(Environment env);

    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
//        return PyTypesGen.PYTYPES
        PyTypes.
        return 5;
    }
}
