package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
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


    static Map fromClass2(ClRefRef className) {
        assert className != null
        Map map = className.getClRef().newInstance3() as Map
        return map;
    }

    static Object fromClass4(ClRefRef className, Object param) {
        return fromClass3(className,RunnableFactory.thisClassLoader,param)
    }

//    static Object fromClass4(ClRefRef className, Object param) {
//        return fromClass3(className,RunnableFactory.thisClassLoader,param)
//    }

    static Object fromClass3(ClRefRef className,ClassLoader cl , Object param) {
        assert className != null
        Object instance3 = className.getClRef().newInstance2(cl)
        return LoadScriptFromFileUtils.runWithParams(instance3, param, null)
    }


}
