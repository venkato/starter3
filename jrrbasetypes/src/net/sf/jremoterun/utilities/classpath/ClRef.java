package net.sf.jremoterun.utilities.classpath;


import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef;

import java.io.Serializable;


public class ClRef implements ClRefRef, Serializable, Comparable<ClRef> {

    public static ClassLoader defaultClassLoader = ClRef.class.getClassLoader();

    private static final long serialVersionUID = 5826987063535505152L;

    public String className;

    public ClRef(String c) {
        this.className = c;
        if (c == null) {
            throw new NullPointerException("arg is null");
        }
        if (c.contains(" ")) {
            throw new IllegalArgumentException("class name contains space : " + c);
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
            throw new IllegalArgumentException("class name is empty");
        }
    }


    public ClRef(Class clazz) {
        this(clazz.getName());
    }

    @Deprecated
    public String getClassName() {
        return className;

    }


    public String getClassPath() {
        return className.replace('.', '/');
    }

//    @Override
    public ClRef getClRef() {
        return this;
    }

    public Class loadClass2() throws ClassNotFoundException {
        return loadClass(defaultClassLoader);
    }

    public Class loadClass(ClassLoader classLoader1) throws ClassNotFoundException {
        if( classLoader1 ==null){
            return Class.forName(className,true,null);
        }
        Thread thread = Thread.currentThread();
        if (classLoader1 == thread.getContextClassLoader()) {
            return classLoader1.loadClass(className);
        }
        return loadClass23(classLoader1);
    }

    public Class loadClass23(ClassLoader classLoader1) throws ClassNotFoundException {
        Thread thread = Thread.currentThread();
        ClassLoader threadLoaderBefore = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(classLoader1);
            return classLoader1.loadClass(className);
        } finally {
            thread.setContextClassLoader(threadLoaderBefore);
        }
    }

    public Object newInstance(ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return newInstance2(classLoader);
    }

    public Object newInstance2(ClassLoader classLoader) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class clazz = loadClass(classLoader);
        return clazz.newInstance();
    }


    public Object newInstance3() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class clazz = loadClass2();
        return clazz.newInstance();
    }

    @Override
    public String toString() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;

        ClRef clRef = (ClRef) o;

        if (!className.equals(clRef.className)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (className != null ? className.hashCode() : 0);
    }

//    @Override
    public int compareTo(ClRef o1) {
        if (o1 == null) {
            return -1;
        }
        if (className == null) {
            return -1;
        }
        if (o1.className == null) {
            return 1;
        }
        return className.compareTo(o1.className);
    }
}
