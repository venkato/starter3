package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import java.util.logging.Logger

@CompileStatic
public abstract class AddFilesToClassLoaderGroovy extends AddFilesToClassLoaderCommon {

    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

    JrrGroovyScriptRunner groovyScriptRunner = GroovyMethodRunner.groovyScriptRunner;


    List<File> addedGroovyClassPathFiles = []

    void addFromGroovyFile(File file) {
        JrrUtilities3.checkFileExist(file)
//        Thread.dumpStack()
        if (addedGroovyClassPathFiles.contains(file)) {
            log.info "${file} already added"
        } else {
            file = file.absoluteFile.canonicalFile
            assert groovyScriptRunner != null
            groovyScriptRunner.addFilesToClassLoaderF(file, this);
            log.fine "added files count : ${addedFiles2.size()}"
            addedGroovyClassPathFiles.add(file)
        }
    }


    void addFilesFromGmrp() {
        GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrp
        if (gmrp == null) {
            throw new IllegalStateException("gmrp not inited")
        }
        addAll(gmrp.addFilesToClassLoader.addedFiles2)
    }


}
