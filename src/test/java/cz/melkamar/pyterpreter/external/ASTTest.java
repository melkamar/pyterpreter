package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.nodes.arithmetic.PyAddNode;
import cz.melkamar.pyterpreter.nodes.PyNumberNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PySubtractNode;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import org.junit.Test;

import static org.junit.Assert.*;


public class ASTTest {
    @Test
    public void additionAstStructure() {
        PyRootNode rootNode = AST.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        assertEquals(rootNode.children.size(), 1);

        PyNode firstChild = rootNode.children.get(0);
        assertTrue(firstChild instanceof PyAddNode);
        assertTrue(firstChild.children.size() == 2);

        assertTrue(firstChild.children.get(0) instanceof PyAddNode);
        assertTrue((Long) (firstChild.children.get(1)).execute(null) == 6);
        assertTrue(firstChild.children.get(1) instanceof PyNumberNode);

        // Check bottom two leaves are numbers
        assertTrue(firstChild.children.get(0).children.get(0).children.get(0).children.get(0).children.get(0) instanceof PyNumberNode);
        assertTrue(firstChild.children.get(0).children.get(0).children.get(0).children.get(0).children.get(1) instanceof PyNumberNode);
    }

    @Test
    public void subtractionAstStructure(){
        PyRootNode rootNode = AST.astFromCode("1 - 4 - 3");
        assertEquals(rootNode.children.size(), 1);

        PyNode firstChild = rootNode.children.get(0);
        assertTrue(firstChild instanceof PySubtractNode);
        assertTrue(firstChild.children.size() == 2);

        assertTrue(firstChild.children.get(1) instanceof PyNumberNode);
        assertTrue((Long) (firstChild.children.get(1)).execute(null) == 3);
        assertTrue(firstChild.children.get(0) instanceof PySubtractNode);

        assertTrue((Long) (firstChild.children.get(0).children.get(0)).execute(null) == 1);
        assertTrue((Long) (firstChild.children.get(0).children.get(1)).execute(null) == 4);
    }
}
