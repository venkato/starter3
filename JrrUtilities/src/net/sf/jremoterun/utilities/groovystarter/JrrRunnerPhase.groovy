package net.sf.jremoterun.utilities.groovystarter;

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI
import net.sf.jremoterun.utilities.groovystarter.seqpattern.NextPhaseEnumUtil;

@CompileStatic
public enum JrrRunnerPhase implements JrrRunnerPhaseI {


    begin,
    /**
     used in JrrInitV2
     */
    argsSet,
    selfCheck,
    createGroovyClassLoader,
    userConfigLoaded,
    userConfigWinLoaded,
    hostConfigLinuxLoaded,
    hostConfigWindowsLoaded,
    directoryConfigLoaded,
    createClassLoaderAdder,
    userConfig2ClassesAdd,
    userConfig2Loaded,
    /**
     used in JrrInitV2, adding javassist here
      */
    addJrrStarterLib,
    /**
     used in JrrInitV2
     */
    checks,
    /**
     used in JrrInit3
     */
    setNextPhase,
    /**
     No custom script set
     */
    jrrUtilsPhaseDone;


//    @Override
//    public int offset() {
//        return ordinal();
//    }

    @Override
    public JrrRunnerPhaseI nextPhase() {
        if (this == jrrUtilsPhaseDone) {
            JrrRunnerPhaseI next = null;
            next = GroovyMethodRunnerParams.getInstance().jrrUtilsPhaseDoneAfter;
            if (next == null) {
                throw new IllegalStateException("GroovyMethodRunnerParams.jrrUtilsPhaseDoneAfter is null");
            }
            return next;
        } else {

             return NextPhaseEnumUtil.nextPhase(this,values())  as JrrRunnerPhase
//            boolean nextIsMy = false;
//            for (JrrRunnerPhase phaseI : values()) {
//                if (nextIsMy) {
//                    return phaseI;
//                }
//                if (phaseI == this) {
//                    nextIsMy = true;
//                }
//            }
//            throw new IllegalStateException("failed find next for " + this);
        }
    }

//    public static int calcMaxValue() {
//        int max = 0;
//        for (JrrRunnerPhase v : JrrRunnerPhase.values()) {
//            max = Math.max(v.offset(), max);
//        }
//        return max;
//    }
}
