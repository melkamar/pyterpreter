package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.PyRootNode;
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
        REPL.startRepl();
        String code = "print(1)";

        SimpleParseTree.printParseTree(code);
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        System.out.println(rootNode.execute(env));

        System.out.println("DONE. Environment:");
        System.out.println(env);
    }
}
