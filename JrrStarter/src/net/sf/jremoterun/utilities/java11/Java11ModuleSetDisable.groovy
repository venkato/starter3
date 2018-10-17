package net.sf.jremoterun.utilities.java11

import groovy.transform.CompileStatic
import net.sf.jremoterun.SimpleJvmTiAgent
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
public class Java11ModuleSetDisable implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ClRef moduleCheckDisableImpl = new ClRef("net.sf.jremoterun.utilities.java11.ModuleCheckDisable");
    public static volatile boolean tried = false;
    public static volatile boolean successful = false;

    @Override
    public void run() {
        doIfNeeded();
    }


    public static void doIfNeeded() {
        if (!tried) {
            tried = true;
            if (isJava11()) {
                if(SimpleJvmTiAgent.instrumentation==null) {
                    log.severe("failed java11 disabled : instrumentation is null")
                }else {
                    RunnableFactory.runRunner(moduleCheckDisableImpl);
                    log.info("java11 disabled!!!")
                    successful = true;
                }
            }
        }

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
