package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.concurrent.Callable
import java.util.logging.Logger

@CompileStatic
class RunRunnableInSeq implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter  {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public List<? extends Runnable> runners;
    RunnerFrom creationInfo;
    Object callerInfo;

    RunRunnableInSeq() {
        runners = []
    }

    RunRunnableInSeq(List<? extends Runnable> runners) {
        this.runners = runners
    }

    @Override
    void run() {
        runThem()
    }

    void runThem() {
        int countt = 0
        runners.each {
            countt++
            try {
                runClass(it)
            } catch (Throwable e) {
                log.info "failed on ${countt} ${it}"
                throw e;
            }
        }

    }

    void runClass(Runnable runner) {
        if (runner instanceof RunnerCreationCallStack) {
            RunnerCreationCallStack creationCallStack = (RunnerCreationCallStack) runner;
            creationCallStack.setCreationInfo(creationInfo)
        }
        if(runner instanceof  CallerInfo){
            runner.setCallerInfo(this)
        }
        runner.run()
    }

    void addRunnerCommon(Runnable runner) {
//        if (runner instanceof RunnerCreationCallStack) {
//            RunnerCreationCallStack creationCallStack = (RunnerCreationCallStack) runner;
//            creationCallStack.setCreationInfo(creationInfo)
//        }
        runners.add runner
    }

    void add(ClRefRef clRef) {
        addRunnerCommon RunnableFactory.createRunner(clRef)
    }

    void add(File clRef) {
        addRunnerCommon RunnableFactory.createRunner(clRef)
    }

    void add(Class clRef) {
        addRunnerCommon RunnableFactory.createRunner(clRef)
    }

    void add(Callable clRef) {
        addRunnerCommon RunnableFactory.createRunnerClosure(clRef)
    }

}
