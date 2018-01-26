package cz.melkamar.pyterpreter.nodes.typed;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.PyFuncRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FloatingNumberTest {
    @Test
    public void assignment(){
        String code = "" +
                "x = 12.51\n" +
                "y = 7.2\n" +
                "a = x+y\n" +
                "b = x-y\n" +
                "c = x*y\n" +
                "d = x/y\n" +
                "";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(12.51, (double)env.getValue("x"), 0.001);
        assertEquals(7.2, (double)env.getValue("y"), 0.001);
        assertEquals(12.51+7.2, (double)env.getValue("a"), 0.001);
        assertEquals(12.51-7.2, (double)env.getValue("b"), 0.001);
        assertEquals(12.51*7.2, (double)env.getValue("c"), 0.001);
        assertEquals(12.51/7.2, (double)env.getValue("d"), 0.001);
    }
}
