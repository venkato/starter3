package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class DownloadDirNotSetException extends Exception{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


}
