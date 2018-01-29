package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Pyterpreter;
import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Assert;
import org.junit.Test;

public class FunctionTest {
    @Test
    public void defOnly() {
        String code = "" +
                "def f():\n" +
                "    return 5\n";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        Assert.assertNotNull(rootNode.getFrameValue("f"));
        Assert.assertNull(rootNode.getFrameValue("g"));
    }

    @Test
    public void noParams() {
        String code = "" +
                "def f():\n" +
                "    return 5\n" +
                "f()";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(5, (long) result);
        Assert.assertNotNull(rootNode.getFrameValue("f"));
        Assert.assertNull(rootNode.getFrameValue("g"));
    }

    @Test
    public void oneParam() {
        String code = "" +
                "def f(a):\n" +
                "    return a\n" +
                "f(6)";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(6, (long) result);
        Assert.assertNotNull(rootNode.getFrameValue("f"));
        Assert.assertNull(rootNode.getFrameValue("g"));
    }

    @Test
    public void twoParams() {
        String code = "" +
                "def f(a,b):\n" +
                "    return a+b\n" +
                "f(1,2)";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(3, (long) result);
        Assert.assertNotNull(rootNode.getFrameValue("f"));
        Assert.assertNull(rootNode.getFrameValue("g"));
    }

    @Test
    public void noReturn() {
        String code = "" +
                "def f():\n" +
                "    1+2\n" +
                "f()";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        Assert.assertTrue(result == null);
    }

    @Test(expected = UndefinedVariableException.class)
    public void undefined() {
        String code = "" +
                "def f():\n" +
                "    1+2\n" +
                "g()";
        Pyterpreter.runCodeForResult(code);
    }

    @Test
    public void scope() {
        String code = "" +
                "x=1\n" +
                "def f():\n" +
                "    x=2\n" +
                "    a=2\n" +
                "    return 3\n" +
                "y = f()\n" +
                "y";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        // TODO clean up FrameDescriptor handling, having it in SimpleParseTree is ugly.
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(3, (long) result);
        Assert.assertNotNull(rootNode.getFrameValue("f"));
        Assert.assertNull(rootNode.getFrameValue("g"));
        Assert.assertNull(rootNode.getFrameValue("a"));
    }

    @Test(expected = UndefinedVariableException.class)
    public void accessOutOfScopeVariable() {
        String code = "" +
                "def f():\n" +
                "    a=2\n" +
                "    return 3\n" +
                "f()\n" +
                "a";
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Object result = rootNode.run();

        // TODO clean up FrameDescriptor handling, having it in SimpleParseTree is ugly.
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(3, (long) result);
        Assert.assertNotNull(rootNode.getFrameValue("f"));
        Assert.assertNull(rootNode.getFrameValue("g"));
        Assert.assertNull(rootNode.getFrameValue("a"));
    }

}
