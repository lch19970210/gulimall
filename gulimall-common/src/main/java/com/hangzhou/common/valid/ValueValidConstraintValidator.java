package com.hangzhou.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 注解指定校验器
 * ConstraintValidator<注解名称 ,校验值类型>
 */
public class ValueValidConstraintValidator implements ConstraintValidator<ValueValid ,Integer>{

    /**
     * 该注解是为了校验是否是0,1,所以该变量是为了存放固定值
     */
    private Set<Integer> set=new HashSet<>();

    /**
     * 初始化
     * @param constraintAnnotation 注解
     */
    @Override
    public void initialize(ValueValid constraintAnnotation) {
        int[] value = constraintAnnotation.value();
        for (int i : value) {
            set.add(i);
        }
    }

    /**
     * 注解校验
     * @param integer 需要校验的值
     * @param constraintValidatorContext 上下承接器
     * @return boolean
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
