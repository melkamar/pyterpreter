package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Assert;
import org.junit.Test;

public class FunctionAdvancedTest {
    @Test
    public void recurse() {
        String code = "" +
                "sum_start = 10\n" +
                "\n" +
                "def recurse_sum(sum, number):\n" +
                "    if number:\n" +
                "        return recurse_sum(sum+number, number-1)\n" +
                "    else:\n" +
                "        return sum\n" +
                "\n" +
                "def sum_to_one(number):\n" +
                "    return recurse_sum(0, number)\n" +
                "\n" +
                "result = sum_to_one(sum_start)";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        System.out.println(rootNode);
        rootNode.run();

        Assert.assertTrue(rootNode.getFrameValue("result") instanceof Long);
        Assert.assertEquals(10+9+8+7+6+5+4+3+2+1L, rootNode.getFrameValue("result"));
    }
}
