package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FlowControlTest {
    @Test
    public void ifThen() {
        String code = "" +
                "x = 0\n" +
                "if x==0:\n" +
                "    y=1\n";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
    }

    @Test
    public void ifThenImplicitBool() {
        String code = "" +
                "x = 1\n" +
                "if x:\n" +
                "    y=1\n" +
                "\n" +
                "if 0:\n" +
                "    z=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
    }

    @Test
    public void ifThenImplicitBoolFuncCall() {
        String code = "" +
                "def f():\n" +
                "    return 1\n" +
                "\n" +
                "if f():\n" +
                "    y=1\n" +
                "\n" +
                "if 0:\n" +
                "    z=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
    }

    @Test
    public void ifThenElse() {
        String code = "" +
                "x = 1\n" +
                "if x==0:\n" +
                "    y=1\n" +
                "else:\n" +
                "    y=2";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(2L, rootNode.getFrameValue("y"));
    }

    @Test
    public void ifNot() {
        String code = "" +
                "if (not 1==2) and 2==2:\n" +
                "    a=1\n" +
                "\n" +
                "if 2==2 and 1==2:\n" +
                "    b=1\n" +
                "\n" +
                "if 1==1 and 2==2:\n" +
                "    c=1\n" +
                "    \n" +
                "if 1==1 and 2==2 and 3==3:\n" +
                "    d=1\n" +
                "\n" +
                "if 1==1 and 2==2 and 3==4:\n" +
                "    e=1\n" +
                "if not 0:\n" +
                "    f=1\n" +
                "if not 1:\n" +
                "    g=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("a"));
        assertNull(rootNode.getFrameValue("b"));
        assertEquals(1L, rootNode.getFrameValue("c"));
        assertEquals(1L, rootNode.getFrameValue("d"));
        assertNull(rootNode.getFrameValue("e"));
        assertEquals(1L, rootNode.getFrameValue("f"));
        assertNull(rootNode.getFrameValue("g"));
    }

    @Test
    public void ifAnd() {
        String code = "" +
                "if 1==2 and 2==2:\n" +
                "    a=1\n" +
                "\n" +
                "if 2==2 and 1==2:\n" +
                "    b=1\n" +
                "\n" +
                "if 1==1 and 2==2:\n" +
                "    c=1\n" +
                "    \n" +
                "if 1==1 and 2==2 and 3==3:\n" +
                "    d=1\n" +
                "\n" +
                "if 1==1 and 2==2 and 3==4:\n" +
                "    e=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertNull(rootNode.getFrameValue("a"));
        assertNull(rootNode.getFrameValue("b"));
        assertEquals(1L, rootNode.getFrameValue("c"));
        assertEquals(1L, rootNode.getFrameValue("d"));
        assertNull(rootNode.getFrameValue("e"));
    }

    @Test
    public void ifOr() {
        String code = "" +
                "if 1==2 or 2==2: \n" +
                "    a=1 \n" +
                " \n" +
                "if 2==2 or 1==2: \n" +
                "    b=1 \n" +
                " \n" +
                "if 1==2 or 3==2: \n" +
                "    c=1\n" +
                "    \n" +
                "if 1==2 or 3==2 or 4==4: \n" +
                "    d=1\n" +
                "    \n" +
                "if 1==1 or 2==2: \n" +
                "    e=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("a"));
        assertEquals(1L, rootNode.getFrameValue("b"));
        assertEquals(1L, rootNode.getFrameValue("d"));
        assertEquals(1L, rootNode.getFrameValue("e"));
        assertNull(rootNode.getFrameValue("c"));
    }

    @Test
    public void ifAndOr() {
        String code = "" +
                "if (1==2 or 2==2) and 3==3:\n" +
                "    a=1\n" +
                "    \n" +
                "if (1==2 or 1==2) and 3==3:\n" +
                "    b=1\n" +
                "    \n" +
                "if (2==2 or 1==2) and 2==3:\n" +
                "    c=1" ;

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("a"));
        assertNull(rootNode.getFrameValue("b"));
        assertNull(rootNode.getFrameValue("c"));
    }

    @Test
    public void greater() {
        String code = "" +
                "x = 5\n" +
                "if x>4:\n" +
                "    y=1\n" +
                "if x>5:\n" +
                "    z=1\n" +
                "if x>6:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
        assertNull(rootNode.getFrameValue("a"));
    }

    @Test
    public void greaterEqual() {
        String code = "" +
                "x = 5\n" +
                "if x>=4:\n" +
                "    y=1\n" +
                "if x>=5:\n" +
                "    z=1\n" +
                "if x>=6:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertEquals(1L, rootNode.getFrameValue("z"));
        assertNull(rootNode.getFrameValue("a"));
    }

    @Test
    public void equal() {
        String code = "" +
                "x = 5\n" +
                "if x==4:\n" +
                "    y=1\n" +
                "if x==5:\n" +
                "    z=1\n" +
                "if x==6:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertNull(rootNode.getFrameValue("y"));
        assertEquals(1L, rootNode.getFrameValue("z"));
        assertNull(rootNode.getFrameValue("a"));
    }

    @Test
    public void notEqual() {
        String code = "" +
                "x = 5\n" +
                "if x!=4:\n" +
                "    y=1\n" +
                "if x!=5:\n" +
                "    z=1\n" +
                "if x!=6:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
        assertEquals(1L, rootNode.getFrameValue("a"));
    }

    @Test
    public void lesser() {
        String code = "" +
                "x = 5\n" +
                "if x<4:\n" +
                "    y=1\n" +
                "if x<5:\n" +
                "    z=1\n" +
                "if x<6:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertNull(rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
        assertEquals(1L, rootNode.getFrameValue("a"));
    }

    @Test
    public void lesserEqual() {
        String code = "" +
                "x = 5\n" +
                "if x<=4:\n" +
                "    y=1\n" +
                "if x<=5:\n" +
                "    z=1\n" +
                "if x<=6:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertNull(rootNode.getFrameValue("y"));
        assertEquals(1L, rootNode.getFrameValue("z"));
        assertEquals(1L, rootNode.getFrameValue("a"));
    }

    @Test
    public void greaterBigNum() {
        String code = "" +
                "x = 9999999999999999999999999999999999999999999999999999999998\n" +
                "if x>9999999999999999999999999999999999999999999999999999999997:\n" +
                "    y=1\n" +
                "if x>9999999999999999999999999999999999999999999999999999999998:\n" +
                "    z=1\n" +
                "if x>9999999999999999999999999999999999999999999999999999999999:\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
        assertNull(rootNode.getFrameValue("a"));
    }

    @Test
    public void greaterString() {
        String code = "" +
                "x = 'abc'\n" +
                "if x>'ab':\n" +
                "    y=1\n" +
                "if x>'abc':\n" +
                "    z=1\n" +
                "if x>'abd':\n" +
                "    a=1";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        rootNode.run();

        assertEquals(1L, rootNode.getFrameValue("y"));
        assertNull(rootNode.getFrameValue("z"));
        assertNull(rootNode.getFrameValue("a"));
    }

}
