package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.external.ParseTree;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import cz.melkamar.pyterpreter.parser.AstPrinter;
import cz.melkamar.pyterpreter.parser.ParserFacade;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PyMain {

    public static void main(String[] args) throws IOException {
//        doStuff();
        runTests();
        if (true) return;

        ParserFacade parserFacade = new ParserFacade();
        AstPrinter astPrinter = new AstPrinter();
//        astPrinter.print(parserFacade.parse(new File("examples/tiny.py")));

        File testFile = null;
        try {
            URI uri = PyMain.class.getResource("/tinier.py").toURI();
            testFile = new File(uri);
            astPrinter.print(parserFacade.parse(testFile));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void doStuff() {
        ParserFacade parserFacade = new ParserFacade();
        File testFile = null;
        try {
            URI uri = PyMain.class.getResource("/tinier.py").toURI();
            testFile = new File(uri);

            org.antlr.v4.runtime.tree.ParseTree parseTree = parserFacade.parse(testFile);
            ParseTree ast = new ParseTree(parseTree);
            System.out.println(ast);
            ast.generateAST();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }


    public static void runTests() {
        String code = "x=5+1";
        ParseTree.printParseTree(code);
        PyRootNode rootNode = ParseTree.astFromCode(code);
        System.out.println(rootNode.execute(Environment.getDefaultEnvironment()));
    }
}
