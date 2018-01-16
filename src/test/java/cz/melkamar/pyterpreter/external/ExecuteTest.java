package cz.melkamar.pyterpreter.external;

import cz.melkamar.pyterpreter.nodes.template.PyRootNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 16.01.2018 19:20.
 */
public class ExecuteTest {
    @Test
    public void addition() {
        PyRootNode rootNode = AST.astFromCode("1 + 2 + 3 + 4 + 5 + 6");
        Assert.assertEquals(1L + 2 + 3 + 4 + 5 + 6, ((Long) rootNode.execute(null)).longValue());
    }

    @Test
    public void subtraction() {
        PyRootNode rootNode = AST.astFromCode("6-5-2-3");
        Assert.assertEquals(6L - 5 - 2 - 3, ((Long) rootNode.execute(null)).longValue());
    }
}
