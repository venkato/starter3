package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.groovystarter.runners.GroovyClassLoaderDefault
import net.sf.jremoterun.utilities.groovystarter.runners.GroovyConfigLoaderGeneric
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFile

import java.util.logging.Logger

@CompileStatic
public class GroovyMethodRunner {

    private static final Logger log = Logger.getLogger(GroovyMethodRunner.getName());

    public GroovyMethodRunnerParams gmrp

    GroovyMethodRunner(GroovyMethodRunnerParams gmrp) {
        this.gmrp = gmrp
    }

    void systemExit(String msgs) {
        println msgs
        if (gmrp.showExceptionInSwingWindowV) {
            throw new Exception(msgs)
        } else {
            runRunner(gmrp.onStartupError);
        }
    }

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
            SystemExit.exit 1
        }

    }

    void setArgs(Object initialScript, List<String> args) {
        gmrp.groovyMethodRunner = this;
        gmrp.args = args;//new ArrayList<>(args.toList());
        gmrp.starterScript = initialScript
        gmrp.setPhaseChanger(JrrRunnerPhase.selfCheck, this.&selfCheck)
        gmrp.setPhaseChanger(JrrRunnerPhase.userConfigLoaded, this.&loadUserConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.userConfigWinLoaded, this.&loadUserConfigShared)
        gmrp.setPhaseChanger(JrrRunnerPhase.userConfig2Loaded, this.&loadUserConfig2)
        gmrp.setPhaseChanger(JrrRunnerPhase.hostConfigLinuxLoaded, this.&loadHostLinuxConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.hostConfigWindowsLoaded, this.&loadHostWindowConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.directoryConfigLoaded, this.&loadDirConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase.grHomeDirectoryConfigLoaded, this.&grHomeDirectoryConfigLoaded)
        gmrp.setPhaseChanger(JrrRunnerPhase.createGroovyClassLoader, this.&createGroovyClassLoader)
        gmrp.setPhaseChanger(JrrRunnerPhase.createClassLoaderAdder, this.&createClassLoaderAdder)
        gmrp.setPhaseChanger(JrrRunnerPhase.userConfig2ClassesAdd, this.&loadUserConfig2ClassesAdd)
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

    void grHomeDirectoryConfigLoaded() {
        if (gmrp.loadGrHomeRawConfig) {
            File parentFile1 = gmrp.grHome.getParentFile()
            if (parentFile1 != null) {
                File grHomeConfigFile1 = new File(parentFile1, JrrStarterConstatnts.rawConfiGrHomeFileName)
                if (grHomeConfigFile1.exists()) {
                    loadScriptFromFile(grHomeConfigFile1)
                }
            }
        }
    }


    void loadHostLinuxConfig() {
        if (gmrp.groovyHostConfigLinuxRaw != null && gmrp.groovyHostConfigLinuxRaw.exists()) {
            loadScriptFromFile(gmrp.groovyHostConfigLinuxRaw)
        }
    }

    void loadUserConfig2ClassesAdd() {
        if (JrrStarterVariables2.getInstance().classesDir != null) {
            gmrp.addFilesToClassLoader.add(JrrStarterVariables2.getInstance().classesDir);
        }
    }

    void loadUserConfig2() {
        if (JrrStarterVariables2.getInstance().filesDir != null) {
            File configRaw = new File(JrrStarterVariables2.getInstance().filesDir, JrrStarterConstatnts.rawConfigFileName)
            if (configRaw.exists()) {
                loadScriptFromFile(configRaw);
            }
        }
    }


    void loadUserConfigShared() {
        if (gmrp.loadUserRawWindowsConfigShared) {
            File homeFromWindows = gmrp.detectHomeFromWindows()
            if (homeFromWindows != null) {
                String path1 = homeFromWindows.canonicalFile.absolutePath.replace('\\', '/')
                String path2 = gmrp.userHome.canonicalFile.absolutePath.replace('\\', '/')
                if (path1 == path2) {
                    // otherwise file loaded
                    File f = new File(homeFromWindows, JrrStarterConstatnts.rawConfigFileName)
                    if (f.exists()) {
                        loadScriptFromFile(f)
                    }
                }
            }
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
        if (gmrp.addFilesToClassLoaderClassaderOfGroovy == null) {
            if (ClassLoader.getSystemClassLoader() == GroovyObject.getClassLoader()) {
                gmrp.addFilesToClassLoaderClassaderOfGroovy = gmrp.addFilesToClassLoaderSystem
            } else {
                if (GroovyObject.classLoader instanceof URLClassLoader) {
//                    URLClassLoader  = (URLClassLoader) GroovyObject.classLoader;
                    gmrp.addFilesToClassLoaderClassaderOfGroovy = new AddFilesToUrlClassLoaderGroovy(GroovyObject.getClassLoader() as URLClassLoader)

                }
            }
        }
    }

    void createGroovyClassLoader() {
        gmrp.groovyClassLoader = (GroovyClassLoader) gmrp.starterScript.getClass().getClassLoader()
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

    public static boolean doClassLoaderCheck= true

    /**
     * to be used by frameworkOnly
     */
    void loadScriptFromFile(File file) {
        if (gmrp.groovyClassLoader == null) {
            throw new IllegalStateException("gmrp.groovyClassLoader not set")
        }
        if(doClassLoaderCheck) {
            if(! gmrp.groovyClassLoader.is(GroovyClassLoaderDefault.receiveGroovyClassLoader2())){
                Class clazz1 = gmrp.groovyClassLoader.loadClass(net.sf.jremoterun.utilities.groovystarter.runners.GroovyClassLoaderDefault.getName())
                assert net.sf.jremoterun.utilities.groovystarter.runners.GroovyClassLoaderDefault.is(clazz1)
            }
        }
        assert file.exists()
        RunnableFile runner = RunnableFactory.createRunner(file)
        runner.callerInfo = this
        runner.run()
    }


    @Deprecated
    static void runRunner(Runnable runable) {
        if (runable != null) {
            runable.run()
        }
    }

}
