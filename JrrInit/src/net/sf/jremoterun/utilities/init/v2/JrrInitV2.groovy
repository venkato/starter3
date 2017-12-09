package net.sf.jremoterun.utilities.init.v2

import groovy.transform.CompileStatic
import net.sf.jremoterun.SimpleFindParentClassLoader
import net.sf.jremoterun.utilities.CreationInfo
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.JrrRunnerPhase
import net.sf.jremoterun.utilities.groovystarter.ShowExceptionInSwingHandler
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.st.JdkLogFormatter
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2
import net.sf.jremoterun.utilities.groovystarter.st.JrrStarterChecks
import net.sf.jremoterun.utilities.init.DownloadDropShip2
import net.sf.jremoterun.utilities.init.SetGroovyDefaultClassLoader
import net.sf.jremoterun.utilities.init.commonrunner.CommonRunner
import net.sf.jremoterun.utilities.init.commonrunner.CommonRunner2
import net.sf.jremoterun.utilities.init.commonrunner.RunnerInterface
import net.sf.jremoterun.utilities.init.utils.AlreadyInited

import javax.swing.SwingUtilities
import java.util.logging.Logger

/**
 * Called from
 * @SeeAlso net.sf.jremoterun.utilities.init.commonrunner.CommonRunner2
 */
@CompileStatic
class JrrInitV2 implements RunnerInterface {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.instance

    public static boolean setSystemClassLoader = true;

    public static String showExceptionInSwingWindow = "exception.swing";
    public
    static boolean showExceptionInSwingWindowValue = "true".equalsIgnoreCase(System.getProperty(showExceptionInSwingWindow));

    static ClRef setDepResolver = new ClRef('net.sf.jremoterun.utilities.init.JrrInit3')

    static ClRef fieldAccessorSetter = new ClRef('net.sf.jremoterun.utilities.JrrFieldAccessorSetter')

    static ClRef groovyCastImproved = new ClRef('net.sf.jremoterun.utilities.groovystrans.JrrCastASTTransformation')

    public static boolean setClassLoaderForSwingThread = true

    private static CreationInfo inited

    @Override
    void jrrRunScript() {
        preRun()
        List<String> argsCopy = new ArrayList<>(CommonRunner.args)
        new GroovyMethodRunner().jrrRunScript(CommonRunner.initialScript2, argsCopy)
    }

    void setIfSwing() {
        if (showExceptionInSwingWindowValue || CommonRunner2.isUsedJavaw) {
            gmrp.showExceptionInSwingWindowV = showExceptionInSwingWindowValue
            gmrp.onExceptionOccured = new ShowExceptionInSwingHandler();
        }
    }

    void preRun() {
        gmrp.creationDate = CommonRunner.creationDate
        gmrp.grHome = CommonRunner.GR_HOME
        gmrp.addClasspathToSystemClassLoader = CommonRunner.useSystemClassLoader;
        setIfSwing();
        if (inited == null) {
            inited = new CreationInfo()
        } else {
            AlreadyInited.alreadyInited(inited, this)
            return
        }
        RunnableFactory.runRunner fieldAccessorSetter
        RunnableFactory.runRunner groovyCastImproved
        // don't change to 'addL'
        gmrp.getListeners(JrrRunnerPhase.argsSet, false).add(this.&preRun2)
    }

    void preRun2() {
        JdkLogFormatter.setLogFormatter()
//        gmrp.preparedClassName = GroovyMethodRunner2.name
        gmrp.addL(JrrRunnerPhase.addJrrStarterLib, false, DownloadDropShip2.&addJavassist)
        gmrp.addL(JrrRunnerPhase.checks, false, new JrrStarterChecks())
        gmrp.addL(JrrRunnerPhase.createGroovyClassLoader, false, this.&setSystemClassLoaderM)
        gmrp.addL(JrrRunnerPhase.addJrrStarterLib, false, setDepResolver)

        gmrp.jrrUtilsPhaseDoneAfter = JrrRunnerPhase2.afterCoreLibAdded
    }


    void setSystemClassLoaderM() {
        if (setSystemClassLoader) {
            setSystemClassLoaderImpl()
        }
    }

    void setSystemClassLoaderImpl() {
        GroovyClassLoader groovyClassLoader = gmrp.groovyClassLoader
        new SetGroovyDefaultClassLoader().run()
        assert groovyClassLoader != null
        JrrClassUtils.setFieldValue(ClassLoader, 'scl', groovyClassLoader)
        assert ClassLoader.getSystemClassLoader() == groovyClassLoader
        SimpleFindParentClassLoader.setDefaultClassLoader(groovyClassLoader)
        Thread thread2 = Thread.currentThread()
        thread2.setContextClassLoader(groovyClassLoader)
        if (setClassLoaderForSwingThread) {
            SwingUtilities.invokeLater {
                Thread thread = Thread.currentThread()
                thread.setContextClassLoader(groovyClassLoader)
            }
        }
    }


}
