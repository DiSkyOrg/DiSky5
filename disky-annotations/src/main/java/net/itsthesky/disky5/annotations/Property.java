package net.itsthesky.disky5.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Property {
    String value();

    boolean shared() default false;

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface Setter {
        String value();

        boolean shared() default false;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface Retrieve {
        String value();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface Adder {
        String value();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface Remover {
        String value();
    }
}
