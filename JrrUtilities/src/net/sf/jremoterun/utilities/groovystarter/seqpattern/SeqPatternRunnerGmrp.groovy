package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.classpath.ClassNameReference
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableClassName

import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

@CompileStatic
class SeqPatternRunnerGmrp extends SeqPatternRunner {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    Map<JrrRunnerPhaseI, Runnable> phaseChanger = new ConcurrentHashMap()


    @Override
    Runnable getRunnableForPhase(JrrRunnerPhaseI jrrRunnerPhaseNew) {
        return phaseChanger.get(jrrRunnerPhaseNew)
    }


    void setPhaseChanger(JrrRunnerPhaseI phase, Runnable r) {
        if (isPhasePassed(phase)) {
            throw new Exception("Phase ${phase} passed, current : ${jrrRunnerPhase}")
        }
        phaseChanger.put(phase, r);
    }



    void setPhaseChanger(JrrRunnerPhaseI phase, ClRef r) {
        Runnable r5 = new RunnableClassName(r);
//        RunnerFrom runnerFrom = new RunnerFrom(r5)
        setPhaseChanger(phase, r5)
    }

}
