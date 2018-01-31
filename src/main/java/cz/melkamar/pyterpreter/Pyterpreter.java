package cz.melkamar.pyterpreter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.nodes.PyTopProgramNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;

import java.io.File;
import java.io.IOException;

import static cz.melkamar.pyterpreter.REPL.startRepl;

public class Pyterpreter {
    public static void main(String[] args) {
        if (args.length == 0) startRepl();
        else if (args.length == 1){
            PyTopProgramNode rootNode = null;
            try {
                File file = new File(args[0]);
                SimpleParseTree simpleParseTree = SimpleParseTree.fromFile(file);

                Environment environment = new EnvironmentBuilder().createEnvironment();
                rootNode = simpleParseTree.generateAST(environment);
                rootNode.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Expected zero or one argument.");
        }
    }

    public static Object runCodeForResult(String code){
        Environment environment = new EnvironmentBuilder().createEnvironment();
        PyRootNode rootNode = SimpleParseTree.astFromCode(code, environment);
        CallTarget target = Truffle.getRuntime().createCallTarget(rootNode);
        Object result = target.call();
        if (Environment.DEBUG_MODE) rootNode.print();
        return result;
    }
}
