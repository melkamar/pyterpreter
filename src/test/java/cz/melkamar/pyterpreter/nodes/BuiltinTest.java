package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.EnvironmentBuilder;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    @Test
    public void input() {
        String input = "hello 42";
        String code = "" +
                "x = input()\n" +
                "";

        InputStream stdin = new ByteArrayInputStream(input.getBytes());
        Environment environment = new EnvironmentBuilder().setStdin(stdin).createEnvironment();

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.run();

        Assert.assertEquals("hello 42", rootNode.getFrameValue("x"));
    }

    @Test
    public void inputInFunc() {
        String input = "a\nb\nc\nd\ne\n";
        String code = "" +
                "def f():\n" +
                "    return \"[\"+input()+\"]\"\n" +
                "\n" +
                "x=5\n" +
                "s = ''\n" +
                "while x>0:\n" +
                "    s = s + f()\n" +
                "    x = x-1\n" +
                "";

        InputStream stdin = new ByteArrayInputStream(input.getBytes());
        Environment environment = new EnvironmentBuilder().setStdin(stdin).createEnvironment();

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.run();

        Assert.assertEquals("[a][b][c][d][e]", rootNode.getFrameValue("s"));
    }


    @Test
    public void time() {
        String code = "" +
                "x = time()\n" +
                "";

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
    }

    @Test
    public void timeSleepDiff() {
        String code = "" +
                "x = time()\n" +
                "sleep(2)\n" +
                "y = time()\n" +
                "diff = y-x" +
                "";

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        Assert.assertTrue((Long) rootNode.getFrameValue("diff") > 2);
    }

    @Test
    public void sleep() {
        String code = "" +
                "sleep(1)\n" +
                "";

        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();
    }
}
