package cz.melkamar.pyterpreter;

import org.junit.Assert;
import org.junit.Test;

public class TypeCastTest {
    @Test
    public void numberCast() {
        Assert.assertTrue(TypeCast.castToBoolean(1L));
        Assert.assertTrue(TypeCast.castToBoolean(-1L));
        Assert.assertFalse(TypeCast.castToBoolean(0L));
    }

    @Test
    public void stringCast() {
        Assert.assertTrue(TypeCast.castToBoolean("hello"));
        Assert.assertTrue(TypeCast.castToBoolean(" "));
        Assert.assertFalse(TypeCast.castToBoolean(""));
    }
}
