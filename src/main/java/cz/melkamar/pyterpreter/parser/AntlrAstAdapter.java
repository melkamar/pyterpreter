package cz.melkamar.pyterpreter.parser;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import org.antlr.v4.runtime.RuleContext;

/**
 * Class to convert ANTLR-generated AST into our objects.
 */
public class AntlrAstAdapter {
    public PyNode convert(RuleContext root){
        PyNode pyRoot = new PyNode() {
            @Override
            public Object execute(Environment env) {
                return null;
            }
        };

        return recurseConvert(root, pyRoot);
    }

    private PyNode recurseConvert(RuleContext antlrNode, PyNode pyNode){

    }


}
