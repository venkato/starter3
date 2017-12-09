package net.sf.jremoterun.utilities.nonjdk.firstdownload

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.groovystarter.ShortcutSelector
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2
import net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader.FirstDownloadSettings

import java.util.logging.Logger

@CompileStatic
class FdInit extends GroovyRunnerConfigurator2 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

//    ClassNameReference classNameReference = new ClassNameReference('net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader.BeforeClassRun')

    @Override
    void doConfig() {
        doJob()
    }

    void doJob() {
        initDirs()
        gmrp.addL(JrrRunnerPhase2.setDependecyResolver, false, this.&addGitClassPath)

    }

    void initDirs() {
        gmrp.addFilesToClassLoader.isLogFileAlreadyAdded = false
        MavenDefaultSettings.mavenDefaultSettings.grapeLocalDir.mkdir();
        MavenDefaultSettings.mavenDefaultSettings.grapeLocalDir.mkdirs();
    }

    void addGitClassPath() {
        gmrp.addFilesToClassLoader.addGenericEnteries FdClassPath.mavenIds
        RunnableFactory.runRunner FirstDownloadSettings.A.GitRepoDownloader
        Map m = ['selfUpdate': this.&doSeftUpdate]
        boolean actionSelected = ShortcutSelector.runAction2(m)
        if (actionSelected) {
        } else {
            RunnableFactory.runRunner FirstDownloadSettings.A.ifFrameWoekAdder
        }
    }

    boolean doSeftUpdate() {
        gmrp.args[0] = FirstDownloadSettings.A.selfUpdateRef.clRef.className
    }


}
