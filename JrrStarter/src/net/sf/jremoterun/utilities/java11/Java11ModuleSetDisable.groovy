package net.sf.jremoterun.utilities.java11

import groovy.transform.CompileStatic
import net.sf.jremoterun.SimpleJvmTiAgent
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.GroovyClassLoaderDefault
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
public class Java11ModuleSetDisable implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ClRef moduleCheckDisableImpl = new ClRef("net.sf.jremoterun.utilities.java11.ModuleCheckDisable");
    public static ClRef moduleCheckDisableImpl2 = new ClRef("net.sf.jremoterun.utilities.java11.ModuleCheckDisable");
    public static volatile boolean tried = false;
    public static volatile boolean tried2 = false;
    public static volatile boolean tryLoadJavassist = true;
    public static volatile boolean successful = false;
    public static volatile boolean successful2 = false;

    @Override
    public void run() {
        doIfNeeded();
    }


    public static boolean doIfNeeded2() {
        if (!tried2) {
            tried2 = true;
            if (isJava11()) {
                RunnableFactory.runRunner(moduleCheckDisableImpl2);
                log.info("java11 modules disabled2")
                successful2 = true;
                return true;
            }
            return true;
        }
        return successful2;
    }

    public static boolean doIfNeeded() {
        if (!tried) {
            tried = true;
            if (isJava11()) {
                if (SimpleJvmTiAgent.instrumentation == null) {
                    log.severe("failed java11 disabled : instrumentation is null")
                    return false;
                } else {
                    if (tryLoadJavassist) {
                        GroovyClassLoaderDefault.thisClassLoader.loadClass('javassist.CtClass')
                    }
                    RunnableFactory.runRunner(moduleCheckDisableImpl);
                    log.info("java11 modules disabled")
                    successful = true;
                    return true;
                }
            }
            return successful;
        }
        return successful;

    }


    public static boolean isJava11() {
        try {
            Java11ModuleSetDisable.class.getClassLoader().loadClass("java.lang.module.ModuleFinder");
            return true;
        } catch (ClassNotFoundException e) {
            log.log(Level.FINE, "failed load module class", e);
            return false;
        }
    }
}
