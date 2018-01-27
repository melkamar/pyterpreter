package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.File;
import java.io.IOException;

import static cz.melkamar.pyterpreter.REPL.startRepl;

public class Pyterpreter {
    public static void main(String[] args) {
        if (args.length == 0) startRepl();
        else if (args.length == 1){
            PyRootNode rootNode = null;
            try {
                File file = new File(args[0]);
                SimpleParseTree simpleParseTree = SimpleParseTree.fromFile(file);
                rootNode = simpleParseTree.generateAST();
//                Environment env = Environment.getDefaultEnvironment();

                CallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
                target.call();

                System.out.println("\n\nEnvironment (root):");
//                System.out.println(env);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Expected zero or one argument.");
        }
    }

    public static Object runCodeForResult(String code){
        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        CallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
        Object result = target.call();
        return result;
    }
}
