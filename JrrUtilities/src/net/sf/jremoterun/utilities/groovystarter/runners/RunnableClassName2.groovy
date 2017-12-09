package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClassNameReference
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils

import java.util.logging.Logger

@CompileStatic
class RunnableClassName2 implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    Class clazz;

    RunnableClassName2(Class clazz) {
        this.clazz = clazz
    }

    @Override
    void run() {
        Object obj = clazz.newInstance()
        LoadScriptFromFileUtils.runNoParams(obj,null)
    }


    @Override
    String toString() {
        return "${this.class.simpleName} : ${clazz.name}"
    }
}
