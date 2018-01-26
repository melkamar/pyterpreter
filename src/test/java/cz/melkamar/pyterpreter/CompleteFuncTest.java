package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.PyFuncRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompleteFuncTest {
    @Test
    public void factorialRecursive() {
        String code = "" +
                "def fact(x):\n" +
                "    print(x)\n" +
                "    if x==0:\n" +
                "        return 1\n" +
                "    else:\n" +
                "        return fact(x-1) * x\n" +
                "\n" +
                "result = fact(6)";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(720L, env.getValue("result"));
    }

    @Test
    public void fibonacciRecursive() {
        String code = "" +
                "def fib(x):\n" +
                "    if x==1:\n" +
                "        return 1\n" +
                "    if x==2:\n" +
                "        return 1\n" +
                "    \n" +
                "    return fib(x-1)+fib(x-2)\n" +
                "\n" +
                "result = fib(10)";

        PyFuncRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals(55L, env.getValue("result"));
    }
}
