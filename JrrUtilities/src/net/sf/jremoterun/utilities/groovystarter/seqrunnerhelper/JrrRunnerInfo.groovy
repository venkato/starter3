package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI;

import java.util.logging.Logger;

@CompileStatic
class JrrRunnerInfo implements RunnerInfoI{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    JrrRunnerPhaseI phase

    boolean before

    Runnable runnable1

}
