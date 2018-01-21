package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrueFalseNodeTest {
    @Test
    public void assignment(){
        String code = "" +
                "x = True\n" +
                "y = False\n" +
                "" +
                "if True or False:\n" +
                "    a=1\n" +
                "    \n" +
                "if True and False:\n" +
                "    b=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue(env.getValue("x") instanceof Boolean);
        assertTrue(env.getValue("y") instanceof Boolean);

        assertTrue((Boolean) env.getValue("x"));
        assertFalse((Boolean) env.getValue("y"));

        assertEquals(1L, env.getValue("a"));
        assertFalse(env.contains("b"));
    }
}
