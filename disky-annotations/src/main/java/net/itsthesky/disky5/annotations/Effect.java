package net.itsthesky.disky5.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Effect {

    String[] value();

    boolean reason() default false;
}
