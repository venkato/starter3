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
        updateGitRepo(dir, "master")
    }



    static void fetch(File dir, String branch) {
        Git git = Git.open(dir)
        FetchCommand fetch = git.fetch()
//        PullCommand pullCommand = git.pull()
//            pullCommand.remoteBranchName = "origin/master"
//        PullResult pullResult = pullCommand.call()
//        log.info "${pullResult}"
        FetchResult fetchResult = fetch.call()
        log.info("${fetchResult}")
    }

    static void updateGitRepo(File dir, String branch) {
        Git git = Git.open(dir)
        StatusCommand statusCommand = git.status()
        Status statusResult = statusCommand.call()
        log.info "${statusResult}"
        log.info "Has uncommited : ${statusResult.hasUncommittedChanges()}"
        log.info "Is clean : ${statusResult.isClean()}"
        if (statusResult.hasUncommittedChanges()) {
            log.info "conflict : ${statusResult.conflicting}"
            log.info "modified : ${statusResult.modified}"
            log.info "untacked : ${statusResult.untrackedFolders}"
            log.info "missing : ${statusResult.missing}"
        }
        FetchCommand fetch = git.fetch()
        FetchResult fetchResult = fetch.call()
        log.info("${fetchResult}")
        ResetCommand resetCommand = git.reset()

        resetCommand.mode = ResetCommand.ResetType.HARD
        Ref resetResult = resetCommand.call()
        log.info "${resetResult}"
        if (false) {
            PullCommand pullCommand = git.pull()
//            pullCommand.remoteBranchName = "origin/${branch}"
            PullResult pullResult = pullCommand.call()
            log.info "${pullResult}"
        }

        String branchName = "${branch}-${new Date().format("yyyy-MM-dd-HH-mm-ss")}"
//        String branchName ="master"

        if (true) {
            CheckoutCommand checkoutCommand = git.checkout()
            checkoutCommand.name = branchName
//            checkoutCommand.name = "master${new Date().format("yyyy-MM-dd-HH-mm-ss")}"
            checkoutCommand.createBranch = true
            checkoutCommand.startPoint = "origin/${branch}"
            checkoutCommand.force = true
//        checkoutCommand.stage = CheckoutCommand.Stage.THEIRS

//            checkoutCommand.allPaths = true
//            checkoutCommand.upstreamMode = CreateBranchCommand.SetupUpstreamMode.TRACK;
            Ref cheoutRes = checkoutCommand.call()
            log.info "${cheoutRes}"
            log.info "${checkoutCommand.result}"
            log.info "${checkoutCommand.result.status}"
            log.info("update done : ${dir}")

        }
        git.close()
    }

    static void cleanDir(File toDir) {
        if (toDir.exists()) {
            toDir.deleteDir()
        }
        toDir.mkdirs()
        assert toDir.exists()
        assert toDir.listFiles().length == 0

    }

    static String createGitRepoSuffix(String src) {
        String dirSuffix = src;
        dirSuffix = dirSuffix.replace('@', '/');
        dirSuffix = dirSuffix.replace(':', '/')
        dirSuffix = dirSuffix.replace('//', '/')
        return dirSuffix
    }

    static File cloneGitRepo2(File gitDownloadDir, String src) {
        assert gitDownloadDir.exists()
        String dirSuffix = createGitRepoSuffix(src)
        File toDir = new File(gitDownloadDir, dirSuffix)
        cleanDir(toDir)
        cloneGitRepo(toDir, src)
        return toDir
    }

    static void cloneGitRepo(File toDir, String src) {
        log.info "downloading ${src}"
        assert toDir.parentFile.exists()
        if (toDir.exists()) {
            assert toDir.listFiles().length == 0
        }
        CloneCommand cloneCommand = Git.cloneRepository()
        cloneCommand.branch = 'master'
        cloneCommand.setURI(src)
        File gitDir = new File(toDir, ".git");
        cloneCommand.setGitDir(gitDir)
        cloneCommand.directory = toDir
        Git gitCloneResult = cloneCommand.call()
        log.info "${gitCloneResult}"
        assert gitDir.listFiles().length > 1
        assert toDir.listFiles().length > 2
        log.info("checkout fine : ${src}")
    }


}
