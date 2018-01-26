package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.functions.UserFunction;
import cz.melkamar.pyterpreter.nodes.PyFuncRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 16.01.2018 19:20.
 */
public class ExecuteTest {
    @Test
    public void addition() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        assertEquals(1L + 2 + 3 + 4 + 5 + 6, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void subtraction() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("6-5-2-3");
        assertEquals(6L - 5 - 2 - 3, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void multiplication() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("1+2*3*4-2+3");
        assertEquals(1 + 2 * 3 * 4 - 2 + 3, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void division() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("1+4/2-2");
        assertEquals(1 + 4 / 2 - 2, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void parensPreference() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("(((2+4/2)-2)*4)/2");
        assertEquals((((2 + 4 / 2) - 2) * 4) / 2, ((Long) rootNode.execute()).longValue());
    }


    @Test
    public void assignment() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("x = 5 + 1");
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("x"));
        assertEquals(6, (long) env.getValue("x"));
    }

    @Test
    public void assignmentNested() {
        PyFuncRootNode rootNode = SimpleParseTree.astFromCode("x = 5 + 1\ny=x+1");
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("x"));
        assertTrue(env.contains("y"));
        assertEquals(6, (long) env.getValue("x"));
        assertEquals(7, (long) env.getValue("y"));

    }

    @Test
    public void funcDef() {
        String code = "" +
                "def f(a,b):\n" +
                "    6\n" +
                "    x=5\n" +
                "    druha=2*x+1" +
                "\n";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("f"));
        assertFalse(env.contains("x"));
        assertFalse(env.contains("druha"));

        assertTrue(env.getValue("f") instanceof UserFunction);
    }

    @Test
    public void funcCall() {
        String code = "" +
                "def f(a,b):\n" +
                "    x=a+b+2\n" +
                "    return x\n" +
                "\n" +
                "def g():\n" +
                "    return 5\n" +
                "\n" +
                "res = f(1+2,2*g())";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("f"));
        assertTrue(env.contains("g"));
        assertTrue(env.contains("res"));
        assertFalse(env.contains("x"));

        assertTrue(env.getValue("f") instanceof UserFunction);
        assertEquals((1L + 2) + 2 + (2 * 5), env.getValue("res"));
    }

    /**
     * Just check if print function finishes without crashes - no easy was to intercept stdout.
     */
    @Test
    public void printFunction() {
        String code = "" +
                "print(1)\n" +
                "print(1*2+3)\n" +
                "x=4\n" +
                "print(x)\n" +
                "print(x+2)";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("x"));
    }
}
