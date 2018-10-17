package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenCommonUtils
import net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.GitRepoUtils2

import java.util.logging.Logger

@CompileStatic
class CloneGitRepo {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public static MavenCommonUtils mcu = new MavenCommonUtils()

    static void updateGitRepo(File dir) {
        new GitRepoUtils2(dir).fetchAndCheckout()
    }



}
