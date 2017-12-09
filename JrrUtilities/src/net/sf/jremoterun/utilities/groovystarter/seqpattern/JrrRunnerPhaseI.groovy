package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic;

@CompileStatic
public interface JrrRunnerPhaseI {

//    int offset();

    JrrRunnerPhaseI nextPhase();
}
