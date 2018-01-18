package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}
