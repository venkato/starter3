package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.SharedObjectsUtils
import net.sf.jremoterun.utilities.FileOutputStream2
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.seqpattern.JrrRunnerPhaseI
import net.sf.jremoterun.utilities.groovystarter.seqpattern.SeqPatternRunnerGmrp

import java.util.concurrent.Callable

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

    public volatile File userHome = new File(System.getProperty("user.home"));


    public volatile File groovyUserConfigRaw = new File(userHome, "${JrrStarterConstatnts.jrrConfigDir}/${JrrStarterConstatnts.rawConfigFileName}");

    private volatile File userDir = new File(System.getProperty("user.dir"));

    public volatile File groovyFolderConfigRaw = new File(userDir, JrrStarterConstatnts.rawConfigFileName);

    public volatile File groovyHostConfigLinuxRaw = new File("${JrrStarterConstatnts.jrrConfigDirLinuxAllUsers}/${JrrStarterConstatnts.rawConfigFileName}");

    public volatile File groovyHostConfigWindowsRaw = new File("${JrrStarterConstatnts.jrrConfigDirWindowsAllUsers}/${JrrStarterConstatnts.rawConfigFileName}");

    public volatile boolean loadUserRawWindowsConfigShared = true;
    public volatile boolean loadGrHomeRawConfig = true;

    public volatile SeqPatternRunnerGmrp seqPatternRunnerGmrp = new SeqPatternRunnerGmrp();

    public volatile List<String> args;

    public
    volatile boolean showExceptionInSwingWindowV = false;

    public volatile boolean addClasspathToSystemClassLoader = false;

    public volatile GroovyMethodRunner groovyMethodRunner

    public
    volatile NewValueListener<Throwable> onExceptionOccured = new PrintExceptionListener();

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
            SystemExit.exit(1);
        }
    };

    public static volatile GroovyMethodRunnerParams gmrp;

    public static GroovyMethodRunnerParams getGmrpn(){
        if(gmrp==null){
            throw new NullPointerException('gmrp is null')
        }
        return gmrp
    }

    static GroovyMethodRunnerParams createInstance() {
        assert gmrp==null
        return getInstance()
    }

    static GroovyMethodRunnerParams getInstance() {
        if (gmrp != null) {
            return gmrp;
        }
        Map classloaders = SharedObjectsUtils.getGlobalMap();
        gmrp = (GroovyMethodRunnerParams) classloaders
                .get(GroovyMethodRunnerParams.getName());
        if (gmrp != null) {
            if (enableManyClassLoaders) {
                println "GroovyMethodRunnerParams created in different classloader"
            } else {
                throw new Exception("GroovyMethodRunnerParams created in different classloader ${gmrpn.getClass().getClassLoader()} ${GroovyMethodRunnerParams.getClassLoader()}");
            }
        }
        if (gmrp == null) {
            //Thread.dumpStack()
            gmrp = new GroovyMethodRunnerParams();
        }
        return gmrp;
    }


    File detectHomeFromWindows(){
        return JrrStarterVariables2.getInstance().detectHomeFromWindows();
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
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    @Deprecated
    void addL(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addListenerOrRunIfPassedImpl(phase, before, listener)
    }

    static void addL(JrrRunnerPhaseI phase, boolean before, File listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    static void addL(JrrRunnerPhaseI phase, boolean before, String listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    static void addL(JrrRunnerPhaseI phase, boolean before, Class listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    static void addL(JrrRunnerPhaseI phase, boolean before, ClRef listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    void addListenerOrRunIfPassed(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, File listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, String listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }

    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, Class listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }


    @Deprecated
    static void addListenerOrRunIfPassed2(JrrRunnerPhaseI phase, boolean before, ClRef listener) {
        gmrpn.seqPatternRunnerGmrp.addL(phase, before, listener)
    }




}
