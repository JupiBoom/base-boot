package zz.hujing.baseboot.core.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author : hujing
 * @date : 2019/10/14
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final Logger log = LoggerFactory.getLogger(PhoneValidator.class);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("the source value : {}", s);
        return Pattern.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", s);
    }
}
