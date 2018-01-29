package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.parser.SimpleParseTree;
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
    public void assignmentBignumber() {
        BigInteger bigNum = new BigInteger(Long.MAX_VALUE+"").add(new BigInteger(Long.MAX_VALUE+""));
        String code = "x="+bigNum.toString();
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
        BigInteger bigNum = new BigInteger(Long.MAX_VALUE+"").add(new BigInteger(Long.MAX_VALUE+""));
        String code = "x="+bigNum.toString()+"\ny=x";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(bigNum.toString(), rootNode.getFrameValue("y").toString());
        assertNull(result);
    }
}