package annotation;

import java.lang.annotation.*;

/**
 * 自定义注解 -表示方法允许远程调用
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE,ElementType.METHOD}) //表示只能用在类、接口、和方法上
@Documented
public @interface EnableRpc {

}
