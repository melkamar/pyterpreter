package cz.melkamar.pyterpreter.nodes;

import cz.melkamar.pyterpreter.Pyterpreter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class BasicTypesTest {
    @Test
    public void longType() {
        Object result = Pyterpreter.runCodeForResult("666");

        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(666, (long) result);
    }

    @Test
    public void stringType() {
        Object result = Pyterpreter.runCodeForResult("\"666 hello\"");

        Assert.assertTrue(result instanceof String);
        Assert.assertEquals("666 hello", (String) result);
    }

    @Test
    public void falseType() {
        Object result = Pyterpreter.runCodeForResult("False");

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, result);
    }

    @Test
    public void trueType() {
        Object result = Pyterpreter.runCodeForResult("True");

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, result);
    }

    @Test
    public void bigNumberType() {
        String bignum = "999999999999999999999999999999999999999999999999999999999999999";
        Object result = Pyterpreter.runCodeForResult(bignum);

        Assert.assertTrue(result instanceof BigInteger);
        Assert.assertEquals(new BigInteger(bignum), result);
    }
}
