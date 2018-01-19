package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.IOException;

public class PyMain {

    public static void main(String[] args) throws IOException {
        String code = "" +
                "def f(a,b):\n" +
                "    x=a+b\n" +
                "\n" +
                "res = f(1,2+3)" ;
        SimpleParseTree.printParseTree(code);
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        System.out.println(rootNode.execute(env));

        System.out.println("DONE. Environment:");
        System.out.println(env);
    }
}
