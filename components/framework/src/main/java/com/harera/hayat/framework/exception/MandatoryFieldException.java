package com.harera.hayat.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MandatoryFieldException extends FieldException {

    public MandatoryFieldException(String code, String field) {
        super(String.format("Field [%s] is mandatory", field), code, field);
    }

    public MandatoryFieldException(String field) {
        super(String.format("Field [%s] is mandatory", field), null, field);
    }
}
