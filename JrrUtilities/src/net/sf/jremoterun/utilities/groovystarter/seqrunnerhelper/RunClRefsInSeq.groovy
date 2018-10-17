package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableClassName
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Logger

@CompileStatic
class RunClRefsInSeq implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public List<? extends ClRefRef> clRefs;
    RunnerFrom creationInfo;
    Object callerInfo;

    RunClRefsInSeq(List<? extends ClRefRef> clRefs) {
        this.clRefs = clRefs
    }

    @Override
    void run() {
        runThem()
    }

    void runThem() {
        int countt = 0
        clRefs.each {
            countt++
            try {
                runClass(it)
            } catch (Throwable e) {
                log.info "failed on ${countt} ${it}"
                throw e;
            }
        }

    }

    void runClass(ClRefRef ref) {
        RunnableClassName runner = RunnableFactory.createRunner(ref)
        runner.setCreationInfo(creationInfo)
        runner.run()
    }

}
