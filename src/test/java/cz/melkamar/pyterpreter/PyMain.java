package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.external.AST;
import cz.melkamar.pyterpreter.nodes.PyAddNode;
import cz.melkamar.pyterpreter.nodes.PyNumberNode;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import org.junit.Test;

import static org.junit.Assert.*;


public class PyMain {
    @Test
    public void runTests() {
        PyRootNode rootNode = AST.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        assertEquals(rootNode.children.size(), 1);

        PyNode firstChild = rootNode.children.get(0);
        assertTrue(firstChild instanceof PyAddNode);
        assertTrue(firstChild.children.size() == 2);

        assertTrue(firstChild.children.get(0) instanceof PyNumberNode);
        assertTrue((Long) (firstChild.children.get(0)).execute(null) == 1);
        assertTrue(firstChild.children.get(1) instanceof PyAddNode);

        // Check bottom two leaves are numbers
        assertTrue(firstChild.children.get(1).children.get(1).children.get(1).children.get(1).children.get(0) instanceof PyNumberNode);
        assertTrue(firstChild.children.get(1).children.get(1).children.get(1).children.get(1).children.get(1) instanceof PyNumberNode);
    }
}
