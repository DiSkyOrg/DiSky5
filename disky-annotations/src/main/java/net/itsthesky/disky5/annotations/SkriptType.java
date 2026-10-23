package net.itsthesky.disky5.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SkriptType {

    String name();

    Class<?> jda();

    Class<?> parent() default Void.class;

}
