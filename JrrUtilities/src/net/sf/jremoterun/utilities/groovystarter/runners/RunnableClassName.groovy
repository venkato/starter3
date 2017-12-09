package net.sf.jremoterun.utilities.groovystarter.runners;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class RunnableClassName implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ClRef cnr;

    ClassLoader classLoader = JrrClassUtils.currentClassLoader

    RunnableClassName(ClRef cnr) {
        this.cnr = cnr
    }

    RunnableClassName(ClRef cnr, ClassLoader classLoader) {
        this.cnr = cnr
        this.classLoader = classLoader
    }

    @Override
    void run() {
        Object obj = cnr.newInstance2(classLoader);
        LoadScriptFromFileUtils.runNoParams(obj,null)
    }

    @Override
    String toString() {
        return "${this.class.simpleName} : ${cnr}"
    }
}
