package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils

import java.util.logging.Logger

@CompileStatic
class RunnableWithParamsFactory {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    static Map fromClass1(ClRefRef className, ClassLoader classLoader) {
        assert className != null
        Map map = className.getClRef().newInstance2(classLoader) as Map
        return map;
    }


    static Map fromClass2(File file) {
        Object instance2 =new GroovyConfigLoaderGeneric(GroovyClassLoaderDefault.receiveGroovyClassLoader2()).parseConfig(file)
        return instance2 as Map;
    }

    static Map fromClass2(ClRefRef className) {
        assert className != null
        Map map = className.getClRef().newInstance3() as Map
        return map;
    }


    static Object runFile(File f, Object param) {
        return new RunnableWithParamsFactory2().loadSettingsWithParam(f,param)
    }


    static Object runClRef(ClRefRef className, Object param) {
        return runClRefClassloader(className,  GroovyClassLoaderDefault.thisClassLoader, param)
    }


    static Object runClRefClassloader(ClRefRef className, ClassLoader cl, Object param) {
        assert className != null
        Object instance3 = className.getClRef().newInstance2(cl)
        return LoadScriptFromFileUtils.runWithParams(instance3, param, null)
    }

    @Deprecated
    static void fromClass5(File f, Object param) {
        runFile(f,param)
    }

    @Deprecated
    static Object fromClass4(ClRefRef className, Object param) {
        return runClRef(className,param)
    }

    @Deprecated
    static Object fromClass3(ClRefRef className, ClassLoader cl, Object param) {
        return runClRefClassloader(className,cl,param)
    }


}
