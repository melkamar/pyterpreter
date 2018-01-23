package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlowControlTest {
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
    public void iNot() {
        String code = "" +
                "if (not 1==2) and 2==2:\n" +
                "    a=1\n" +
                "\n" +
                "if 2==2 and 1==2:\n" +
                "    b=1\n" +
                "\n" +
                "if 1==1 and 2==2:\n" +
                "    c=1\n" +
                "    \n" +
                "if 1==1 and 2==2 and 3==3:\n" +
                "    d=1\n" +
                "\n" +
                "if 1==1 and 2==2 and 3==4:\n" +
                "    e=1\n" +
                "if not 0:\n" +
                "    f=1\n" +
                "if not 1:\n" +
                "    g=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("a"));
        assertFalse(env.contains("b"));
        assertEquals(1L, env.getValue("c"));
        assertEquals(1L, env.getValue("d"));
        assertFalse(env.contains("e"));
        assertEquals(1L, env.getValue("f"));
        assertFalse(env.contains("g"));
    }

    @Test
    public void ifAnd() {
        String code = "" +
                "if 1==2 and 2==2:\n" +
                "    a=1\n" +
                "\n" +
                "if 2==2 and 1==2:\n" +
                "    b=1\n" +
                "\n" +
                "if 1==1 and 2==2:\n" +
                "    c=1\n" +
                "    \n" +
                "if 1==1 and 2==2 and 3==3:\n" +
                "    d=1\n" +
                "\n" +
                "if 1==1 and 2==2 and 3==4:\n" +
                "    e=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertFalse(env.contains("a"));
        assertFalse(env.contains("b"));
        assertEquals(1L, env.getValue("c"));
        assertEquals(1L, env.getValue("d"));
        assertFalse(env.contains("e"));
    }

    @Test
    public void ifOr() {
        String code = "" +
                "if 1==2 or 2==2: \n" +
                "    a=1 \n" +
                " \n" +
                "if 2==2 or 1==2: \n" +
                "    b=1 \n" +
                " \n" +
                "if 1==2 or 3==2: \n" +
                "    c=1\n" +
                "    \n" +
                "if 1==2 or 3==2 or 4==4: \n" +
                "    d=1\n" +
                "    \n" +
                "if 1==1 or 2==2: \n" +
                "    e=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("a"));
        assertEquals(1L, env.getValue("b"));
        assertEquals(1L, env.getValue("d"));
        assertEquals(1L, env.getValue("e"));
        assertFalse(env.contains("c"));
    }

    @Test
    public void ifAndOr() {
        String code = "" +
                "if (1==2 or 2==2) and 3==3:\n" +
                "    a=1\n" +
                "    \n" +
                "if (1==2 or 1==2) and 3==3:\n" +
                "    b=1\n" +
                "    \n" +
                "if (2==2 or 1==2) and 2==3:\n" +
                "    c=1" ;

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("a"));
        assertFalse(env.contains("b"));
        assertFalse(env.contains("c"));
    }

    /**
     * Test that return from a function with no arguments works
     */
    @Test
    public void returnBlank() {
        String code = "" +
                "def f():\n" +
                "    return\n" +
                "f()";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.contains("f"));
    }


}
