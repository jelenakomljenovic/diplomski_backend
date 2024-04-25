package com.example.university.exception;

import com.example.university.web.constants.ErrorMessageConstants;

public class UniversityNotFoundException extends ResourceNotFoundException {

    public UniversityNotFoundException(Long id) {
        super(String.format(ErrorMessageConstants.UNIVERSITY_NOT_FOUND_ERROR_MESSAGE_FORMAT, id));
    }

}
