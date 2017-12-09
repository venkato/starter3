package net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef

@CompileStatic
class FirstDownloadSettings {

    public static File gitRepoDir = new File(MavenDefaultSettings.mavenDefaultSettings.jrrDownloadDir, "git")

    public static String fdGitRef = "https://github.com/venkato/firstdownload"
    public static String starterGitRef = "https://github.com/venkato/starter3"
    public static boolean updateRepoOnStart = false

    public static boolean continueRunAfterFirstDownloadedAdded = true

    public static File outLog = new File('logs/out.txt');

    enum A implements ClRefRef {
        GitRepoDownloader(new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader.GitRepoDownloader'))
        , selfUpdateRef(new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.SelfUpdater'))
        , ifFrameWoekAdder(new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader.sep1.IfFrameworkDownloader'))
        ;

        ClRef clRef;
        A(ClRef clRef) { this.clRef = clRef }
    }

    /**
     * point to starter/firstdownload\
     */
    public static File firstdownload;


    /**
     * point to https://github.com/venkato/firstdownload
     */
    public static File firstdownDir;
    public static File starterDir;

    private volatile static boolean inited = false


    static void init() {
        if (!inited) {
            inited = true
            gitRepoDir.mkdir()
            assert gitRepoDir.exists()

        }
    }

}
