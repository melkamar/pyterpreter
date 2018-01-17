package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;

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
        String code = "x=5+1";
        SimpleParseTree.printParseTree(code);
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        System.out.println(rootNode.execute(Environment.getDefaultEnvironment()));
    }
}
