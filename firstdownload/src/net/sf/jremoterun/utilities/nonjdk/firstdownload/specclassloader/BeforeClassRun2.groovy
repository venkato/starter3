package net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2

import net.sf.jremoterun.utilities.nonjdk.firstdownload.FdClassPath


@Deprecated
@CompileStatic
class BeforeClassRun2 extends GroovyRunnerConfigurator2 {



    @Override
    void doConfig() {
        doImpl()
    }

    void doImpl(){
        gmrp.addL(JrrRunnerPhase2.afterClassPathSet, false, this.&addGitClassPath)
        RunnableFactory.runRunner FirstDownloadSettings.A.GitRepoDownloader
    }


    void addGitClassPath(){
        adder.addAll FdClassPath.mavenIds
    }



}
