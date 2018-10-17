package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack

import java.util.logging.Logger

@CompileStatic
class RunnableClRefWithParam implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter  {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public ClRefRef clRef;

    public Object param

    public ClassLoader classLoader = JrrClassUtils.getCurrentClassLoader()



    RunnerFrom creationInfo;
    Object callerInfo;

    RunnableClRefWithParam(ClRefRef clRef, Object param) {
        this.clRef = clRef
        this.param = param
    }

    @Override
    void run() {
        Object obj = clRef.clRef.newInstance2(classLoader);
        if (obj instanceof RunnerCreationCallStack) {
            obj.setCreationInfo(creationInfo)
        }
        if(obj instanceof  CallerInfo){
            obj.setCallerInfo(this)
        }
        LoadScriptFromFileUtils.runWithParams(obj, param, null)
    }


    @Override
    String toString() {
        return "${getClass().getSimpleName()} : ${clRef}"
    }

}
