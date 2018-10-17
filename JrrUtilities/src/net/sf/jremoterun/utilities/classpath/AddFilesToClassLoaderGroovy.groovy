package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableWithParamsFactory2

import java.util.logging.Logger

@CompileStatic
public abstract class AddFilesToClassLoaderGroovy extends AddFilesToClassLoaderCommon {

    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

    public RunnableWithParamsFactory2 groovyScriptRunner = new RunnableWithParamsFactory2();


    List<File> addedGroovyClassPathFiles = []

    void addFromGroovyFile(File file) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
//        Thread.dumpStack()
        if (addedGroovyClassPathFiles.contains(file)) {
            log.info "${file} already added"
        } else {
            file = file.getAbsoluteFile().getCanonicalFile()
            //assert groovyScriptRunner != null
            //groovyScriptRunner.addFilesToClassLoaderF(file, this);
            groovyScriptRunner.loadSettingsWithParam(file,this)
//            Object script = groovyScriptRunner.groovyScriptRunner.parseConfig(file)
//            LoadScriptFromFileUtils.runWithParams(script,this,null)
//            groovyScriptRunner.runScript(script,this)
            log.fine "added files count : ${addedFiles2.size()}"
            addedGroovyClassPathFiles.add(file)
        }
    }


    @Deprecated
    void addFromGroovyTextFile(String groovyFileContent, String scriptName) {
        groovyScriptRunner.loadSettingsWithParam(groovyFileContent,this)
    }


    void addFilesFromGmrp() {
        addAll(GroovyMethodRunnerParams.gmrpn.addFilesToClassLoader.addedFiles2)
    }


    void addFromGroovyFile(ToFileRef2 file) {
        File f3 = file.resolveToFile()
        assert f3 != null
        addFromGroovyFile(f3)
    }

}
