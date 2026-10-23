package net.itsthesky.disky5.annotations.docs;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Documentation {

    String name();

    String[] description() default {};

    String[] examples() default {};

    String[] since() default {};

}
