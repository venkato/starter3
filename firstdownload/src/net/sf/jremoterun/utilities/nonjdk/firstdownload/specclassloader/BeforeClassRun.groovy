package net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.st.GroovyRunnerConfigurator

@Deprecated
@CompileStatic
class BeforeClassRun extends GroovyRunnerConfigurator {


    static String selfUpdateS = "selfUpdate"

    @Override
    void doConfig() {
        RunnableFactory.runRunner FirstDownloadSettings.A.GitRepoDownloader
        String msg = "${selfUpdateS} : ${selfUpdateS}"
        if (getFirstParam2(msg) == selfUpdateS) {
            gmrp.args[0] = FirstDownloadSettings.A.selfUpdateRef.clRef.className
        } else {
            RunnableFactory.runRunner FirstDownloadSettings.A.ifFrameWoekAdder
        }

    }


}
