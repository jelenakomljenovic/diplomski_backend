package com.example.university.exception;

import com.example.university.web.constants.ErrorMessageConstants;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super(String.format(ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE_FORMAT, id));
    }

    public UserNotFoundException(String username) {
        super(String.format(ErrorMessageConstants.USER_WITH_USERNAME_NOT_FOUND_ERROR_MESSAGE_FORMAT, username));
    }

}
