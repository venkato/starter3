package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter;

import java.util.logging.Logger;

@CompileStatic
class RunnableWithParamsFactory2<T> implements  CallerInfo, CallerInfoGetter{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public GroovyConfigLoaderGeneric<T> groovyScriptRunner = GroovyConfigLoaderJrr.configLoader
    Object callerInfo;

    public T instance;
    public String errorMsg;

    void check(){
        assert groovyScriptRunner != null
    }

    void createInstance(File file) {
        check()
        instance = groovyScriptRunner.parseConfig(file)
        setCallerInfo6()
    }

    void setCallerInfo6(){
        if(instance instanceof  CallerInfo){
            instance.setCallerInfo(this)
        }
    }

    void createInstance2(String text) {
        check()
        instance = groovyScriptRunner.parseConfig(text)
        setCallerInfo6()
    }

    Object runWithParamsImpl(Object param) {
        assert instance != null
        return LoadScriptFromFileUtils.runWithParams(instance, param, errorMsg);
    }

    Object loadSettingsWithParam(File file, Object param) {
        createInstance(file)
        try {
            return runWithParamsImpl(param);
        }catch (Throwable e){
            log.info "failed on ${file} ${e}"
            throw e
        }
    }

    Object loadSettingsWithParam(String text, Object param) {
        createInstance2(text)
        return runWithParamsImpl(param);
    }


}
