import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.*
import net.sf.jremoterun.utilities.groovystarter.*
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Logger

// ClassName used in ConsoleRedirectFD
@CompileStatic
class FirstDownloadConfig implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public static ClRef initClass = new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.FdInit')

    public static Runnable customRunner = RunnableFactory.createRunner(initClass)

    GmrpRunnerHelper h= GmrpRunnerHelper.get()

    @Override
    void run() {
        runCustomConfig();
        h.gmrpn.addListener(JrrRunnerPhase.createClassLoaderAdder,false, this.&bcs );
    }

    void runCustomConfig(){
        File parentFile = h.gmrpn.grHome.parentFile
        assert parentFile.exists()
        File dfFile = new File(parentFile,"FirstDownloadCustomConfig.groovy")
        dfFile = dfFile.canonicalFile.absoluteFile
        log.info "checking custom config : ${dfFile} , exist = ${dfFile.exists()}"
        if(dfFile.exists()) {
            RunnableFactory.runRunner(dfFile)
        }
    }

    void bcs(){
        if(MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir==null) {
            MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = ("jrrdownload" as File).absoluteFile
        }
        MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir.mkdirs();

        AddFilesToUrlClassLoaderGroovy b = h.gmrpn.addFilesToClassLoader;
        File firstDownloadRepoPureGit = new File(MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir,'git/https/github.com/venkato/starter3/git')
        if(firstDownloadRepoPureGit.exists()){
        }else{
            log.info "not found : ${firstDownloadRepoPureGit}"
            firstDownloadRepoPureGit = h.gmrpn.grHome
//            firstDownloadRepoPureGit = new File("..").absoluteFile.canonicalFile
        }
        b.addF new File(firstDownloadRepoPureGit,"firstdownload/src")
        b.addF new File(firstDownloadRepoPureGit,"firstdownload/log4j2-config")
        customRunner.run();
//        gmrp.addL(JrrRunnerPhase.createClassLoaderAdder,false,initClass)
    }

}

