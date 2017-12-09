package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class SeqPatternRunnerEnum extends SeqPatternRunner {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    @Override
    Runnable getRunnableForPhase(JrrRunnerPhaseI phase) {
        JrrRunnerPhaseI2 phase1 = phase as JrrRunnerPhaseI2
//        log.info "${phase1} call method getRunnerForCurrentPhase"
        Runnable r = phase1.getRunnerForCurrentPhase();
        return r;
    }
}
