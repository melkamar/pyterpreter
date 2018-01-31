package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.PyTopProgramNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.IOException;

public class PyMain {
    public static void main(String[] args) throws IOException {
//        String code = "" +
//                "def f(a,b):\n" +
//                "    x=a+b+2\n" +
//                "    return x\n" +
//                "\n" +
//                "def g():\n" +
//                "    return 5\n" +
//                "\n"+
//                "res = f(1+2,2*g())";
//        REPL.startRepl();
        String code = "" +
                "def fact(x):\n" +
                "    print(x)\n" +
                "    if x==0:\n" +
                "        return 1\n" +
                "    else:\n" +
                "        return fact(x-1) * x\n" +
                "\n" +
                "result = fact(6)";

//        code = "" +
//                "if 1==2:\n" +
//                "    print(1)\n" +
//                "elif 2==3:\n" +
//                "    print(2)\n" +
//                "else:\n" +
//                "    print(3)";
//
//        code = "" +
//                "x = 0\n" +
//                "if x==0:\n" +
//                "    y=1\n" +
//                "else:\n" +
//                "    y=2";

//        code = "" +
//                "def pwr(number, limit):   \n" +
//                "    if number == limit:   \n" +
//                "        return            \n" +
//                "                                         \n" +
//                "    res = number*number   \n" +
//                "    print(number+\": \"+res)\n" +
//                "    pwr(number+1, limit)\n" +
//                "\n" +
//                "pwr(1, 20)\n" +
//                "";

        code = "" +
                "print('hello world')\n" +
                "";

        Environment environment = new EnvironmentBuilder().createEnvironment();
        SimpleParseTree.printParseTree(code);
        PyTopProgramNode rootNode = SimpleParseTree.astFromCode(code, environment);
        rootNode.print();
        rootNode.run();
    }
}
