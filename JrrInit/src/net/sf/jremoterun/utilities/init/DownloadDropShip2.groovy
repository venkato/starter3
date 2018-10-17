package net.sf.jremoterun.utilities.init

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.DownloadDirNotSetException
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenIdContains
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.mdep.DropshipClasspath

import java.util.logging.Logger

@CompileStatic
class DownloadDropShip2 implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public static boolean downloadIvyToTempDirIfNotPresent = true;
    public static boolean exitAfterdownloadIvyToTempDir = true;
    public static boolean downloadDirSetHere = false;

    //static GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrp


    @Override
    void run() {
        addJavassist2()
    }

    static void addJavassist() {
        new DownloadDropShip2().addJavassist2()
    }

    void addJavassist2() {
        checkIvyRepoFilePermissions(DropshipClasspath.allLibsWithoutGroovyPlusXmlApi)
        DropshipClasspath.allLibsWithoutGroovyPlusXmlApi.each {
            MavenId mavenId = it.getM()
//            assert mavenId.version!= MavenCommonUtils.lastVersionInd
//            gmrp.addFilesToClassLoader.findMavenOrGradleOrUrl(mavenId)
            try {
                GroovyMethodRunnerParams.gmrpn.addFilesToClassLoader.addU(mavenId)
            } catch (DownloadDirNotSetException e) {
                if (downloadIvyToTempDirIfNotPresent) {
                    downloadDirSetHere = true
                    MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = createTmpDir(e)
                    log.info "jrr download dir was set : ${MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir}"
                    GroovyMethodRunnerParams.gmrpn.addFilesToClassLoader.addU(mavenId)
                } else {
                    throw e;
                }
            } catch (Throwable e) {
                log.info "failed download ${mavenId} ${e}"
                throw e
            }
        }
    }

    void checkIvyRepoFilePermissions(List<? extends MavenIdContains> listMavenIds) {
        File ivyDir = MavenDefaultSettings.mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2()
        if (ivyDir != null) {
            checkIvyRepoFilePermissions2(listMavenIds, ivyDir)
        }
    }

    void checkIvyRepoFilePermissions3(MavenId mavenId , File ivyDir) {
        File groupDir = new File(ivyDir, mavenId.groupId)
        if (groupDir.exists()) {
            assert groupDir.canRead()
            assert groupDir.isDirectory()
            File artifactDir = new File(groupDir, mavenId.artifactId)
            if (artifactDir.exists()) {
                assert artifactDir.canRead()
                assert artifactDir.isDirectory()
                File[] files12 = artifactDir.listFiles()
                if(files12==null){
                    throw new Exception("failed list children for dir ${artifactDir}")
                }
                files12.toList().each {
                    if(!it.exists()){
                        throw new FileNotFoundException(it.toString())
                    }
                    if(!it.canRead()){
                        throw new IOException("can't read ${it}");
                    }
//                        log.info "checked : ${it}"
                }
            }
        }

    }
    void checkIvyRepoFilePermissions2(List<? extends MavenIdContains> listMavenIds, File ivyDir) {
        assert ivyDir.exists()
        assert ivyDir.canRead()
        assert ivyDir.isDirectory()
        listMavenIds.each {
            MavenId mavenId = it.getM()
            try {
                checkIvyRepoFilePermissions3(mavenId,ivyDir)
            }catch(Throwable e){
                log.info "failed check lib present on ${mavenId} : ${e}"
                throw e;
            }
        }
    }


    File createTmpDir(DownloadDirNotSetException e) {
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
