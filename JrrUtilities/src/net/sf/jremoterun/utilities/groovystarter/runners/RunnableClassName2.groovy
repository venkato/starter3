package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack

import java.util.logging.Logger

@CompileStatic
class RunnableClassName2 implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    Class clazz;

    RunnerFrom creationInfo;
    Object callerInfo;

    RunnableClassName2(Class clazz) {
        this.clazz = clazz
    }

    @Override
    void run() {
        Object obj = clazz.newInstance()
        if(obj instanceof RunnerCreationCallStack){
            obj.setCreationInfo(creationInfo)
        }

        if(obj instanceof  CallerInfo){
            obj.setCallerInfo(this)
        }
        LoadScriptFromFileUtils.runNoParams(obj,null)
    }


    @Override
    String toString() {
        return "${getClass().getSimpleName()} : ${clazz.getName()}"
    }
}
