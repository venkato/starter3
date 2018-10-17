package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef
import net.sf.jremoterun.utilities.javaonly.InitInfo

@CompileStatic
class FirstDownloadSettings {

    public static File gitRepoDir = new File(MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir, "git")

    public static String fdGitRef = "https://github.com/venkato/firstdownload"
    public static String starterGitRef = "https://github.com/venkato/starter3"
    public static boolean updateRepoOnStart = false

    public static boolean continueRunAfterFirstDownloadedAdded = true

    public static File outLog = new File('logs/out.txt');


    /**
     * point to starter/firstdownload\
     */
    public static File firstdownload;


    /**
     * point to https://github.com/venkato/firstdownload
     */
    public static File firstdownDir;
    public static File starterDir;

    //private volatile static boolean inited = false
    public static InitInfo initInfo = new InitInfo(FirstDownloadSettings)


    static void init() {
        if (!initInfo.setInited()) {
            initInfo.setInited()
            gitRepoDir.mkdir()
            assert gitRepoDir.exists()

        }
    }

}
