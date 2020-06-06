package com.ly.storeserver.utils;

import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;

/**
 * @Description: bean校验器
 * @Author wangy
 * @Date 2020/4/10 19:11
 * @Version V1.0.0
 **/
@Component
public class ParamValidUtil {

    public <T> void valid(T object) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        ConstraintViolation<T> constraintViolation = getFirst(validate, null);
        if (constraintViolation != null) {
            throw new ServiceException(constraintViolation.getMessage(), RStatus.FAIL);
        }
    }

}
