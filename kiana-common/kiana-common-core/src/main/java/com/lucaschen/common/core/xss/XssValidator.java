package com.lucaschen.common.core.xss;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义 XSS 校验注解实现
 */
public class XssValidator implements ConstraintValidator<Xss, String> {
    private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";

    public static boolean containsHtml(final String value) {
        final StringBuilder sHtml = new StringBuilder();
        final Pattern pattern = Pattern.compile(HTML_PATTERN);
        final Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            sHtml.append(matcher.group());
        }
        return pattern.matcher(sHtml).matches();
    }

    @Override
    public boolean isValid(final String value,
                           final ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return !containsHtml(value);
    }
}