package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.groovystarter.ShortcutSelector
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2
import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings.FdClassPath
import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings.SomeClassRefs

import java.util.logging.Logger

@CompileStatic
class FdInit extends GroovyRunnerConfigurator2 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

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
        MavenDefaultSettings.mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2().mkdir();
        MavenDefaultSettings.mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2().mkdirs();
    }

    void addGitClassPath() {
        gmrp.addFilesToClassLoader.addGenericEnteries FdClassPath.mavenIds
        RunnableFactory.runRunner SomeClassRefs.GitRepoDownloader
        Map m = ['selfUpdate': this.&doSeftUpdate]
        boolean actionSelected = ShortcutSelector.runAction2(m)
        if (actionSelected) {
        } else {
            RunnableFactory.runRunner SomeClassRefs.ifFrameworkAdder
        }
    }

    boolean doSeftUpdate() {
        gmrp.args[0] = SomeClassRefs.selfUpdateRef.clRef.className
    }


}
