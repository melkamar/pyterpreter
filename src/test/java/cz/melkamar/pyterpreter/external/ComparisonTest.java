package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ComparisonTest {
    @Test
    public void equals() {
        String code = "" +
                "if 1==1:\n" +
                "    y=1\n" +
                "if 0==1:\n" +
                "    z=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("y"));
        assertFalse(env.contains("z"));
    }

    @Test
    public void notEquals() {
        String code = "" +
                "if 0 != 1:\n" +
                "    y=1\n" +
                "if 1!=1:\n" +
                "    z=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(1L, env.getValue("y"));
        assertFalse(env.contains("z"));
    }
}
