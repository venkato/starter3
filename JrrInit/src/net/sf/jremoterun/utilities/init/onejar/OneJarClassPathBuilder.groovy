package net.sf.jremoterun.utilities.init.onejar

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.init.commonrunner.CommonRunner
import net.sf.jremoterun.utilities.init.utils.AddFileToUrlClassloaderUtil
import net.sf.jremoterun.utilities.init.utils.CopyFileUtil

@CompileStatic
class OneJarClassPathBuilder {

    void addDirs() {
        CopyFileUtil.addAllJarsInDir2(CommonRunner.cll, new File(CommonRunner.GR_HOME, "libs/copy/"))
        File jrrutilisiesFile = new File(CommonRunner.GR_HOME, "onejar/jrrutilities.jar")
        if (!jrrutilisiesFile.exists()) {
            throw new IllegalStateException("first create jrrutilities.jar using net.sf.jremoterun.utilities.nonjdk.compile.JrrUtilsCompiler")
        }
        AddFileToUrlClassloaderUtil.addFileToUrlClassLoader(CommonRunner.cll, jrrutilisiesFile)
    }


}
