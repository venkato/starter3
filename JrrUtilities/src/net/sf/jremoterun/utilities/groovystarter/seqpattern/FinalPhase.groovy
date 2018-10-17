package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic;

@CompileStatic
public class FinalPhase implements JrrRunnerPhaseI {

    public static FinalPhase finalPhase = new FinalPhase();

    private FinalPhase() {
    }


    @Override
    public JrrRunnerPhaseI nextPhase() {
        throw new IllegalStateException("final phase");
    }
}
