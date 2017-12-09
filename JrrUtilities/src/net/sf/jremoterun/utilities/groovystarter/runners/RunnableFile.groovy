package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClassNameReference
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils

import java.util.logging.Logger

@CompileStatic
class RunnableFile implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    File file;

    GroovyClassLoader groovyClassLoader

    RunnableFile(File file, GroovyClassLoader groovyClassLoader) {
        this.file = file
        this.groovyClassLoader = groovyClassLoader
    }

    @Override
    void run() {
        Object obj  =LoadScriptFromFileUtils.loadScriptFromFile(file,groovyClassLoader)
//        assert obj!=null
//        Object obj = groovyMethodRunner.loadScriptFromFile(file);
//        RunnableFactory.runRunnableFromObject(obj)
    }




    @Override
    String toString() {
        return "${this.class.simpleName} : ${file}"
    }
}
