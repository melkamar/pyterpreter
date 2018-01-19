package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 16.01.2018 16:31.
 */
public class PyListNode extends PyNode {
    List<Object> list = new ArrayList<>();

    public PyListNode(List<Object> list) {
        this.list = list;
    }

    @Override
    public Object execute(Environment env) {
        throw new NotImplementedException();
    }

    @Override
    public void print(int indent) {
        printIndented(Arrays.toString(list.toArray()), indent);
        for (PyNode child : children) {
            child.print(indent+1);
        }
    }
}
