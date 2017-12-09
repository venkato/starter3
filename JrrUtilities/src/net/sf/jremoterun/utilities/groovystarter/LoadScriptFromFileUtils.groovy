package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3

import java.util.logging.Logger

@CompileStatic
class LoadScriptFromFileUtils {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String varNameInScript = "a"


    static Object loadScriptFromFile(File file, GroovyClassLoader groovyClassLoader) {
        Class clazz = loadClassFromFile(file, groovyClassLoader)
        Object instance = clazz.newInstance();
        return runNoParams(instance, file)
    }

    static Object runNoParams(Object instance, Object errorMsg) {
        assert instance != null
        if (instance instanceof Runnable) {
            Runnable r = (Runnable) instance;
            r.run()
            return null
        }
        if (instance instanceof Script) {
            Script s = (Script) instance;
            return s.run()
        }
//        return JrrClassUtils.invokeJavaMethod(instance, 'run')
        if(errorMsg==null){
            throw new IllegalArgumentException("stranage class ${instance.class.name}")
        }else {
            throw new IllegalArgumentException("stranage class ${instance.class.name} ${errorMsg}")
        }
    }


    static Object runScriptWithParamFromFile(File file, GroovyClassLoader groovyClassLoader, Object param) {
        Class clazz = loadClassFromFile(file, groovyClassLoader)
        Object instance = clazz.newInstance();
        return runWithParams(instance, param, file)
    }

    static Object runWithParams(Object instance, Object param, Object errorMsg) {
        assert instance != null
        if (instance instanceof Map) {
            Map r = (Map) instance;
            return r.get(param)
        }
        if (instance instanceof GroovyConfigLoader) {
            instance.loadConfig(param)
            return null
        }
        if (instance instanceof Script) {
            Script s = (Script) instance;
            s.binding.setVariable(varNameInScript, param)
            return s.run()
        }
//        return JrrClassUtils.invokeJavaMethod(instance, 'run')
        throw new IllegalArgumentException("stranage class ${instance.class.getName()} ${errorMsg}")

    }

    static Class loadClassFromFile(File file, GroovyClassLoader groovyClassLoader) {
        assert groovyClassLoader != null
        JrrUtilities3.checkFileExist(file)
        file = file.absoluteFile.canonicalFile
        Class clazz = groovyClassLoader.parseClass(file)
        assert clazz != null
        return clazz
    }

}
