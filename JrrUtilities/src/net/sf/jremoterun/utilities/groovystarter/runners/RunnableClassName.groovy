package net.sf.jremoterun.utilities.groovystarter.runners;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class RunnableClassName implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public ClRef clRef;

    public ClassLoader classLoader = JrrClassUtils.getCurrentClassLoader()

    RunnerFrom creationInfo;
    Object callerInfo;

    RunnableClassName(ClRef clRef) {
        this.clRef = clRef
    }

    RunnableClassName(ClRef clRef, ClassLoader classLoader) {
        this.clRef = clRef
        this.classLoader = classLoader
        assert clRef!=null
    }

    @Override
    void run() {
        Object obj = clRef.newInstance2(classLoader);
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
        return "${getClass().getSimpleName()} : ${clRef}"
    }

}
