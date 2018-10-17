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
    grHomeDirectoryConfigLoaded,
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
    jrrUtilsPhaseDone,
    ;


    @Override
    public JrrRunnerPhaseI nextPhase() {
        if (this == jrrUtilsPhaseDone) {
            JrrRunnerPhaseI next = GroovyMethodRunnerParams.gmrpn.jrrUtilsPhaseDoneAfter;
            if (next == null) {
                throw new IllegalStateException("GroovyMethodRunnerParams.jrrUtilsPhaseDoneAfter is null");
            }
            return next;
        } else {
             return NextPhaseEnumUtil.nextPhase(this,values())  as JrrRunnerPhase
        }
    }

}
