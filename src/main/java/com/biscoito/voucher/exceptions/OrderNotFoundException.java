package com.biscoito.voucher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(final String message) {
        super(message);
    }
}
