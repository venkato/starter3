package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams;

import java.util.logging.Logger;

@CompileStatic
class RunnableCheckClassLoaded implements Runnable{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public ClRefRef clRef;

    RunnableCheckClassLoaded(ClRefRef clRef) {
        this.clRef = clRef
    }

    @Override
    void run() {
        clRef.clRef.loadClass2()
    }
}
