package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils;

import java.util.logging.Logger;

@CompileStatic
class RunnableWithParamsFactory2<T> {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public GroovyConfigLoaderGeneric<T> groovyScriptRunner = GroovyConfigLoaderJrr.configLoader


    public T instance;
    public String errorMsg;

    void check(){
        assert groovyScriptRunner != null
    }

    void createInstance(File file) {
        check()
        instance = groovyScriptRunner.parseConfig(file)
    }

    void createInstance2(String text) {
        check()
        instance = groovyScriptRunner.parseConfig(text)
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
