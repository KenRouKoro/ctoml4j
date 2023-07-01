package cn.korostudio.ctoml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface OutputAnnotation {
    String value();
    Location at() default Location.Top;

}
