package com.example.university.exception;

import com.example.university.web.constants.ErrorMessageConstants;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super(ErrorMessageConstants.FORBIDDEN_EXCEPTION);
    }
}
