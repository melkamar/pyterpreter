package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;
import cz.melkamar.pyterpreter.functions.PyFunction;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import cz.melkamar.pyterpreter.types.PyNoneType;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class AssignmentTest {
    @Test
    public void assignmentNumber() {
        String code = "x=5\ny=5*3+2";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(5L, rootNode.getFrameValue("x"));
        assertEquals(5L * 3 + 2, rootNode.getFrameValue("y"));
        assertNull(result);
    }

    @Test
    public void assignmentString() {
        String code = "x='hello'";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals("hello", rootNode.getFrameValue("x"));
        assertNull(result);
    }

    @Test
    public void assignmentBoolean() {
        String code = "x=True\ny=False";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
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
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
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
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(bigNum.toString(), rootNode.getFrameValue("x").toString());
        assertNull(result);
    }

    @Test
    public void readNumber() {
        String code = "x=5\ny=5*3+2\nz=y";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(5L * 3 + 2, rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test
    public void readString() {
        String code = "x='hello'\nz=x";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals("hello", rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test
    public void readBoolean() {
        String code = "x=True\ny=False\nz=x";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertTrue((boolean) rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test
    public void readBignumber() {
        BigInteger bigNum = new BigInteger(Long.MAX_VALUE + "").add(new BigInteger(Long.MAX_VALUE + ""));
        String code = "x=" + bigNum.toString() + "\ny=x";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(bigNum.toString(), rootNode.getFrameValue("y").toString());
        assertNull(result);
    }

    @Test
    public void readNone() {
        String code = "x=None\ny=False\nz=x";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        assertEquals(PyNoneType.NONE_SINGLETON, rootNode.getFrameValue("z"));
        assertNull(result);
    }

    @Test(expected = UndefinedVariableException.class)
    public void readUndefined() {
        String code = "x";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
    }

    @Test
    public void increment() {
        String code = "" +
                "x = 1\n" +
                "x+=1";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
        assertEquals(2L, rootNode.getFrameValue("x"));
    }

    @Test
    public void decrement() {
        String code = "" +
                "x = 1\n" +
                "x-=1";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
        assertEquals(0L, rootNode.getFrameValue("x"));
    }

    @Test
    public void timesIncrement() {
        String code = "" +
                "x = 1\n" +
                "x*=4";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
        assertEquals(4L, rootNode.getFrameValue("x"));
    }

    @Test
    public void divDecrement() {
        String code = "" +
                "x = 6\n" +
                "x/=2";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
        assertEquals(3L, rootNode.getFrameValue("x"));
    }

    @Test
    public void modDecrement() {
        String code = "" +
                "x = 5\n" +
                "x%=2";
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
        assertEquals(1L, rootNode.getFrameValue("x"));
    }
}
