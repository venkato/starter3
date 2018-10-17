package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import org.eclipse.jgit.api.*
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.SubmoduleConfig
import org.eclipse.jgit.transport.FetchResult

import java.text.SimpleDateFormat
import java.util.logging.Logger

/**
 * Sync code with :
 * @see net.sf.jremoterun.utilities.nonjdk.git.GitRepoUtils
 */
@CompileStatic
class GitRepoUtils2 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public File gitBaseDir;
    public Git git;
    public Repository gitRepository

    GitRepoUtils2(File gitBaseDir) {
        this.gitBaseDir = gitBaseDir
        assert gitBaseDir.exists()
        assert gitBaseDir.isDirectory()
        git = Git.open(gitBaseDir)
        gitRepository = git.getRepository()
    }

    void doCustomGitTuning(GitCommand gitCommand) {
    }

    void addAllFiles() {
        AddCommand addCommand = git.add()
        addCommand.addFilepattern('.')
        doCustomGitTuning(addCommand)
        addCommand.call()
        log.info("finished git add")
    }


    void gitCommit() {
        CommitCommand commitCommand = git.commit()
        commitCommand.setAll(true)
        SimpleDateFormat sdf =new SimpleDateFormat('yyyy-MM-dd--HH-mm')
        String msg = "automsg-${sdf.format(new Date())}"
        commitCommand.setMessage(msg)
        doCustomGitTuning(commitCommand)
        commitCommand.call()
    }


    public SubmoduleConfig.FetchRecurseSubmodulesMode fetchRecurseSubmodulesMode

    void fetch(String remoteName) {
        FetchCommand fetch1 = git.fetch()
        if (remoteName != null) {
            fetch1.setRemote(remoteName)
        }
        if(fetchRecurseSubmodulesMode!=null) {
            fetch1.setRecurseSubmodules(fetchRecurseSubmodulesMode)
        }
        doCustomGitTuning(fetch1)
        FetchResult fetchResult = fetch1.call()
        log.info("fetch done for ${gitBaseDir}")
    }


    void fetchAndCheckout() {
        fetch(null)
        checkoutAndCommitDirty2('master', Constants.DEFAULT_REMOTE_NAME)
    }



    void checkoutAndCommitDirty2(String branch, String remoteName) {
        StatusCommand statusCommand = git.status()
        doCustomGitTuning(statusCommand)
        Status statusResult = statusCommand.call()
        log.info "${gitBaseDir} is clean : ${statusResult.isClean()}"
        if (!statusResult.isClean()) {
            log.info "conflict : ${statusResult.conflicting}"
            log.info "modified : ${statusResult.modified}"
            log.info "untacked : ${statusResult.untrackedFolders}"
            log.info "missing : ${statusResult.missing}"
            addAllFiles()
            gitCommit()
        }


        CreateBranchCommand branchCreate = git.branchCreate()
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        String branchName =  "${branch}-${sdf.format(new Date())}"
        branchCreate.setName(branchName)
        branchCreate.startPoint = "remotes/${remoteName}/${branch}"
        doCustomGitTuning(branchCreate)
        branchCreate.call()

        CheckoutCommand checkoutCommand = git.checkout()
        checkoutCommand.setName(branchName)
        doCustomGitTuning(checkoutCommand)
        checkoutCommand.call()
        log.info "${gitBaseDir} switched to ${branchName}"
    }

    void close() throws Exception {
        git.close()
    }
}
