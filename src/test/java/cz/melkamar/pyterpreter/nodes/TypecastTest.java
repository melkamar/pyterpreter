package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.EnvironmentBuilder;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class TypecastTest {
    @Test
    public void castToInt() {
        String code = "" +
                "a = int(True)\n" +
                "b = int(False)\n" +
                "c = int(42)\n" +
                "d = int(\"42\")\n" +
                "e = int(9999999999999999999999999999999999999999999999999999999999)\n" +
                "f = int(\"9999999999999999999999999999999999999999999999999999999999\")\n" +
                "";
        Environment environment = new EnvironmentBuilder().createEnvironment();
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.run();

        Assert.assertTrue(rootNode.getFrameValue("a") instanceof Long);
        Assert.assertTrue(rootNode.getFrameValue("b") instanceof Long);
        Assert.assertTrue(rootNode.getFrameValue("c") instanceof Long);
        Assert.assertTrue(rootNode.getFrameValue("d") instanceof Long);
        Assert.assertTrue(rootNode.getFrameValue("e") instanceof BigInteger);
        Assert.assertTrue(rootNode.getFrameValue("f") instanceof BigInteger);
    }

    @Test
    public void castToStr() {
        String code = "" +
                "a = str(True)\n" +
                "b = str(False)\n" +
                "c = str(42)\n" +
                "d = str(\"42\")\n" +
                "e = str(9999999999999999999999999999999999999999999999999999999999)\n" +
                "f = str(\"9999999999999999999999999999999999999999999999999999999999\")\n" +
                "";
        Environment environment = new EnvironmentBuilder().createEnvironment();
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.run();

        Assert.assertTrue(rootNode.getFrameValue("a") instanceof String);
        Assert.assertTrue(rootNode.getFrameValue("b") instanceof String);
        Assert.assertTrue(rootNode.getFrameValue("c") instanceof String);
        Assert.assertTrue(rootNode.getFrameValue("d") instanceof String);
        Assert.assertTrue(rootNode.getFrameValue("e") instanceof String);
        Assert.assertTrue(rootNode.getFrameValue("f") instanceof String);

        Assert.assertEquals("True", rootNode.getFrameValue("a"));
        Assert.assertEquals("False", rootNode.getFrameValue("b"));
        Assert.assertEquals("42", rootNode.getFrameValue("c"));
        Assert.assertEquals("42", rootNode.getFrameValue("d"));
        Assert.assertEquals("9999999999999999999999999999999999999999999999999999999999", rootNode.getFrameValue("e"));
        Assert.assertEquals("9999999999999999999999999999999999999999999999999999999999", rootNode.getFrameValue("f"));
    }
}
