package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Logger


@CompileStatic
class GroovyRunnerConfigurator22  {

    public static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();



    static String removeFirstParam() {
        assert GroovyMethodRunnerParams.gmrpn.args.size() > 0
        return GroovyMethodRunnerParams.gmrpn.args.remove(0)
    }



    static String getFirstParam2(String shortcutsDescription) {
        String firstParam3 = getFirstParam()
        if (firstParam3 == ConsoleSymbols.question.s) {
            StackTraceElement[] trace = new Exception().getStackTrace()
            StackTraceElement find = trace.find { it.className != GroovyRunnerConfigurator22.name }
            System.out.println """${find} : 
${shortcutsDescription}"""
            return null
        }
        return firstParam3
    }



    static String getFirstParam() {
        if (GroovyMethodRunnerParams.gmrpn.args.size() == 0) {
            return null
        }
        return GroovyMethodRunnerParams.gmrpn.args[0]
    }



    static String getFirstParam2(Map shortcutsDescription) {
        String firstParam3 = getFirstParam()
        if (firstParam3 == ConsoleSymbols.question.s) {
            String msg = shortcutsDescription.collect { "${it.key} : ${it.value}" }.join('\n')
            StackTraceElement[] trace = new Exception().getStackTrace()
            StackTraceElement find = trace.find { it.className != GroovyRunnerConfigurator22.name }
            System.out.println """${find} :\n 
${msg}"""
            return null
        }
        return firstParam3

    }
}
