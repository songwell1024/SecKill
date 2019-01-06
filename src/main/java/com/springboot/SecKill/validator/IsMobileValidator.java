package com.springboot.SecKill.validator;

import com.springboot.SecKill.util.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * JSR303具体的校验器
 * @author WilsonSong
 * @date 2018/8/2/002
 */
public class IsMobileValidator  implements ConstraintValidator<IsMobile, String> {

    public boolean required = false;
    //初始化
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    //校验
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){       //值是必须的就判断是否合法
            return ValidatorUtil.isMobile(s);     //不为空就判断格式
        }else {  //若不必须就判断是否有值
            if (StringUtils.isEmpty(s)){
                return true;
            }else {
                return ValidatorUtil.isMobile(s);     //不为空就判断格式
            }
        }
    }
}
