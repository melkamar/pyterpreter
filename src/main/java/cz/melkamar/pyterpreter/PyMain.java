package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.functions.builtin.InputFunction;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
//               "";


        String stdin = "" +
                "example input -\n" +
                "and a second line." +
                "";
        InputStream inputStream = new ByteArrayInputStream(stdin.getBytes(StandardCharsets.UTF_8.name()));

        InputFunction.setStdin(inputStream);
        code = "" +
                "x = input()\n" +
                "print(x)\n" +
                "print(input())\n" +
                "";



        SimpleParseTree.printParseTree(code);
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.print();

        System.out.println(rootNode.execute(env));

        System.out.println("DONE. Environment:");
        System.out.println(env);
    }
}
