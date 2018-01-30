package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import cz.melkamar.pyterpreter.truffle.PyNoneType;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class AssignmentTest {
    @Test
    public void assignmentNumber() {
        String code = "x=5\ny=5*3+2";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(5L, rootNode.getFrameValue("x"));
        assertEquals(5L * 3 + 2, rootNode.getFrameValue("y"));
        assertNull(result);
    }

    @Test
    public void assignmentString() {
        String code = "x='hello'";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals("hello", rootNode.getFrameValue("x"));
        assertNull(result);
    }

    @Test
    public void assignmentBoolean() {
        String code = "x=True\ny=False";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertTrue((boolean) rootNode.getFrameValue("x"));
        assertFalse((boolean) rootNode.getFrameValue("y"));
        assertNull(result);
    }

    @Test
    public void assignmentFunction() {
        String code = "" +
                "def f():\n" +
                "    return 1\n" +
                "x=f\n" +
                "y=x" +
                "";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertTrue(rootNode.getFrameValue("f") instanceof PyFunction);
        assertTrue(rootNode.getFrameValue("x") instanceof PyFunction);
        assertTrue(rootNode.getFrameValue("x") instanceof PyFunction);
        assertEquals(rootNode.getFrameValue("f") instanceof PyFunction, rootNode.getFrameValue("x") instanceof PyFunction);
        assertEquals(rootNode.getFrameValue("f") instanceof PyFunction, rootNode.getFrameValue("y") instanceof PyFunction);
    }

    @Test
    public void assignmentBignumber() {
        BigInteger bigNum = new BigInteger(Long.MAX_VALUE + "").add(new BigInteger(Long.MAX_VALUE + ""));
        String code = "x=" + bigNum.toString();
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(bigNum.toString(), rootNode.getFrameValue("x").toString());
        assertNull(result);
    }

    @Test
    public void readNumber() {
        String code = "x=5\ny=5*3+2\nz=y";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(5L * 3 + 2, rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test
    public void readString() {
        String code = "x='hello'\nz=x";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals("hello", rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test
    public void readBoolean() {
        String code = "x=True\ny=False\nz=x";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertTrue((boolean) rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test
    public void readBignumber() {
        BigInteger bigNum = new BigInteger(Long.MAX_VALUE + "").add(new BigInteger(Long.MAX_VALUE + ""));
        String code = "x=" + bigNum.toString() + "\ny=x";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(bigNum.toString(), rootNode.getFrameValue("y").toString());
        assertNull(result);
    }

    @Test
    public void readNone() {
        String code = "x=None\ny=False\nz=x";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(PyNoneType.NONE_SINGLETON, rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test(expected = UndefinedVariableException.class)
    public void readUndefined() {
        String code = "x";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
    }
}
