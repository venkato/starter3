package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git

import java.util.logging.Logger

@CompileStatic
class CloneGitRepo3 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    private static String gitDirSuffix = "git_download_"

    File gitBaseDir
    File gitTmpDir

    CloneGitRepo3(File gitBaseDir2) {
        this.gitBaseDir = gitBaseDir2
        assert gitBaseDir.exists()
        this.gitTmpDir = new File(gitBaseDir, "tmp")
        gitTmpDir.mkdir()
        assert gitTmpDir.exists()
    }

    File cloneGitRepo3(String src,String branch1) {
        String dirSuffix = createGitRepoSuffix(src)
        File toDir3 = new File(gitBaseDir, dirSuffix + '/git')
        cloneGitRepo4(src, toDir3,branch1);
        return toDir3;
    }

    void cloneGitRepo4(String src, File toDir3,String branch1) {
        if (toDir3.exists() && toDir3.listFiles().length > 0) {
            log.info("already downloaded ${src}")
        } else {
            File tmpGitDir = findGitDownloadDir();
            tmpGitDir.mkdir()
            cloneGitRepo(tmpGitDir, src,branch1)
            if (toDir3.exists()) {
                assert toDir3.deleteDir()
            }
            if (!tmpGitDir.renameTo(toDir3)) {
                log.info("can't rename ${tmpGitDir} to ${toDir3}, tring copy and delete")
                FileUtils.copyDirectory(tmpGitDir, toDir3)
                if (!FileUtils.deleteQuietly(tmpGitDir)) {
                    log.info("failed delete ${tmpGitDir}")
                }
            }
        }

    }

    File findGitDownloadDir() {
        int i = 10
        while (i < 100) {
            i++;
            File tmpGitDir = new File(gitTmpDir, gitDirSuffix + "${i}")
            if (!tmpGitDir.exists()) {
                return tmpGitDir;
            }
        }
        throw new Exception("can't find free dir in ${gitTmpDir}")
    }



    static String createGitRepoSuffix(String src) {
        if (src.endsWith('.git')) {
            src = src.substring(0, src.length() - 4 )
//            log.info "new git ref = ${src}"
        }
        String dirSuffix = src;
        dirSuffix = dirSuffix.replace('@', '/');
        dirSuffix = dirSuffix.replace(':', '/')
        dirSuffix = dirSuffix.replace('//', '/')
        return dirSuffix
    }

    public static boolean cloneSubModules = true


    static void cloneGitRepo(File toDir, String src,String branch1) {
        if(src.contains(' ')){
            throw new Exception("Repo contains spaces : ${src}")
        }
        log.info "downloading ${src}"
        assert toDir.parentFile.exists()
        if (toDir.exists()) {
            assert toDir.listFiles().length == 0
        }
        CloneCommand cloneCommand = Git.cloneRepository()
        cloneCommand.setCloneSubmodules(cloneSubModules)
        cloneCommand.branch = branch1
        cloneCommand.setURI(src)
        File gitDir = new File(toDir, ".git");
        cloneCommand.setGitDir(gitDir)
        cloneCommand.directory = toDir
        Git gitCloneResult = cloneCommand.call()
        log.info "${gitCloneResult}"
        assert gitDir.listFiles().length > 1
        assert toDir.listFiles().length > 2
        gitCloneResult.close()
        log.info("checkout fine : ${src}")
    }


}
