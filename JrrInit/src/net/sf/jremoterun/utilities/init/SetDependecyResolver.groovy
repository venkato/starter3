package net.sf.jremoterun.utilities.init

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import net.sf.jremoterun.utilities.groovystarter.SystemExit
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2
import net.sf.jremoterun.utilities.mdep.DropshipClasspath
import net.sf.jremoterun.utilities.mdep.ivy.IvyDepResolver2

import java.util.logging.Logger

@CompileStatic
class SetDependecyResolver implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ClRef cnr = new ClRef('net.sf.jremoterun.utilities.init.SetGrapeWrapper')

    @Override
    void run() {
        f2()
    }


    void f2() {
        boolean b =IvyDepResolver2.setDepResolver()
        //b = true
        if (b) {
            f3()
        } else {
            DropshipClasspath.downloadyIvydepToIvyDir()
            checkIfNeedExit();
        }

    }

    void f3() {
//        log.info("setting dep resolver ..")
        DropshipClasspath.downloadyIvydepToIvyDir()
        checkIfNeedExit();
        GroovyMethodRunnerParams.gmrpn.addFilesToClassLoaderGroovy.addM(DropshipClasspath.groovy)
        GroovyMethodRunnerParams.gmrpn.addL(JrrRunnerPhase2.addClassPathFiles, false, cnr)
    }

    void checkIfNeedExit() {
        if (DownloadDropShip2.downloadDirSetHere && DownloadDropShip2.exitAfterdownloadIvyToTempDir) {
            log.info "jrrDownloadDir was set to download basic maven ids. rerun job"
            SystemExit.exit(1)
        }
    }
}
