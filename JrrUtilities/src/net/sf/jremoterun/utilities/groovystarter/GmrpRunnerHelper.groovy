package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.classpath.MavenCommonUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory;

import java.util.logging.Logger;

@CompileStatic
class GmrpRunnerHelper {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static GmrpRunnerHelper helper;

    public static GmrpRunnerHelper get(){
        if(helper==null){
            helper=new GmrpRunnerHelper()
        }
        return helper
    }



    public String question = ConsoleSymbols.question.s

    GroovyMethodRunnerParams getGmrp(){
        return GroovyMethodRunnerParams.gmrpn
    }

    GroovyMethodRunnerParams getGmrpn(){
        return GroovyMethodRunnerParams.gmrpn
    }

    AddFilesToUrlClassLoaderGroovy getAdder(){
        return gmrp.addFilesToClassLoader
    }

    AddFilesToUrlClassLoaderGroovy getAddFilesToClassLoader(){
        return gmrp.addFilesToClassLoader
    }



    String getFirstParam() {
        return GroovyRunnerConfigurator22.getFirstParam()
    }

    String getFirstParam2(Map shortcutsDescription) {
        return GroovyRunnerConfigurator22.getFirstParam2(shortcutsDescription)
    }

    String getFirstParam2(String shortcutsDescription) {
        return GroovyRunnerConfigurator22.getFirstParam2(shortcutsDescription)
    }

    String removeFirstParam() {
        return GroovyRunnerConfigurator22.removeFirstParam()
    }




    Runnable createRunnerFromFile(File file) {
        return RunnableFactory.createRunner(file)
    }


    Runnable createRunnerFromClass(Class clazz) {
        return RunnableFactory.createRunner(clazz)
    }

    Runnable createRunnerFromClass(String className) {
        return RunnableFactory.createRunner(className)
    }

    Runnable createRunnerFromClass(ClRef className) {
        return RunnableFactory.createRunner(className)
    }

    Runnable createRunnerFromClass(ClRef cnr, ClassLoader classLoader) {
        return RunnableFactory.createRunnerFromClass(cnr, classLoader)
    }


    MavenDefaultSettings getMds(){
        return MavenDefaultSettings.mavenDefaultSettings
    }

    MavenCommonUtils getMcu() {
        return gmrp.addFilesToClassLoader.mavenCommonUtils
    }


    void startJmx(int port) {
        JrrUtils.creatJMXConnectorAndRMIRegistry(null, port, null, null);
    }

    Map createRunnerWithParamsFromClass(ClRef className, ClassLoader classLoader) {
        assert className != null
        Map map = className.newInstance2(classLoader) as Map
        return map;
    }

}
