import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.*
import net.sf.jremoterun.utilities.groovystarter.*

// ClassName used in ConsoleRedirectFD
@CompileStatic
class FirstDownloadConfig extends GroovyRunnerConfigurator2 {


    public static ClRef initClass = new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.FdInit')

    public static Runnable customRunner = {createRunnerFromClass(initClass).run() }

    @Override
    void doConfig() {
        runCustomConfig();
        gmrp.addListener(JrrRunnerPhase.createClassLoaderAdder,false, this.&bcs );
    }

    void runCustomConfig(){
        File parentFile = gmrp.grHome.parentFile
        assert parentFile.exists()
        File dfFile = new File(parentFile,"FirstDownloadCustomConfig.groovy")
        dfFile = dfFile.canonicalFile.absoluteFile
        log.info "checking custom config : ${dfFile} , exist = ${dfFile.exists()}"
        if(dfFile.exists()) {
            createRunnerFromFile(dfFile).run()
        }
    }

    void bcs(){
        if(MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir==null) {
            MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir = ("jrrdownload" as File).absoluteFile
        }
        MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir.mkdirs();

        AddFilesToUrlClassLoaderGroovy b = gmrp.addFilesToClassLoader;
        File firstDownloadRepoPureGit = new File(MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir,'git/https/github.com/venkato/starter3/git')
        if(firstDownloadRepoPureGit.exists()){
        }else{
            log.info "not found : ${firstDownloadRepoPureGit}"
            firstDownloadRepoPureGit = gmrp.grHome
//            firstDownloadRepoPureGit = new File("..").absoluteFile.canonicalFile
        }
        b.addF new File(firstDownloadRepoPureGit,"firstdownload/src")
        b.addF new File(firstDownloadRepoPureGit,"firstdownload/log4j2-config")
        customRunner.run();
//        gmrp.addL(JrrRunnerPhase.createClassLoaderAdder,false,initClass)
    }

}

