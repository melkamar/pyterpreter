package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Assert;
import org.junit.Test;

public class BinaryNodeTest {
    @Test
    public void addition() {
        String code = "1+2+3+4+5+6";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        CallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
        Object result = target.call();

        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(1 + 2 + 3 + 4 + 5 + 6, (long) result);

        System.out.println("RESULT: " + result);
    }
}
