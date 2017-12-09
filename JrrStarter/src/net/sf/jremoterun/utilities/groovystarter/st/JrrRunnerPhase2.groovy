package net.sf.jremoterun.utilities.groovystarter.st;

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.seqpattern.FinalPhase
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI;

@CompileStatic
public enum JrrRunnerPhase2 implements JrrRunnerPhaseI {

    // On start classpath status :
    // JrrJavasist - yes, Javassist - no
    afterCoreLibAdded,
    setSystemOut,
    userConfigEnriched,
    hostConfigLinuxEnriched,
    hostConfigWindowsEnriched,
    directoryConfigEnriched,
    setDependecyResolver,
    addClassPathFiles,
    afterClassPathSet,
    findMainClass,
    classImplementionTasks,
    createClassInstance,
    runTargetMethod,
    targetMethodFinishedNormally,
    normalExit;


//    static int maxBefore = JrrRunnerPhase.calcMaxValue() + 1;

//    @Override
//    public int offset() {
//        return ordinal() + maxBefore;
//    }


//    public static int calcMaxValue() {
//        int max = 0;
//        for (JrrRunnerPhase2 v : JrrRunnerPhase2.values()) {
//            max = Math.max(v.offset(), max);
//        }
//        return max;
//    }


    @Override
    public JrrRunnerPhaseI nextPhase() {
        if (this == normalExit) {
            return FinalPhase.finalPhase;
        } else {
            boolean nextIsMy = false;
            for (JrrRunnerPhase2 phaseI : values()) {
                if (nextIsMy) {
                    return phaseI;
                }
                if (phaseI == this) {
                    nextIsMy = true;
                }
            }
            throw new IllegalStateException("failed find next for " + this);
        }
    }


}
