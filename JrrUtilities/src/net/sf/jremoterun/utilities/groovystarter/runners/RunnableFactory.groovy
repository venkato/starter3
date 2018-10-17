package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import java.util.concurrent.Callable
import java.util.logging.Logger

@CompileStatic
class RunnableFactory {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();





    static RunnableText createRunnerText(String text) {
        return createRunnerFromText(text, GroovyConfigLoaderJrr.configLoader)
    }

    static RunnableFile createRunner(File file) {
        return createRunnerFromFile2(file, GroovyConfigLoaderJrr.configLoader)
    }

    static RunnableFile createRunnerFromFile2(File file, GroovyConfigLoaderGeneric groovyClassLoader) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
        if (!file.isFile()) {
            throw new IllegalArgumentException("Not a file : ${file}")
        }
        return new RunnableFile(file, groovyClassLoader)
    }

    static RunnableText createRunnerFromText(String text, GroovyConfigLoaderGeneric groovyClassLoader) {
        return new RunnableText(text, groovyClassLoader)
    }


    static RunnableClosure createRunnerClosure(Callable callable) {
        return new RunnableClosure(callable)
    }

    static RunnableClassName2 createRunner(Class clazz) {
        assert clazz != null
        Runnable r = new RunnableClassName2(clazz)
        return r;
    }

    static RunnableClassName createRunner(String className) {
        assert className != null
        return new RunnableClassName(new ClRef(className));
    }


    static RunnableClassName createRunner(ClRefRef className) {
        return createRunnerFromClass(className, GroovyClassLoaderDefault.thisClassLoader)
    }

    static RunnableClassName createRunnerFromClass(ClRefRef cnr, ClassLoader classLoader) {
        assert cnr != null
        return  new RunnableClassName(cnr.getClRef(), classLoader)
    }




    static void runRunner(File file) {
        assert file.exists()
        Runnable runner = createRunner(file)
        runner.run()
    }

    static void runRunner(Class clazz) {
        assert clazz != null
        Runnable r = new RunnableClassName2(clazz)
        r.run()
    }

    static void runRunner(String className) {
        assert className != null
        Runnable r = new RunnableClassName(new ClRef(className));
        r.run()
    }

    static void runRunner(ClRefRef className) {
        if(className==null){
            throw new NullPointerException('class name is null')
        }
         createRunnerFromClass(className.getClRef(),  GroovyClassLoaderDefault.thisClassLoader).run()
    }


    static void runRunner2(ClRefRef className,ClassLoader classLoader2) {
        createRunnerFromClass(className, classLoader2).run()
    }


}
