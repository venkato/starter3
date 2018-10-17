package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator2
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import java.util.logging.Logger

@CompileStatic
public abstract class AddFilesToClassLoaderGroovy extends AddFilesToClassLoaderCommon {

    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

    JrrGroovyScriptRunner groovyScriptRunner = GroovyMethodRunner.groovyScriptRunner;


    List<File> addedGroovyClassPathFiles = []

    void addFromGroovyFile(File file) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
//        Thread.dumpStack()
        if (addedGroovyClassPathFiles.contains(file)) {
            log.info "${file} already added"
        } else {
            file = file.getAbsoluteFile().getCanonicalFile()
            assert groovyScriptRunner != null
            groovyScriptRunner.addFilesToClassLoaderF(file, this);
            log.fine "added files count : ${addedFiles2.size()}"
            addedGroovyClassPathFiles.add(file)
        }
    }


    void addFromGroovyTextFile(String groovyFileContent, String scriptName) {
        assert groovyScriptRunner != null
//        Script script1 = groovyScriptRunner.createScriptClass(groovyFileContent, scriptName, this)
        Class scriptClass = groovyScriptRunner.createScriptClass(groovyFileContent, scriptName)
        Object script = scriptClass.newInstance();
        groovyScriptRunner.runScript(script,this)
        //Script script = createScript(scriptSource, scriptName, addCl);
        //script1.run()
    }


    void addFilesFromGmrp() {
        GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrp
        if (gmrp == null) {
            throw new IllegalStateException("gmrp not inited")
        }
        addAll(gmrp.addFilesToClassLoader.addedFiles2)
    }


    void addFromGroovyFile(ToFileRef2 file) {
        File f3 = file.resolveToFile()
        assert f3 != null
        addFromGroovyFile(f3)
    }

}
