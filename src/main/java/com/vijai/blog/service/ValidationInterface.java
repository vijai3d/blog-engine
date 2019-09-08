package com.vijai.blog.service;

import com.vijai.blog.exception.BadRequestException;
import com.vijai.blog.util.AppConstants;

interface ValidationInterface {
    default void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
