package com.biscoito.voucher.usecases;

import org.junit.Assert;
import org.junit.Test;

public class TinybarsCalculatorTest {

    //100_000_000 (100 milhoes) tinybar => 1 hbar => 12 cents dolar

    private final TinybarsCalculator tinybarsCalculator = new TinybarsCalculator();

    @Test
    public void toReal() {
        long real = tinybarsCalculator.toRealInCents(100_000_000);
        Assert.assertEquals(48, real);

        real = tinybarsCalculator.toRealInCents(100_000_000 * 2);
        Assert.assertEquals(96, real);

        real = tinybarsCalculator.toRealInCents(100_000_000 * 3);
        Assert.assertEquals(144, real);

        real = tinybarsCalculator.toRealInCents(100_000_000 * 10);
        Assert.assertEquals(480, real);

        real = tinybarsCalculator.toRealInCents(308_333_333);
        Assert.assertEquals(148, real);
    }

    @Test
    public void toTinybars() {
        long tinybars = tinybarsCalculator.toTinybars(48);
        Assert.assertEquals(100_000_000, tinybars);

        tinybars = tinybarsCalculator.toTinybars(96);
        Assert.assertEquals(200_000_000, tinybars);

        tinybars = tinybarsCalculator.toTinybars(144);
        Assert.assertEquals(300_000_000, tinybars);

        tinybars = tinybarsCalculator.toTinybars(480);
        Assert.assertEquals(100_000_000 * 10, tinybars);

        tinybars = tinybarsCalculator.toTinybars(148);
        Assert.assertEquals(308_333_333, tinybars);
    }
}