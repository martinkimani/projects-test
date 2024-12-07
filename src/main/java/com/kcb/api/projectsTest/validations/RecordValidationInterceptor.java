package com.kcb.api.projectsTest.validations;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kcb.api.projectsTest.exceptions.ErrorCode;
import com.kcb.api.projectsTest.exceptions.SystemException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;

/**
 *
 * @author martin
 */
public class RecordValidationInterceptor implements HandlerInterceptor {

    private static final Validator VALIDATOR;

    static {
        VALIDATOR =  Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> void validate(@Origin Constructor<T> constructor, @AllArguments Object[] args) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.forExecutables()
                .validateConstructorParameters(constructor, args);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(System.lineSeparator()));
            throw new ConstraintViolationException(message, violations);
        }
    }

}
