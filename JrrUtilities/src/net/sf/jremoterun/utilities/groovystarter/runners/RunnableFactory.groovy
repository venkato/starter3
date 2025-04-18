package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import java.util.logging.Logger

@CompileStatic
class RunnableFactory {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ClassLoader thisClassLoader = JrrClassUtils.currentClassLoader
    public static GroovyClassLoader groovyClassLoader

    static GroovyClassLoader receiveGroovyClassLoader2() {
        receiveGroovyClassLoader()
        return groovyClassLoader
    }

    static void receiveGroovyClassLoader() {
        if (groovyClassLoader == null) {
            if (thisClassLoader instanceof GroovyClassLoader) {
                groovyClassLoader = (GroovyClassLoader) thisClassLoader;
            } else {
                groovyClassLoader = new GroovyClassLoader(thisClassLoader)
            }
        }
    }


    static Runnable createRunner(File file) {
        return createRunnerFromFile2(file, receiveGroovyClassLoader2())
    }

    static Runnable createRunnerFromFile2(File file, GroovyClassLoader groovyClassLoader) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
        if (!file.isFile()) {
            throw new IllegalArgumentException("Not a file : ${file}")
        }
        Runnable r = new RunnableFile(file, groovyClassLoader)
        return r;
    }


    static Runnable createRunner(Class clazz) {
        assert clazz != null
        Runnable r = new RunnableClassName2(clazz)
        return r;
    }

    static Runnable createRunner(String className) {
        assert className != null
        Runnable r = new RunnableClassName(new ClRef(className));
        return r;
    }

//    static Runnable createRunner(ClRefRef className) {
//        return createRunner(className.clRef)
//    }

    static Runnable createRunner(ClRefRef className) {
        return createRunnerFromClass(className, thisClassLoader)
    }

    static Runnable createRunnerFromClass(ClRefRef cnr, ClassLoader classLoader) {
        assert cnr != null
        RunnableClassName runnableClassName = new RunnableClassName(cnr.getClRef(), classLoader)
        return runnableClassName;
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
         createRunnerFromClass(className.getClRef(), thisClassLoader).run()
    }

//    static void runRunner2(ClRef className) {
//        createRunnerFromClass(className, thisClassLoader).run()
//    }

    static void runRunner2(ClRefRef className,ClassLoader classLoader2) {
        createRunnerFromClass(className, classLoader2).run()
    }





//    static void runRunnableFromObject(Object obj) {
//        assert obj != null
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

}
