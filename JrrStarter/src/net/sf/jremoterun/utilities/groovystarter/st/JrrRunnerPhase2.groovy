package net.sf.jremoterun.utilities.groovystarter.st;

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.seqpattern.FinalPhase
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI;

@CompileStatic
public enum JrrRunnerPhase2 implements JrrRunnerPhaseI {

    // On start classpath status :
    // JrrJavasist - yes, Javassist - yes
    afterCoreLibAdded,
    setSystemOut,
    java11ModuleDisable,
    userConfigEnriched,
    userConfigWinEnriched,
    userConfig2Enriched,
    hostConfigLinuxEnriched,
    hostConfigWindowsEnriched,
    directoryConfigEnriched,
    /**
     *  No custom code set by framework
     */
    setDependecyResolver,
    addClassPathFiles,
    /**
     *  No custom code set by framework
     */
    afterClassPathSet,
    findMainClass,
    classImplementionTasks,
    checkIfMainJavaMethod,
    createClassInstance,
    runTargetMethod,
    /**
     *  No custom code set by framework
     */
    targetMethodFinishedNormally,
    normalExit;


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
