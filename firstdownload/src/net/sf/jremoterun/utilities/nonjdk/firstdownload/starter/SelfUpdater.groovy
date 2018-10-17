package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.init.utils.CopyFileUtil
import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings.FirstDownloadSettings
import org.apache.commons.io.FileUtils

import java.util.logging.Logger

@CompileStatic
class SelfUpdater implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    @Override
    void run() {
        doJob2()
    }


    static void doJob2(){
        updateRepos()
        doJob()
    }

    static void doJob(){
        File old = GroovyMethodRunnerParams.gmrpn.grHome
//        File old = GroovyMethodRunnerParams.instance.grHome
        assert old !=null
        assert old.exists()
        String checkSuffix = 'libs/origin/jremoterun.jar'
        File newer = FirstDownloadSettings.starterDir
        File oldSample = new File(old, checkSuffix)
        File newSample =  new File(newer, checkSuffix)
        assert oldSample.exists()
        assert newSample.exists()
        FileUtils.copyDirectory(newer, old)
        CopyFileUtil.copyLibs(GroovyMethodRunnerParams.gmrpn.grHome)
//        CopyFileUtil.copyLibs(GroovyMethodRunnerParams.instance.grHome)
        log.info "self update finished fine"

        assert oldSample.length() == newSample.length()

    }



    static void updateRepos(){
        if(!FirstDownloadSettings.updateRepoOnStart){
            CloneGitRepo.updateGitRepo(FirstDownloadSettings.starterDir)
            CloneGitRepo.updateGitRepo(FirstDownloadSettings.firstdownDir)
        }
    }
}
