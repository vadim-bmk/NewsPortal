package com.dvo.NewsPortal.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = NewsFilterValidValidator.class)
public @interface NewsFilterValid {
    String message() default "Поля для пагинации должны быть указаны. Если вы указываете userName или categoryName, то они должны быть заполнены.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
