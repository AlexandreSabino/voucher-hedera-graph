package com.biscoito.voucher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class InsuficientFundsException extends RuntimeException {

    public InsuficientFundsException(final String message) {
        super(message);
    }
}
