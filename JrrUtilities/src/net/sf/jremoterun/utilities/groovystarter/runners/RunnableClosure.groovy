package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.concurrent.Callable
import java.util.logging.Logger

@CompileStatic
class RunnableClosure implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    Callable callable;

    RunnableClosure(Callable callable) {
        this.callable = callable
    }

    @Override
    void run() {
        callable.call()
    }



    @Override
    String toString() {
        return "${this.class.simpleName} : ${callable}"
    }
}
