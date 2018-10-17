package net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenCommonUtils
import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.FetchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.PullResult
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.api.Status
import org.eclipse.jgit.api.StatusCommand
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.transport.FetchResult

import java.util.logging.Logger

@CompileStatic
class CloneGitRepo {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public static MavenCommonUtils mcu = new MavenCommonUtils()

    static void updateGitRepo(File dir) {
        new GitRepoUtils2(dir).fetchAndCheckout()
    }



}
