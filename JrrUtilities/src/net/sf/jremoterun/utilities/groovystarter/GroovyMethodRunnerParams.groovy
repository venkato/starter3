package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.SharedObjectsUtils
import net.sf.jremoterun.utilities.FileOutputStream2
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI
import net.sf.jremoterun.utilities.groovystarter.seqpattern.SeqPatternRunnerGmrp

import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
public class GroovyMethodRunnerParams {



    public static volatile enableManyClassLoaders = false

    public final Exception creationCallStack = new Exception();

    // internal
    @Deprecated
    public volatile Exception initCallStack

    public Date creationDate = new Date()

    public volatile File grHome;
    public volatile Object starterScript;


    public volatile GroovyClassLoader groovyClassLoader;

    private volatile File userHome = new File(System.getProperty("user.home"));


    public volatile File groovyUserConfigRaw = new File(userHome, "${JrrStarterConstatnts.jrrConfigDir}/${JrrStarterConstatnts.rawConfigFileName}");

    private volatile File userDir = new File(System.getProperty("user.dir"));

    public volatile File groovyFolderConfigRaw = new File(userDir, JrrStarterConstatnts.rawConfigFileName);

    public volatile File groovyHostConfigLinuxRaw = new File("${JrrStarterConstatnts.jrrConfigDirLinuxAllUsers}/${JrrStarterConstatnts.rawConfigFileName}");

    public volatile File groovyHostConfigWindowsRaw = new File("${JrrStarterConstatnts.jrrConfigDirWindowsAllUsers}/${JrrStarterConstatnts.rawConfigFileName}");

    public volatile SeqPatternRunnerGmrp seqPatternRunnerGmrp = new SeqPatternRunnerGmrp();

    public volatile List<String> args;

//    public Map<JrrRunnerPhaseI, List<Runnable>> listenersBefore = new ConcurrentHashMap()
//    public Map<JrrRunnerPhaseI, List<Runnable>> listenersAfter = new ConcurrentHashMap()


    public
    volatile boolean showExceptionInSwingWindowV = false;

    public volatile boolean addClasspathToSystemClassLoader = false;

    public volatile GroovyMethodRunner groovyMethodRunner

    public
    volatile NewValueListener<Throwable> onExceptionOccured = new PrintExceptionListener();


//    public volatile List<Runnable> phaseChangedListener = [];

    public volatile AddFilesToUrlClassLoaderGroovy addFilesToClassLoader;
    public volatile AddFilesToUrlClassLoaderGroovy addFilesToClassLoaderSystem;
    public volatile AddFilesToUrlClassLoaderGroovy addFilesToClassLoaderClassaderOfGroovy;
    public volatile AddFilesToUrlClassLoaderGroovy addFilesToClassLoaderGroovy;


    public volatile FileOutputStream2 fileOut;

    public volatile JrrRunnerPhaseI jrrUtilsPhaseDoneAfter;

    public volatile Runnable onStartupError = new Runnable() {

        public void run() {
            if (fileOut != null) {
                fileOut.flush()
            }
            System.exit(1);
        }
    };

    /**
     * use getInstance() method
     */
    @Deprecated
    static volatile GroovyMethodRunnerParams gmrp;


    static GroovyMethodRunnerParams getInstance() {
        if (gmrp != null) {
            return gmrp;
        }
        Map classloaders = SharedObjectsUtils.getGlobalMap();
        gmrp = (GroovyMethodRunnerParams) classloaders
                .get(GroovyMethodRunnerParams.name);
        if (gmrp != null) {
            if (enableManyClassLoaders) {
                println "GroovyMethodRunnerParams created in different classloader"
            } else {
                throw new Exception("GroovyMethodRunnerParams created in different classloader ${gmrp.class.classLoader} ${GroovyMethodRunnerParams.classLoader}");
            }
        }
        if (gmrp == null) {
            gmrp = new GroovyMethodRunnerParams();
        }
        return gmrp;
    }


    void setPhaseChanger(JrrRunnerPhaseI phase, ClRef r) {
        seqPatternRunnerGmrp.setPhaseChanger(phase, r)
    }

    @Deprecated
    void setPhaseChanger(JrrRunnerPhaseI phase, Runnable r) {
        seqPatternRunnerGmrp.setPhaseChanger(phase, r)
    }

    @Deprecated
    List<Runnable> getListeners(JrrRunnerPhaseI phase, boolean before) {
        return seqPatternRunnerGmrp.getListeners(phase, before)
    }


    @Deprecated
    void addListener(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addListenerImpl(phase, before, listener)
    }


    @Deprecated
    void addListenerImpl(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addListenerImpl(phase, before, listener);
    }


    @Deprecated
    void addListenerOrRunIfPassedImpl(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addListenerOrRunIfPassedImpl(phase, before, listener);
    }

    @Deprecated
    void addL(JrrRunnerPhaseI phase, boolean before, Callable listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    @Deprecated
    void addL(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addListenerOrRunIfPassedImpl(phase, before, listener)
    }

    static void addL(JrrRunnerPhaseI phase, boolean before, File listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    static void addL(JrrRunnerPhaseI phase, boolean before, String listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    static void addL(JrrRunnerPhaseI phase, boolean before, Class listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    static void addL(JrrRunnerPhaseI phase, boolean before, ClRef listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    void addListenerOrRunIfPassed(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, File listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, String listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, Class listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, ClRef listener) {
        gmrp.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


}
