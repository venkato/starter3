package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings.FirstDownloadSettings

import java.util.logging.Logger


@CompileStatic
class GitRepoDownloader implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    CloneGitRepo3 cloneGitRepo3;



    public static File idwDocking;

    @Override
    void run() {
        FirstDownloadSettings.init()
        testGitRepo()
        addToClassPath()
    }

    void testDownload() {
        FirstDownloadSettings.gitRepoDir.mkdir()
        if(FirstDownloadSettings.starterDir==null) {
            FirstDownloadSettings.starterDir = cloneGitRepo(FirstDownloadSettings.starterGitRef,'master')
        }

        if(FirstDownloadSettings.firstdownload==null) {
            FirstDownloadSettings.firstdownload = new File(FirstDownloadSettings.starterDir, 'firstdownload')
        }
        if(FirstDownloadSettings.firstdownDir==null        ) {
            FirstDownloadSettings.firstdownDir= cloneGitRepo(FirstDownloadSettings.fdGitRef,'master')
        }
    }



    void testGitRepo() {
        testDownload();
//        downloadAndAddIdw()
        if(FirstDownloadSettings.updateRepoOnStart) {
            CloneGitRepo.updateGitRepo(FirstDownloadSettings.starterDir)
            CloneGitRepo.updateGitRepo(FirstDownloadSettings.firstdownDir)
//        CloneGitRepo.updateGitRepo(sshConsole)
        }
    }



    void addToClassPath() {
        AddFilesToUrlClassLoaderGroovy adder = GroovyMethodRunnerParams.gmrpn.addFilesToClassLoader
        adder.addF(new File(FirstDownloadSettings.firstdownDir, "src"))
    }

    File cloneGitRepo(String repo,String branch1) {
        if (cloneGitRepo3 == null) {
            cloneGitRepo3 = new CloneGitRepo3(FirstDownloadSettings.gitRepoDir);
        }
        return cloneGitRepo3.cloneGitRepo3(repo,branch1)
    }


}
