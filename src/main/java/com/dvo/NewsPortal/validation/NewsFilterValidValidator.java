package com.dvo.NewsPortal.validation;

import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import jakarta.validation.ConstraintValidator;

public abstract class NewsFilterValidValidator implements ConstraintValidator<NewsFilterValid, FilterNewsRequest> {

}
