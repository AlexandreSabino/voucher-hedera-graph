package com.biscoito.voucher.usecases;

import org.springframework.stereotype.Service;

@Service
public class TinybarsCalculator {

    //100_000_000 (100 milhoes) tinybar => 1 hbar => 12 cents dolar

    private static final long TINYBAR_FACTOR = 100_000_000;

    private static final long CENTS_DOLAR_ONE_HBAR = 12;

    private static final long FACTOR_DOLAR_TO_REAL = 4;

    private static final Double TINYBAR_REPRESENTS_ONE_CENT = (TINYBAR_FACTOR / (CENTS_DOLAR_ONE_HBAR * 1.0));

    public long toRealInCents(final long tinybars) {
        return (tinybars / TINYBAR_REPRESENTS_ONE_CENT.longValue()) * FACTOR_DOLAR_TO_REAL;
    }

    public long toTinybars(final long amountInCents) {
        final long amountInCentsDolar = amountInCents / 4;
        return (long) (TINYBAR_REPRESENTS_ONE_CENT * amountInCentsDolar);
    }
}
