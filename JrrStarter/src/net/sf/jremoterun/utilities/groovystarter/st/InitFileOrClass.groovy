package net.sf.jremoterun.utilities.groovystarter.st

import groovy.transform.CompileStatic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitFileOrClass {

    String value();
}
