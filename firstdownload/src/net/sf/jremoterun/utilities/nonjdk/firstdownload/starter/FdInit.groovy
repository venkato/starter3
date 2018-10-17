package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.GmrpRunnerHelper
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import net.sf.jremoterun.utilities.groovystarter.ShortcutSelector
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2
import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings.FdClassPath
import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings.SomeClassRefs

import java.util.logging.Logger

@CompileStatic
class FdInit implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    GmrpRunnerHelper h= GmrpRunnerHelper.get()

    @Override
    void run() {
        doJob()
    }

    void doJob() {
        initDirs()
        GroovyMethodRunnerParams.gmrpn.addL(JrrRunnerPhase2.setDependecyResolver, false, this.&addGitClassPath)

    }

    void initDirs() {
        h.gmrpn.addFilesToClassLoader.isLogFileAlreadyAdded = false
        MavenDefaultSettings.mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2().mkdir();
        MavenDefaultSettings.mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2().mkdirs();
    }

    void addGitClassPath() {
        h. gmrpn.addFilesToClassLoader.addGenericEnteries FdClassPath.mavenIds
        RunnableFactory.runRunner SomeClassRefs.GitRepoDownloader
        Map m = ['selfUpdate': this.&doSeftUpdate]
        boolean actionSelected = ShortcutSelector.runAction2(m)
        if (actionSelected) {
        } else {
            RunnableFactory.runRunner SomeClassRefs.ifFrameworkAdder
        }
    }

    boolean doSeftUpdate() {
        GroovyMethodRunnerParams.gmrpn.args[0] = SomeClassRefs.selfUpdateRef.clRef.className
    }


}
