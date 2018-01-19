package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.nodes.AssignNode;
import cz.melkamar.pyterpreter.nodes.PyNumberNode;
import cz.melkamar.pyterpreter.nodes.PySymbolNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PyAddNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PyMultiplyNode;
import cz.melkamar.pyterpreter.nodes.arithmetic.PySubtractNode;
import cz.melkamar.pyterpreter.nodes.functions.PyDefFuncNode;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SuppressWarnings("Duplicates")
public class SimpleParseTreeTest {
    @Test
    public void additionAstStructure() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
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
        PyRootNode rootNode = SimpleParseTree.astFromCode("1 - 4 - 3");
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
    public void multiplicationAstStructure() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("1 - 4 * 3 + 2");
        assertEquals(rootNode.children.size(), 1);

        PyNode firstChild = rootNode.getChild(0);
        assertTrue(firstChild instanceof PyAddNode);
        assertTrue(firstChild.children.size() == 2);

        assertTrue(firstChild.getChild(0) instanceof PySubtractNode);
        assertTrue(firstChild.getChild(1) instanceof PyNumberNode);
        assertEquals(2, ((PyNumberNode) firstChild.getChild(1)).number);

        assertTrue(firstChild.getChild(0).getChild(0) instanceof PyNumberNode);
        assertEquals(1, ((PyNumberNode) firstChild.getChild(0).getChild(0)).number);
        assertTrue(firstChild.getChild(0).getChild(1) instanceof PyMultiplyNode);

        assertTrue(firstChild.getChild(0).getChild(1).getChild(0) instanceof PyNumberNode);
        assertTrue(firstChild.getChild(0).getChild(1).getChild(1) instanceof PyNumberNode);
        assertEquals(4, ((PyNumberNode) firstChild.getChild(0).getChild(1).getChild(0)).number);
        assertEquals(3, ((PyNumberNode) firstChild.getChild(0).getChild(1).getChild(1)).number);
    }

    @Test
    public void assignSimple() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("x = 5");
        PyNode firstChild = rootNode.getChild(0);

        assertTrue(firstChild instanceof AssignNode);
        assertTrue(firstChild.getChild(0) instanceof PySymbolNode);
        assertTrue(((PySymbolNode) firstChild.getChild(0)).name.equals("x"));

        assertTrue(firstChild.getChild(1) instanceof PyNumberNode);
        assertTrue(((PyNumberNode) firstChild.getChild(1)).number == 5);
    }

    @Test
    public void assignHarder() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("x = 5 + 1");
        PyNode firstChild = rootNode.getChild(0);

        assertTrue(firstChild instanceof AssignNode);
        assertTrue(firstChild.getChild(0) instanceof PySymbolNode);
        assertTrue(((PySymbolNode) firstChild.getChild(0)).name.equals("x"));

        assertTrue(firstChild.getChild(1) instanceof PyAddNode);
        assertTrue(((PyNumberNode) firstChild.getChild(1).getChild(0)).number == 5);
        assertTrue(((PyNumberNode) firstChild.getChild(1).getChild(1)).number == 1);
    }

    @Test
    public void defFunction() {
        String code = "" +
                "def f(a,b):\n" +
                "    6\n" +
                "    x=5\n" +
                "    druha=2*x+1" +
                "\n";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        PyNode firstChild = rootNode.getChild(0);

        assertTrue(firstChild instanceof PyDefFuncNode);

        assertTrue(firstChild.getChild(0) instanceof PyNumberNode); // 6
        assertEquals(6, ((PyNumberNode) firstChild.getChild(0)).number);

        //:=
        //  {x}
        //  5
        assertTrue(firstChild.getChild(1) instanceof AssignNode);
        assertEquals("x", ((PySymbolNode)firstChild.getChild(1).getChild(0)).name);
        assertEquals(5, ((PyNumberNode)firstChild.getChild(1).getChild(1)).number);

        //:=
        //  {druha}
        //  +
        //    *
        //      2
        //      {x}
        //    1
        assertTrue(firstChild.getChild(2) instanceof AssignNode);
        assertEquals("druha", ((PySymbolNode)firstChild.getChild(2).getChild(0)).name);

        assertTrue(firstChild.getChild(2).getChild(1) instanceof PyAddNode);
        assertTrue(firstChild.getChild(2).getChild(1).getChild(0) instanceof PyMultiplyNode);
        assertTrue(firstChild.getChild(2).getChild(1).getChild(1) instanceof PyNumberNode);
        assertEquals(1, ((PyNumberNode)firstChild.getChild(2).getChild(1).getChild(1)).number);

        assertTrue(firstChild.getChild(2).getChild(1).getChild(0).getChild(0) instanceof PyNumberNode);
        assertEquals(2, ((PyNumberNode)firstChild.getChild(2).getChild(1).getChild(0).getChild(0)).number);
        assertTrue(firstChild.getChild(2).getChild(1).getChild(0).getChild(1) instanceof PySymbolNode);
        assertEquals("x", ((PySymbolNode)firstChild.getChild(2).getChild(1).getChild(0).getChild(1)).name);
    }
}
