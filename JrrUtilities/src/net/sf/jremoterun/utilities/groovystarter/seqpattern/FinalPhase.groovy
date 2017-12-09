package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic;

@CompileStatic
public class FinalPhase implements JrrRunnerPhaseI{

    public static FinalPhase finalPhase = new FinalPhase();

    private FinalPhase() {
    }

//    @Override
//    public int offset() {
//        return Integer.MAX_VALUE;
//    }

    @Override
    public JrrRunnerPhaseI nextPhase() {
        throw new IllegalStateException("final phase");
    }
}
