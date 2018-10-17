package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic;
import net.sf.jremoterun.JrrUtils;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI
import net.sf.jremoterun.utilities.groovystarter.seqpattern.PhaseState
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.JustStackTrace3
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack
import org.codehaus.groovy.runtime.MethodClosure

import java.util.logging.Logger

@CompileStatic
class RunnerFrom implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    // need static : if there are many nested call stack : log first, remaining skip
    static volatile boolean logCallStack = true;

    public JustStackTrace3 callStack2 = new JustStackTrace3()

    public Runnable impl;
    private String nestedName2
    public JrrRunnerPhaseI creationPhase;
    public PhaseState runBeforePhase;

    public RunnerFrom runnerFromParent;
    Object callerInfo

    RunnerFrom(Runnable impl) {
        this.impl = impl
        assert impl!=null
    }

    @Override
    void run() {
        try {
            impl.run()
        } catch (Throwable e) {
            onException(e)

        }
    }

    void onException(Throwable e){
        if (logCallStack) {
            logCallStack = false
            Throwable rootException = JrrUtils.getRootException(e);
            StringBuilder sb4 = JrrClassUtils.printExceptionWithoutIgnoreClasses2(callStack2)
            log.info "${rootException} , callStack \n${sb4}"
        }
        throw e;
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

    @Override
    void setCreationInfo(RunnerFrom runnerFrom) {
        runnerFromParent=runnerFrom
    }
}
