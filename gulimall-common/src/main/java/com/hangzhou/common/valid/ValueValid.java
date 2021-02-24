package com.hangzhou.common.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Constraint(validatedBy = { }): 指定校验器
 * @Target: 注解可标记类型
 * @Retention: 被它所注解的注解保留多久
 */
@Documented
@Constraint(validatedBy = { ValueValidConstraintValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ValueValid {
    /**
     * 校验提示信息
     * @return ValidationMessages.properties 配置文件中所对应的信息
     */
    String message() default "{com.hangzhou.common.valid.ValueValid.message}";

    /**
     * 分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 存放注解当中的值
     * @return
     */
    int[] value() default {};
}
