package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic;
import net.sf.jremoterun.JrrUtils;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.codehaus.groovy.runtime.MethodClosure;

import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
class RunnerFrom implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    // need static : if there are many nested call stack : log first, remaining skip
    static volatile boolean logCallStack = true;

    public Exception callStack2 = new Exception("CallStack")

    public Runnable impl;
    private String nestedName2

    RunnerFrom(Runnable impl) {
        this.impl = impl
        assert impl!=null
    }

    @Override
    void run() {
        try {
//            log.info "running : ${getNestedName()}"
            impl.run()
//            log.info "finished : ${getNestedName()}"
        } catch (Throwable e) {
//            log.info "fail"
            if (logCallStack) {
                logCallStack = false
//                log.info "failed in ${getNestedName()}"
//                e.printStackTrace()
//                Thread.dumpStack()
                Throwable rootException = JrrUtils.getRootException(e);
                StringBuilder sb4 = JrrClassUtils.printExceptionWithoutIgnoreClasses2(callStack2)
                log.info "${rootException} , callStack \n${sb4}"
            }
            throw e;
        }
    }

    String getNestedName(){
        if(nestedName2==null) {
            if (impl instanceof MethodClosure) {
                MethodClosure mc = (MethodClosure) impl;
                nestedName2 =  "${mc.getOwner()} ${mc.getMethod()}"
            }else {
                nestedName2 = impl.toString()
            }
        }
        return nestedName2
    }



    @Override
    String toString() {
        return "${this.class.simpleName} : ${getNestedName()}"
    }

}
