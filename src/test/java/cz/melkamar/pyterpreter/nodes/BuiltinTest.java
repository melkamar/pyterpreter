package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.EnvironmentBuilder;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BuiltinTest {
    @Test
    public void print() {
        String code = "" +
                "x = 'world'\n" +
                "print('hello ' + x)\n" +
                "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream stdout = new PrintStream(byteArrayOutputStream, true);
        Environment environment = new EnvironmentBuilder().setStdout(stdout).createEnvironment();

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.run();

        Assert.assertEquals("hello world\n", byteArrayOutputStream.toString());
    }

    @Test
    public void printInFunc() {
        String code = "" +
                "def f(val):\n" +
                "    print(\"- \"+val)\n" +
                "    \n" +
                "x=10\n" +
                "while x>0:\n" +
                "    f(x)\n" +
                "    x = x-1";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream stdout = new PrintStream(byteArrayOutputStream, true);
        Environment environment = new EnvironmentBuilder().setStdout(stdout).createEnvironment();

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.run();

        String expected = "- 10\n" +
                "- 9\n" +
                "- 8\n" +
                "- 7\n" +
                "- 6\n" +
                "- 5\n" +
                "- 4\n" +
                "- 3\n" +
                "- 2\n" +
                "- 1\n";
        Assert.assertEquals(expected, byteArrayOutputStream.toString());
    }

}
