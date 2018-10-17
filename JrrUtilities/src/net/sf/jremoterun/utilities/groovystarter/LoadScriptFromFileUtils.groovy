package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.FileScriptSource
import net.sf.jremoterun.utilities.groovystarter.runners.GroovyConfigLoaderGeneric

import java.util.logging.Logger

@CompileStatic
class LoadScriptFromFileUtils {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String varNameInScript = "a"

//    @Deprecated
//    public static volatile boolean checkFileEndsWithGroovy = true

//    @Deprecated
//    public static long maxGroovyFileSize = 1_000_000;


//    static Object loadScriptFromFile(File file, GroovyClassLoader groovyClassLoader) {
//        Object instance =new GroovyConfigLoaderGeneric(groovyClassLoader).parseConfig(file)
//        return runNoParams(instance, file)
//    }

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


//    static Object runScriptWithParamFromFile(File file, GroovyClassLoader groovyClassLoader, Object param) {
//        Object instance =new GroovyConfigLoaderGeneric(groovyClassLoader).parseConfig(file)
//        return runWithParams(instance, param, file)
//    }

    static Object runWithParams(Object instance, Object param, Object errorMsg) {
        assert instance != null
        if (instance instanceof GroovyConfigLoader2I) {
            instance.loadConfig(param)
            return null
        }
        if (instance instanceof GroovyConfigLoader1ParamsI) {
            return instance.loadConfig(param)
        }
        if (instance instanceof GroovyConfigLoader2ParamsI) {
            if(param==null){
                throw new IllegalStateException("param is null")
            }
            if (param instanceof List) {
                List listParam = (List) param;
                if(listParam.size()!=2){
                    throw new IllegalStateException("param size ${listParam.size()}  != 2")
                }
                return instance.loadConfig(listParam[0],listParam[1])
            }else{
                throw new IllegalStateException("param is not list :  ${param.getClass().getName()}")
            }

        }
        if (instance instanceof Map) {
            Map r = (Map) instance;
            return r.get(param)
        }
        if (instance instanceof Script) {
            Script s = (Script) instance;
            s.binding.setVariable(varNameInScript, param)
            return s.run()
        }
//        return JrrClassUtils.invokeJavaMethod(instance, 'run')
        String failedClassName = instance.getClass().getName();
        throw new IllegalArgumentException("stranage class ${failedClassName} ${errorMsg}")

    }


//    static Class loadClassFromFile(File file, GroovyClassLoader groovyClassLoader) {
//        assert groovyClassLoader != null
//        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
//        file = file.getAbsoluteFile().getCanonicalFile()
//        if(LoadScriptFromFileUtils.checkFileEndsWithGroovy){
//            if(!file.getName().endsWith('.groovy')){
//                throw new Exception("that is not groovy file : ${file.getAbsolutePath()}")
//            }
//            long length = file.length()
//            if (LoadScriptFromFileUtils.maxGroovyFileSize != 0 && length > LoadScriptFromFileUtils.maxGroovyFileSize) {
//                throw new Exception("file length too big ${length/1000} kbytes, ${file.getAbsolutePath()}")
//            }
//        }
//        Class clazz = groovyClassLoader.parseClass(file)
//        assert clazz != null
//        return clazz
//    }

}
