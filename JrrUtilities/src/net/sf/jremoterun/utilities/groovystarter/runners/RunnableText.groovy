package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack

import java.util.logging.Logger

@CompileStatic
class RunnableText implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public String text;

    public GroovyConfigLoaderGeneric groovyScriptRunner
    RunnerFrom creationInfo;
    Object callerInfo;

    RunnableText(String text, GroovyConfigLoaderGeneric groovyScriptRunner ) {
        this.text = text
        this.groovyScriptRunner = groovyScriptRunner
        assert groovyScriptRunner!=null
        assert text!=null
    }

    @Override
    void run() {
        Object script = groovyScriptRunner.parseConfig(text)
        if(script instanceof RunnerCreationCallStack){
            script.setCreationInfo(creationInfo)
        }

        if(script instanceof  CallerInfo){
            script.setCallerInfo(this)
        }
        net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils.runNoParams(script,null)
//        assert obj!=null
//        Object obj = groovyMethodRunner.loadScriptFromFile(file);
//        RunnableFactory.runRunnableFromObject(obj)
    }




    @Override
    String toString() {
        return "${getClass().getSimpleName()}"
    }
}
