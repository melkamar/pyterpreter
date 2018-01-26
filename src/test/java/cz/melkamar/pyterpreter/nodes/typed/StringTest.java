package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyFuncRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StringTest {
    @Test
    public void assignment(){
        String code = "" +
                "x = \"hello\"\n" +
                "y ='world'";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue("hello".equals(env.getValue("x")));
        assertTrue("world".equals(env.getValue("y")));
    }

    @Test
    public void concat(){
        String code = "" +
                "x ='hello '+'world'";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertTrue("hello world".equals(env.getValue("x")));
    }
}
