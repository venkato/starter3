package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic

import java.util.logging.Logger

@CompileStatic
class ContextClassLoaderWrapper {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    static Runnable wrap(ClassLoader classLoader, Runnable runnable) {
        Runnable r = {
            wrap2(classLoader,runnable)
        }
        return r;
    }


    static void wrap2(ClassLoader classLoader, Runnable runnable) {
        Thread thread = Thread.currentThread()
        ClassLoader clBefore = thread.getContextClassLoader();
        thread.setContextClassLoader(classLoader)
        try {
            runnable.run();
        } finally {
            thread.setContextClassLoader(clBefore)
        }
    }

}
