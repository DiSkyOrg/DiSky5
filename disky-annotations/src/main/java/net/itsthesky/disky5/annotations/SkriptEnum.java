package net.itsthesky.disky5.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SkriptEnum {

    String name();

    Class<?> jda();

}
