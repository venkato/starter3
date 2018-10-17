package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI

@CompileStatic
interface RunnerInfoI {


    JrrRunnerPhaseI getPhase()

    boolean isBefore()

    Runnable getRunnable1();
}
