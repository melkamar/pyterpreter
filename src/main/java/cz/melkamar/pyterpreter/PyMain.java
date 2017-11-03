package cz.melkamar.pyterpreter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PyMain {

    public static void main(String[] args) throws IOException {
        ParserFacade parserFacade = new ParserFacade();
        AstPrinter astPrinter = new AstPrinter();
//        astPrinter.print(parserFacade.parse(new File("examples/tiny.py")));

        File testFile = null;
        try {
            URI uri = PyMain.class.getResource("/tiny.py").toURI();
            testFile = new File(uri);
            astPrinter.print(parserFacade.parse(testFile));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
