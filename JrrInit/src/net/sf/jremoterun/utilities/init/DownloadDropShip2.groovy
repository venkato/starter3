package net.sf.jremoterun.utilities.init

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.DownloadDirNotSetException
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.mdep.DropshipClasspath

import java.util.logging.Logger

@CompileStatic
class DownloadDropShip2 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public static boolean downloadIvyToTempDirIfNotPresent = true;
    public static boolean exitAfterdownloadIvyToTempDir = true;
    public static boolean downloadDirSetHere = false;

    static GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrp

    static void addJavassist() {
        DropshipClasspath.allLibsWithoutGroovyPlusXmlApi.each {
            try {
                gmrp.addFilesToClassLoader.addU(it)
            } catch (DownloadDirNotSetException e) {
                if (downloadIvyToTempDirIfNotPresent) {
                    downloadDirSetHere = true
                    MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = createTmpDir(e)
                    log.info "jrr download dir was set : ${MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir}"
                    gmrp.addFilesToClassLoader.addU(it)
                } else {
                    throw e;
                }
            } catch (Throwable e) {
                log.info "failed download ${it} ${e}"
                throw e
            }
        }
    }


    static File createTmpDir(DownloadDirNotSetException e) {
        String tmpDirS = System.getProperty("java.io.tmpdir")
        if (tmpDirS == null) {
            throw new Exception(e)
        }
        File tmpDir = tmpDirS as File
        if (!tmpDir.exists()) {
            throw new FileNotFoundException(tmpDirS)
        }
        if (!tmpDir.isDirectory()) {
            throw new FileNotFoundException("not as dir : ${tmpDirS}")
        }

        if (!tmpDir.canWrite()) {
            throw new FileNotFoundException("dir not writable: ${tmpDirS}")
        }
        File jrrDownloadDir = new File(tmpDir, "jrrdownloaddir")
        if (jrrDownloadDir.exists()) {
        } else {
            assert jrrDownloadDir.mkdir()
        }
        if (!jrrDownloadDir.canWrite()) {
            throw new FileNotFoundException("dir not writable: ${jrrDownloadDir}")
        }
        return jrrDownloadDir

//        File jrrDownloadInTmpDir =

    }

}
