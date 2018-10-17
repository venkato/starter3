package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom;

import java.util.logging.Logger;

@CompileStatic
class RunInPhases implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter  {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public List<? extends RunnerInfoI> infoList;
    RunnerFrom creationInfo;
    Object callerInfo;

    RunInPhases(List<? extends RunnerInfoI> infoList) {
        this.infoList = infoList
    }

    @Override
    void run() {
        runInPhases()
    }

    void runInPhases() {
        int countt = 0
        infoList.each {
            countt++
            try {
                addRunnerInfo(it)
            } catch (Throwable e) {
                log.info "failed on ${countt} ${it}"
                throw e;
            }
        }

    }

    void addRunnerInfo(RunnerInfoI runnerInfo) {
        GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrpn
        Runnable runnable1 = runnerInfo.getRunnable1()
        if (runnable1 instanceof RunnerCreationCallStack) {
            RunnerCreationCallStack runnerCreationCallStack = (RunnerCreationCallStack) runnable1;
            runnerCreationCallStack.setCreationInfo(creationInfo)
        }

        if(runnable1 instanceof  CallerInfo){
            runnable1.setCallerInfo(this)
        }
        gmrp.seqPatternRunnerGmrp.addListenerOrRunIfPassedImpl(runnerInfo.getPhase(), runnerInfo.isBefore(), runnable1)
    }

}
