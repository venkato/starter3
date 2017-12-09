package net.sf.jremoterun.utilities.init.v2

import net.sf.jremoterun.utilities.init.commonrunner.CommonRunner
import net.sf.jremoterun.utilities.init.utils.AddFileToUrlClassloaderUtil;

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.init.utils.CopyFileUtil;


@CompileStatic
class InitClassPathBuilder {

    void addDirs(){
        CopyFileUtil.addAllJarsInDir2 (CommonRunner.cll,new File(CommonRunner.GR_HOME, "libs/copy/"))
        AddFileToUrlClassloaderUtil.addFileToUrlClassLoader (CommonRunner.cll,new File(CommonRunner.GR_HOME, "JrrInit/src"))
        AddFileToUrlClassloaderUtil.addFileToUrlClassLoader (CommonRunner.cll,new File(CommonRunner.GR_HOME, "JrrStarter/src"))
        AddFileToUrlClassloaderUtil.addFileToUrlClassLoader (CommonRunner.cll,new File(CommonRunner.GR_HOME, "JrrUtilities/src"))
    }




}
