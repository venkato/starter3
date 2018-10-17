package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.ClRef

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.logging.Level;
import java.util.logging.Logger;

@CompileStatic
class UrlClassLoaderAddFileJava11Aware {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static UrlClassLoaderAddFileJava11Aware classLoaderAddFile = new UrlClassLoaderAddFileJava11Aware()

//    private volatile boolean java11Try = true;
//    private volatile boolean java11Tried = false;

    public boolean java11TriedAndFailed = false

    public Method addUrlM;
    public Method addUrlMJava11;
    public Method addURLM
    public ClRef clRefJava11 = new ClRef("jdk.internal.loader.BuiltinClassLoader")
    public Class clJava11

    public Field ucpField

    void addFileMain(ClassLoader cl, File jrrpath) throws Exception {
        if (!jrrpath.exists()) {
            throw new FileNotFoundException(jrrpath.getAbsolutePath());
        }
        if (cl instanceof URLClassLoader) {
            addUrlToClassLoaderUrl(cl, jrrpath)
        } else {
            addNonUrlClassLoader(cl, jrrpath)
        }
    }

    void addUrlToClassLoaderUrl(URLClassLoader cl, File jrrpath) throws Exception {
        if (addUrlM == null) {
            addUrlM = JrrClassUtils.findMethodByParamTypes3(URLClassLoader, "addURL", URL);
        }
        addUrlM.invoke(cl, jrrpath.toURL());
    }

    void addNonUrlClassLoader(ClassLoader cl, File jrrpath) {
        checkCl(cl)
        if (!java11TriedAndFailed) {
            if (addUrlMJava11 == null) {
                try {
                    findAppendClassPathMethod()
                } catch (NoSuchMethodException e) {
                    java11TriedAndFailed = true
                    onExceptionV6(cl, e)
                }
            }
        }
        if (java11TriedAndFailed) {
            addUrlToClForJava11V3(cl, jrrpath)
        } else {
            assert addUrlMJava11 != null
            addUrlMJava11.invoke(cl, jrrpath.toString());
        }
    }

    void onExceptionV6(ClassLoader cl, Exception e) {
        log.log(Level.INFO, null, e);
    }

    void checkCl(ClassLoader cl) {
        if (clJava11 == null) {
            clJava11 = clRefJava11.loadClass(cl);
        }
        assert clJava11.isInstance(cl)
    }


    void findAppendClassPathMethod() {
        addUrlMJava11 = JrrClassUtils.findMethodByParamTypes4(clRefJava11, "appendClassPath", String.class);
    }


    public void addUrlToClForJava11V3(ClassLoader clObject, File fileToAdd) throws Exception {
        if (ucpField == null) {
            ucpField = JrrClassUtils.findField(clRefJava11, "ucp");
        }
        Object ucpObject = ucpField.get(clObject)
        if (addURLM == null) {
            addURLM = net.sf.jremoterun.utilities.JrrClassUtils.findMethodByParamTypes3(new ClRef('jdk.internal.loader.URLClassPath'), "addURL", URL.class);
//            addURLM = net.sf.jremoterun.utilities.JrrClassUtils.findMethodByParamTypes3(ucpObject.getClass(), "addURL", URL.class);
        }
        addURLM.invoke(ucpObject, fileToAdd.toURL());
    }


}
