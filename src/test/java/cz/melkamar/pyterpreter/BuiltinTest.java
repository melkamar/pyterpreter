package cz.melkamar.pyterpreter;

import cz.melkamar.pyterpreter.functions.builtin.InputFunction;
import cz.melkamar.pyterpreter.nodes.PyRootNode;
import cz.melkamar.pyterpreter.parser.SimpleParseTree;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 24.01.2018 17:11.
 */
public class BuiltinTest {
    @Test
    public void testInput() throws UnsupportedEncodingException {
        String stdin = "" +
                "first input\n" +
                "and a second" +
                "";
        InputStream inputStream = new ByteArrayInputStream(stdin.getBytes(StandardCharsets.UTF_8.name()));

        InputFunction.setStdin(inputStream);
        String code = "" +
                "x = input()\n" +
                "y = input()\n" +
                "print('['+x+']['+y+']')" +
                "";

        PyRootNode rootNode = SimpleParseTree.astFromCode(code);
        Environment env = Environment.getDefaultEnvironment();
        rootNode.execute(env);

        assertEquals("first input", env.getValue("x"));
        assertEquals("and a second", env.getValue("y"));
    }
}
