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
class RunnableFile implements Runnable, RunnerCreationCallStack, CallerInfo, CallerInfoGetter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public File file;
    public boolean logOnException = true;

    public GroovyConfigLoaderGeneric groovyClassLoader
    RunnerFrom creationInfo;
    Object callerInfo;

    RunnableFile(File file, GroovyConfigLoaderGeneric groovyClassLoader) {
        this.file = file
        this.groovyClassLoader = groovyClassLoader
        assert file!=null
        assert groovyClassLoader!=null
        assert file.exists()
    }

    @Override
    void run() {
        try {
            //Object obj = LoadScriptFromFileUtils.loadScriptFromFile(file, groovyClassLoader)
            Object instance =groovyClassLoader.parseConfig(file)
            if(instance instanceof RunnerCreationCallStack){
                instance.setCreationInfo(creationInfo)
            }
            if(instance instanceof  CallerInfo){
                instance.setCallerInfo(this)
            }
            LoadScriptFromFileUtils.runNoParams(instance, file)
        }catch (Throwable e){
            if(logOnException) {
                log.info("failed on ${file} ${e}")
            }
            throw e
        }
    }




    @Override
    String toString() {
        return "${getClass().getSimpleName()} : ${file}"
    }
}
