package cz.melkamar.pyterpreter.parser;

import cz.melkamar.pyterpreter.Environment;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.nodes.template.PyNode;
import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import org.antlr.v4.runtime.RuleContext;

/**
 * Class to convert ANTLR-generated ParseTree into our objects.
 */
public class AntlrAstAdapter {
    public PyNode convert(RuleContext root){
        PyRootNode pyRoot = new PyRootNode();

        return recurseConvert(root, pyRoot);
    }

    private PyNode recurseConvert(RuleContext antlrNode, PyNode pyNode){
        throw new NotImplementedException();
    }


}
