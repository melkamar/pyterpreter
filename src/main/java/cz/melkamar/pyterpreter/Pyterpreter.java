package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import cz.melkamar.pyterpreter.exceptions.SystemExitException;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.PyTopProgramNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static cz.melkamar.pyterpreter.REPL.startRepl;

public class Pyterpreter {
    public static void main(String[] args) {
        System.err.println("== running on " + Truffle.getRuntime().getName());
        if (args.length == 0) startRepl();
        else if (args.length == 1) {
            try {
                PyTopProgramNode rootNode = null;
                File file = new File(args[0]);
                SimpleParseTree simpleParseTree = SimpleParseTree.fromFile(file);

                Environment environment = new EnvironmentBuilder().createEnvironment();
                rootNode = simpleParseTree.generateAST(environment);
                rootNode.run();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SystemExitException e) {
                System.exit(0);
            }
        } else if (args.length == 2) {
            if (args[0].equals("-r")) {
                runResourceFile(args);
            }
        } else {
            System.err.println("Usage TODO");
        }
    }

    private static void runResourceFile(String[] args) {
        PyTopProgramNode rootNode = null;
        try {
            System.err.println("Using file from resources: " + args[1]);

            InputStream in = Pyterpreter.class.getClassLoader().getResourceAsStream(args[1]);
            if (in == null) throw new FileNotFoundException("File " + args[1] + " does not exist in resources.");
            Scanner s = new Scanner(in).useDelimiter("\\A");

            String text = s.hasNext() ? s.next() : "";
            SimpleParseTree simpleParseTree = SimpleParseTree.genParseTree(text);
            Environment environment = new EnvironmentBuilder().createEnvironment();
            rootNode = simpleParseTree.generateAST(environment);
            rootNode.run();
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    public static Object runCodeForResult(String code) {
        Environment environment = new EnvironmentBuilder().createEnvironment();
        PyRootNode rootNode = SimpleParseTree.astFromCode(code, environment);
        CallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
        Object result = target.call();
        if (Environment.DEBUG_MODE) rootNode.print();
        return result;
    }
}
