package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StringTest {
    @Test
    public void assignment(){
        String code = "" +
                "x = \"hello\"\n" +
                "y ='world'";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue("hello".equals(env.getValue("x")));
        assertTrue("world".equals(env.getValue("y")));
    }
}
