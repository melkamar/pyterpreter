package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:09.
 */
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

    public List<PyNode> getChildren() {
        return children;
    }

    public abstract Object execute(Environment env);

    public void print(int indent){
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
}
