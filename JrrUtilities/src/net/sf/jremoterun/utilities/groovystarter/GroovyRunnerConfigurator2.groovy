package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Logger

//import net.sf.jremoterun.utilities.mdep.DropshipClasspath
@CompileStatic
abstract class GroovyRunnerConfigurator2 extends Script {

    public static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.instance

    @Deprecated
    public static String question = ConsoleSymbols.question.s
//    public static String directoryBaseSelector = ':d'
//    public static String userBaseSelector = ':u'
//    public static String hostBaseSelector = ':h'

    AddFilesToUrlClassLoaderGroovy getAdder() {
        return gmrp.addFilesToClassLoader
    }

    static String getFirstParam() {
        if (gmrp.args.size() == 0) {
            return null
        }
        return gmrp.args[0]
    }

    static String getFirstParam2(Map shortcutsDescription) {
        String firstParam3 = getFirstParam()
        if (firstParam3 == ConsoleSymbols.question) {
            String msg = shortcutsDescription.collect { "${it.key} : ${it.value}" }.join('\n')
            StackTraceElement[] trace = new Exception().getStackTrace()
            StackTraceElement find = trace.find { it.className != GroovyRunnerConfigurator2.name }
            System.out.println """${find} :\n 
${msg}"""
            return null
        }
        return firstParam3

    }

    static String getFirstParam2(String shortcutsDescription) {
        String firstParam3 = getFirstParam()
        if (firstParam3 == ConsoleSymbols.question) {
            StackTraceElement[] trace = new Exception().getStackTrace()
            StackTraceElement find = trace.find { it.className != GroovyRunnerConfigurator2.name }
            System.out.println """${find} : 
${shortcutsDescription}"""
            return null
        }
        return firstParam3
    }

    static String removeFirstParam() {
        assert gmrp.args.size() > 0
        return gmrp.args.remove(0)
    }


    @Deprecated
    static Runnable createRunnerFromFile(File file) {
        return RunnableFactory.createRunner(file)
//        JrrUtilities3.checkFileExist(file)
//        if (!file.file) {
//            throw new IllegalArgumentException("Not a file : ${file}")
//        }
//        Runnable r = new RunnableFile(file,GroovyMethodRunnerParams.getInstance().groovyClassLoader)
//        return r;
    }


    @Deprecated
    static Runnable createRunnerFromClass(Class clazz) {
        return RunnableFactory.createRunner(clazz)
//        assert clazz != null
//        Runnable r = new RunnableClassName2(clazz)
//        return r;
    }

    @Deprecated
    static Runnable createRunnerFromClass(String className) {
        return RunnableFactory.createRunner(className)
//        assert className != null
//        Runnable r = new RunnableClassName(new ClRef(className));
//        return r;
    }

    @Deprecated
    static Runnable createRunnerFromClass(ClRef className) {
        return RunnableFactory.createRunner(className)
//        return createRunnerFromClass(className, JrrClassUtils.currentClassLoader)
    }

    @Deprecated
    static Runnable createRunnerFromClass(ClRef cnr, ClassLoader classLoader) {
        return RunnableFactory.createRunnerFromClass(cnr, classLoader)
    }

//    public static void runRunnableFromObject(Object obj) {
//        if (obj instanceof Runnable) {
//            Runnable r = obj as Runnable
//            r.run()
//        } else if (obj instanceof Script) {
//            Script s = (Script) obj;
//            s.run()
//        } else {
//            throw new Exception("unsupport type ${obj}")
//        }
//
//    }

    static Map createRunnerWithParamsFromClass(ClRef className, ClassLoader classLoader) {
        assert className != null
        Map map = className.newInstance2(classLoader) as Map
        return map;
    }

    @Override
    Object run() {
        doConfig()
        return null
    }

    Object getVar(String name) {
        Object propertyValue = binding.getProperty(name)
        if (propertyValue == null) {
            throw new NullPointerException("no such propertyValue ${name}, available : ${binding.variables.keySet()}")
        }
        return propertyValue
    }

    abstract void doConfig()


}
