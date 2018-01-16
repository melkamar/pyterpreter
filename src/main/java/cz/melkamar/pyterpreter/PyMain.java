package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.antlr.Python3Lexer;
import cz.melkamar.pyterpreter.antlr.Python3Parser;
import cz.melkamar.pyterpreter.external.AST;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import cz.melkamar.pyterpreter.parser.AstPrinter;
import cz.melkamar.pyterpreter.parser.ParserFacade;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PyMain {

    public static void main(String[] args) throws IOException {
        doStuff();
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

            ParseTree parseTree = parserFacade.parse(testFile);
            AST ast = new AST(parseTree);
            System.out.println(ast);
            ast.generateAST();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }



    public static void runTests(){
        PyRootNode rootNode = AST.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        assert rootNode.children.size() == 1;
    }
}
