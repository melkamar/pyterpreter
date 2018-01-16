package cz.melkamar.pyterpreter.nodes.template;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import cz.melkamar.pyterpreter.Environment;

import java.util.ArrayList;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:09.
 */
public abstract class PyNode extends Node {
    public ArrayList<PyNode> children = new ArrayList<>();

    public void addChild(PyNode node) {
        children.add(node);
    }

    public abstract Object execute(Environment env);

    public abstract void print(int indent);

    protected void printIndented(String text, int indent){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<indent; i++){
            builder.append("  ");
        }
        builder.append(text);
        System.out.println(builder.toString());
    }
}
