package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom;

import java.util.logging.Logger;

@CompileStatic
interface RunnerCreationCallStack {


    void setCreationInfo(RunnerFrom runnerFrom)



}
