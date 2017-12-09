package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.JrrGroovyScriptRunner

import java.util.logging.Logger

@CompileStatic
public class GroovyMethodRunner {

    private static final Logger log = Logger.getLogger(GroovyMethodRunner.getName());

    volatile GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.getInstance()

    public static JrrGroovyScriptRunner groovyScriptRunner = new JrrGroovyScriptRunner();

//    public JrrRunnerPhaseI jrrRunnerPhase = JrrRunnerPhase.begin;


    void systemExit(String msgs) {
        println msgs
        if (gmrp.showExceptionInSwingWindowV) {
            throw new Exception(msgs)
        } else {
            runRunner(gmrp.onStartupError);
        }
    }


//    void jrrRunScript2(Script initialScript) {
//        String[] args = (String[]) initialScript.binding.getVariable('args')
//        assert args != null
//        jrrRunScript(initialScript, args);
//    }

    /**
     * This method is called
     * @param initialScript reference to script, which called this method
     * @param args command line arguments
     */
    void jrrRunScript(Object initialScript, List<String> args) {
        try {
            setArgs(initialScript, args);
            gmrp.seqPatternRunnerGmrp.start(JrrRunnerPhase.begin)
            //loop()
        } catch (Throwable ex) {
            onException(ex);
        }
    }

    void selfCheck() {
        if (gmrp.initCallStack == null) {
            gmrp.initCallStack = new Exception("Call stack")
        } else {
            log.severe("ERROR : already inited")
            System.err.println "Stacks before : "
            gmrp.initCallStack.printStackTrace()
            System.err.println "Stack current : "
            new Exception().printStackTrace()
            System.err.println "End stacks current"
            throw new Error("Already inited");
        }
    }

    void printUsageAndExit() {
        println """
    Usage :
        <path to groovy file> <method name> [method parameters] 
"""
        if (gmrp.showExceptionInSwingWindowV) {
            throw new Exception("wrong arguments")
        } else {
            System.exit 1
        }

    }

    void setArgs(Object initialScript, List<String> args) {
        gmrp.groovyMethodRunner = this;
        gmrp.args = args;//new ArrayList<>(args.toList());
        gmrp.starterScript = initialScript
        gmrp.setPhaseChanger(JrrRunnerPhase.selfCheck, this.&selfCheck)
        gmrp.setPhaseChanger(JrrRunnerPhase.userConfigLoaded, this.&loadUserConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.directoryConfigLoaded, this.&loadDirConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.hostConfigLinuxLoaded, this.&loadHostLinuxConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.hostConfigWindowsLoaded, this.&loadHostWindowConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.createGroovyClassLoader, this.&createGroovyClassLoader)
        gmrp.setPhaseChanger(JrrRunnerPhase.createClassLoaderAdder, this.&createClassLoaderAdder)
    }

    void loadUserConfig() {
        if (gmrp.groovyUserConfigRaw != null && gmrp.groovyUserConfigRaw.exists()) {
            loadScriptFromFile(gmrp.groovyUserConfigRaw)
        }
    }


    void loadDirConfig() {
        if (gmrp.groovyFolderConfigRaw != null && gmrp.groovyFolderConfigRaw.exists()) {
            loadScriptFromFile(gmrp.groovyFolderConfigRaw)
        }
    }



    void loadHostLinuxConfig() {
        if (gmrp.groovyHostConfigLinuxRaw != null && gmrp.groovyHostConfigLinuxRaw.exists()) {
            loadScriptFromFile(gmrp.groovyHostConfigLinuxRaw)
        }
    }

    void loadHostWindowConfig() {
        if (gmrp.groovyHostConfigWindowsRaw != null && gmrp.groovyHostConfigWindowsRaw.exists()) {
            loadScriptFromFile(gmrp.groovyHostConfigWindowsRaw)
        }
    }

    void createClassLoaderAdder() {
        if (gmrp.addFilesToClassLoaderSystem == null) {
            gmrp.addFilesToClassLoaderSystem = new AddFilesToUrlClassLoaderGroovy(ClassLoader.getSystemClassLoader() as URLClassLoader)
        }
        if (gmrp.addFilesToClassLoaderGroovy == null) {
            gmrp.addFilesToClassLoaderGroovy = new AddFilesToUrlClassLoaderGroovy(gmrp.groovyClassLoader)
        }
        if (gmrp.addFilesToClassLoader == null) {
            if (gmrp.addClasspathToSystemClassLoader) {
                gmrp.addFilesToClassLoader = gmrp.addFilesToClassLoaderSystem;
            } else {
                gmrp.addFilesToClassLoader = gmrp.addFilesToClassLoaderGroovy;
            }
            URLClassLoader classLoaderUse = findCorrectClassloader()
            gmrp.addFilesToClassLoader = new AddFilesToUrlClassLoaderGroovy(classLoaderUse)
        }
        if(gmrp.addFilesToClassLoaderClassaderOfGroovy==null) {
            if (ClassLoader.getSystemClassLoader() == GroovyObject.classLoader) {
                gmrp.addFilesToClassLoaderClassaderOfGroovy = gmrp.addFilesToClassLoaderSystem
            }else{
                if (GroovyObject.classLoader instanceof URLClassLoader) {
//                    URLClassLoader  = (URLClassLoader) GroovyObject.classLoader;
                    gmrp.addFilesToClassLoaderClassaderOfGroovy =  new AddFilesToUrlClassLoaderGroovy(GroovyObject.classLoader as URLClassLoader)

                }
            }
        }
    }

    void createGroovyClassLoader() {
        gmrp.groovyClassLoader = (GroovyClassLoader) gmrp.starterScript.class.classLoader
        Thread.currentThread().setContextClassLoader(gmrp.groovyClassLoader);
    }

    void onException(Throwable exception) {
        gmrp.onExceptionOccured.newValue(exception)
    }

    URLClassLoader findCorrectClassloader() {
        URLClassLoader classLoaderUse;
        if (gmrp.addClasspathToSystemClassLoader) {
            classLoaderUse = (URLClassLoader) ClassLoader.getSystemClassLoader();
//            Thread.currentThread().setContextClassLoader(classLoaderUse)
        } else {
            classLoaderUse = gmrp.groovyClassLoader;
        }
        Thread.currentThread().setContextClassLoader(classLoaderUse)
        return classLoaderUse
    }

    Object loadScriptFromFile(File file) {
        if (gmrp.groovyClassLoader == null) {
            throw new IllegalStateException("gmrp.groovyClassLoader not set")
        }
        return LoadScriptFromFileUtils.loadScriptFromFile(file,gmrp.groovyClassLoader )
//        JrrUtilities3.checkFileExist(file)
//        file = file.absoluteFile.canonicalFile
//        Class clazz = gmrp.groovyClassLoader.parseClass(file)
//        assert clazz != null
//        Object instance = clazz.newInstance();
//        return JrrClassUtils.invokeJavaMethod(instance, 'run')
    }

//    static void runRunners(List<Runnable> runable) {
//        runable.each { it.run() }
//    }

    @Deprecated
    static void runRunner(Runnable runable) {
        if (runable != null) {
            runable.run()
        }
    }

}
