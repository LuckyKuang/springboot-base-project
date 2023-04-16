package com.luckykuang.auth.validation;

import com.luckykuang.auth.utils.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author luckykuang
 * @date 2023/4/11 13:58
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    @Override
    public void initialize(Mobile annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果手机号为空，默认不校验，即校验通过
        if (StringUtils.isBlank(value)) {
            return true;
        }
        // 校验手机
        return ValidationUtils.isMobile(value);
    }
}
