package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack
import org.codehaus.groovy.runtime.MethodClosure

import java.util.concurrent.Callable
import java.util.logging.Logger

@CompileStatic
class RunnableClosure implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public Callable callable;
    public String nestedName2;


    //RunnerFrom creationInfo;

    RunnableClosure(Callable callable) {
        this.callable = callable
    }

    @Override
    void run() {
        callable.call()
    }

    String getNestedName(){
        if(nestedName2==null) {
            if (callable instanceof MethodClosure) {
                MethodClosure mc = (MethodClosure) callable;
                nestedName2 =  "${mc.getOwner()} ${mc.getMethod()}"
            }else {
                nestedName2 = callable.toString()
            }
        }
        return nestedName2
    }





    @Override
    String toString() {
        return "${getClass().getSimpleName()} : ${getNestedName()}"
    }
}
