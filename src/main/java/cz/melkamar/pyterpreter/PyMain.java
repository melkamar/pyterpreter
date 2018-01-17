package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
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
        String code = "x=5+1\ny=x";
        SimpleParseTree.printParseTree(code);
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment parent = Environment.getDefaultEnvironment();
        Environment middle = new Environment(parent);
        Environment env = new Environment(middle);
        System.out.println(rootNode.execute(env));

        System.out.println("DONE. Environment:");
        System.out.println(env);
    }
}
