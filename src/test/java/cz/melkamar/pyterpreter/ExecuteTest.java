package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.functions.UserFunction;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 16.01.2018 19:20.
 */
public class ExecuteTest {
    @Test
    public void addition() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        assertEquals(1L + 2 + 3 + 4 + 5 + 6, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void subtraction() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("6-5-2-3");
        assertEquals(6L - 5 - 2 - 3, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void multiplication() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("1+2*3*4-2+3");
        assertEquals(1 + 2 * 3 * 4 - 2 + 3, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void division() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("1+4/2-2");
        assertEquals(1 + 4 / 2 - 2, ((Long) rootNode.execute()).longValue());
    }

    @Test
    public void parensPreference() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("(((2+4/2)-2)*4)/2");
        assertEquals((((2 + 4 / 2) - 2) * 4) / 2, ((Long) rootNode.execute()).longValue());
    }


    @Test
    public void assignment() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("x = 5 + 1");
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("x"));
        assertEquals(6, (long) env.getValue("x"));
    }

    @Test
    public void assignmentNested() {
        PyRootNode rootNode = SimpleParseTree.astFromCode("x = 5 + 1\ny=x+1");
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

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
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

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
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

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("x"));
    }

    @Test
    public void ifThen() {
        String code = "" +
                "x = 0\n" +
                "if x==0:\n" +
                "    y=1\n";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("y"));
    }

    @Test
    public void ifThenImplicitBool() {
        String code = "" +
                "x = 1\n" +
                "if x:\n" +
                "    y=1\n" +
                "\n" +
                "if 0:\n" +
                "    z=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("y"));
        assertFalse(env.contains("z"));
    }

    @Test
    public void ifThenImplicitBoolFuncCall() {
        String code = "" +
                "def f():\n" +
                "    return 1\n" +
                "\n" +
                "if f():\n" +
                "    y=1\n" +
                "\n" +
                "if 0:\n" +
                "    z=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("y"));
        assertFalse(env.contains("z"));
    }

    @Test
    public void ifThenElse() {
        String code = "" +
                "x = 1\n" +
                "if x==0:\n" +
                "    y=1\n" +
                "else:\n" +
                "    y=2";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(2L, env.getValue("y"));
    }

    @Test
    public void recursionFactorial() {
        String code = "" +
                "def fact(x):\n" +
                "    print(x)\n" +
                "    if x==0:\n" +
                "        return 1\n" +
                "    else:\n" +
                "        return fact(x-1) * x\n" +
                "\n" +
                "result = fact(6)";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(720L, env.getValue("result"));
    }
}
