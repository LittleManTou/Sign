package com.mantou.anno;

import java.lang.annotation.*;
/**
 * @author mantou
 * @date 2021/10/19 15:12
 * @desc 用于加签验签的注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited    //表示能被子类继承
public @interface SignProcess {
    /**
     * 请求参数是否验签
     */
    boolean verify() default true ;

    /**
     * 响应参数是否加签
     */
    boolean sign() default true ;
}
