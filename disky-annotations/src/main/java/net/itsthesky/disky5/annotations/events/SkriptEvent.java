package net.itsthesky.disky5.annotations.events;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SkriptEvent {

    String name();

    String[] patterns();

    Class<?> jda();

    boolean cancellable() default false;

    String[] intents() default {};
}
