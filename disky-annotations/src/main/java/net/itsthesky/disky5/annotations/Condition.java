package net.itsthesky.disky5.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Condition {

    String property() default "";

    ConditionType type() default ConditionType.BE;

    String[] patterns() default {};

    int[] negated() default {};
}
