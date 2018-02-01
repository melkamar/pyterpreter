package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Pyterpreter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class BinaryNodeTest {
    @Test
    public void addition() {
        String code = "1+2+3+4+5+6";
        Object result = Pyterpreter.runCodeForResult(code);

        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(1 + 2 + 3 + 4 + 5 + 6, (long) result);

        System.out.println("RESULT: " + result);
    }

    @Test
    public void additionBig() {
        String code = Long.MAX_VALUE + "+" + Long.MAX_VALUE;
        Object result = Pyterpreter.runCodeForResult(code);

        Assert.assertTrue(result instanceof BigInteger);

        BigInteger expected = new BigInteger("" + Long.MAX_VALUE).add(new BigInteger("" + Long.MAX_VALUE));
        Assert.assertTrue(expected.equals(result));

        System.out.println("RESULT: " + result);
    }

    @Test
    public void additionString() {
        String code = "'hello'+' '+'world'";
        Object result = Pyterpreter.runCodeForResult(code);

        Assert.assertTrue(result instanceof String);
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void subtraction() {
        String code = "6-5-2-3";
        Object result = Pyterpreter.runCodeForResult(code);
        assertEquals(6L - 5 - 2 - 3, (long) result);
    }

    @Test
    public void multiplicationString() {
        String code = "'x' * 5";
        Object result = Pyterpreter.runCodeForResult(code);
        assertEquals("xxxxx", result);
    }

    @Test
    public void multiplication() {
        String code = "1+2*3*4+2+3";
        Object result = Pyterpreter.runCodeForResult(code);
        assertEquals(1 + 2 * 3 * 4 + 2 + 3, (long) result);
    }

    @Test
    public void division() {
        String code = "1+4/2-2";
        Object result = Pyterpreter.runCodeForResult(code);
        assertEquals(1 + 4 / 2 - 2, (long) result);
    }

    @Test
    public void parensPreference() {
        String code = "(((2+4/2)-2)*4)/2";
        Object result = Pyterpreter.runCodeForResult(code);
        assertEquals((((2 + 4 / 2) - 2) * 4) / 2, (long) result);
    }

    @Test
    public void modulo() {
        String code = "5%2";
        Object result = Pyterpreter.runCodeForResult(code);
        assertEquals(5 % 2, (long) result);
    }
}
