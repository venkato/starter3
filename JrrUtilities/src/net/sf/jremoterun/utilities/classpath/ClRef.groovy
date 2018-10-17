package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef

import java.util.logging.Logger

@CompileStatic
class ClRef implements ClRefRef, Serializable, Comparable<ClRef> {

    public static ClassLoader defaultClassLoader = ClRef.getClassLoader()


    public String className;

    ClRef(String c) {
        this.className = c
        if (c == null) {
            throw new NullPointerException('arg is null')
        }
        if (c.contains(' ')) {
            throw new IllegalArgumentException("class name contains space : ${c}")
        }
//        if (c.contains('\n')) {
//            throw new IllegalArgumentException("class name contains new line : ${c}")
//        }
//        if (c.contains('\r')) {
//            throw new IllegalArgumentException("class name contains new line : ${c}")
//        }
//        if (c.contains('\t')) {
//            throw new IllegalArgumentException("class name contains tab : ${c}")
//        }
        if (c.length() == 0) {
            throw new IllegalArgumentException('class name is empty')
        }
    }


    ClRef(Class clazz) {
        this(clazz.getName())
    }

    @Deprecated
    String getClassName() {
        return className
    }


    String getClassPath() {
        return className.replace('.', '/');
    }

    @Override
    ClRef getClRef() {
        return this
    }

    Class loadClass2() {
        return loadClass(defaultClassLoader)
    }

    Class loadClass(ClassLoader classLoader1) {
        assert classLoader1 != null
        Thread thread = Thread.currentThread()
        if (classLoader1 == thread.getContextClassLoader()) {
            return classLoader1.loadClass(className)
        }
        return loadClass23(classLoader1)

    }

    Class loadClass23(ClassLoader classLoader1) {
        Thread thread = Thread.currentThread()
        ClassLoader threadLoaderBefore = thread.getContextClassLoader()
        try {
            thread.setContextClassLoader(classLoader1)
            return classLoader1.loadClass(className)
        } finally {
            thread.setContextClassLoader(threadLoaderBefore)
        }
    }

    Object newInstance(ClassLoader classLoader) {
        return newInstance2(classLoader)
    }

    Object newInstance2(ClassLoader classLoader) {
        Class clazz = loadClass(classLoader)
        return clazz.newInstance()
    }


    Object newInstance3() {
        Class clazz = loadClass2()
        return clazz.newInstance()
    }

    @Override
    String toString() {
        return className
    }

    boolean equals(Object o) {
        if (o == null) {
            return false
        }
        if (this.is(o)) return true;
        if (getClass() != o.getClass()) return false;

        ClRef clRef = (ClRef) o;

        if (className != clRef.className) return false;

        return true;
    }

    int hashCode() {
        return (className != null ? className.hashCode() : 0)
    }

    @Override
    int compareTo(ClRef o1) {
        if (o1 == null) {
            return -1
        }
        if (className == null) {
            return -1
        }
        if (o1.className == null) {
            return 1
        }
        return className.compareTo(o1.className)
    }
}
