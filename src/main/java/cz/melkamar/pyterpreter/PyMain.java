package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PyMain {

    public static void main(String[] args) throws IOException {
//        doStuff();
        runTests();
    }

    public static void doStuff() {
        File testFile;
        try {
            URI uri = PyMain.class.getResource("/tinier.py").toURI();
            testFile = new File(uri);

            SimpleParseTree ast = SimpleParseTree.fromFile(testFile);
            System.out.println(ast);
            ast.generateAST();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }


    public static void runTests() {
        String code = "" +
                "def f(a,b):\n" +
                "x=5\n" +
                "\n" +
                "f(1,a)\n";
        SimpleParseTree.printParseTree(code);
//        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
//        Environment env = Environment.getDefaultEnvironment();
//        System.out.println(rootNode.execute(env));
//
//        System.out.println("DONE. Environment:");
//        System.out.println(env);
    }
}
