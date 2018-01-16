package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.nodes.AssignNode;
import cz.melkamar.pyterpreter.nodes.PySymbolNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PyAddNode;
import cz.melkamar.pyterpreter.nodes.PyNumberNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PySubtractNode;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import cz.melkamar.pyterpreter.types.PySymbol;
import org.junit.Test;

import static org.junit.Assert.*;


@SuppressWarnings("Duplicates")
public class ParseTreeTest {
    @Test
    public void additionAstStructure() {
        PyRootNode rootNode = ParseTree.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        assertEquals(rootNode.children.size(), 1);

        PyNode firstChild = rootNode.children.get(0);
        assertTrue(firstChild instanceof PyAddNode);
        assertTrue(firstChild.children.size() == 2);

        assertTrue(firstChild.children.get(0) instanceof PyAddNode);
        assertEquals(6, ((PyNumberNode) firstChild.getChild(1)).number);
        assertTrue(firstChild.children.get(1) instanceof PyNumberNode);

        // Check bottom two leaves are numbers
        assertTrue(firstChild.children.get(0).children.get(0).children.get(0).children.get(0).children.get(0) instanceof PyNumberNode);
        assertTrue(firstChild.children.get(0).children.get(0).children.get(0).children.get(0).children.get(1) instanceof PyNumberNode);
    }

    @Test
    public void subtractionAstStructure() {
        PyRootNode rootNode = ParseTree.astFromCode("1 - 4 - 3");
        assertEquals(rootNode.children.size(), 1);

        PyNode firstChild = rootNode.children.get(0);
        assertTrue(firstChild instanceof PySubtractNode);
        assertTrue(firstChild.children.size() == 2);

        assertTrue(firstChild.children.get(1) instanceof PyNumberNode);
        assertEquals(3, ((PyNumberNode) firstChild.getChild(1)).number);
        assertTrue(firstChild.children.get(0) instanceof PySubtractNode);

        assertEquals(1, ((PyNumberNode) firstChild.children.get(0).children.get(0)).number);
        assertEquals(4, ((PyNumberNode) firstChild.children.get(0).children.get(1)).number);
    }

    @Test
    public void assignSimple() {
        PyRootNode rootNode = ParseTree.astFromCode("x = 5");
        PyNode firstChild = rootNode.getChild(0);

        assertTrue(firstChild instanceof AssignNode);
        assertTrue(firstChild.getChild(0) instanceof PySymbolNode);
        assertTrue(((PySymbolNode) firstChild.getChild(0)).name.equals("x"));

        assertTrue(firstChild.getChild(1) instanceof PyNumberNode);
        assertTrue(((PyNumberNode) firstChild.getChild(1)).number == 5);
    }

    @Test
    public void assignHarder() {
        PyRootNode rootNode = ParseTree.astFromCode("x = 5 + 1");
        PyNode firstChild = rootNode.getChild(0);

        assertTrue(firstChild instanceof AssignNode);
        assertTrue(firstChild.getChild(0) instanceof PySymbolNode);
        assertTrue(((PySymbolNode) firstChild.getChild(0)).name.equals("x"));

        assertTrue(firstChild.getChild(1) instanceof PyAddNode);
        assertTrue(((PyNumberNode) firstChild.getChild(1).getChild(0)).number == 5);
        assertTrue(((PyNumberNode) firstChild.getChild(1).getChild(1)).number == 1);
    }
}
