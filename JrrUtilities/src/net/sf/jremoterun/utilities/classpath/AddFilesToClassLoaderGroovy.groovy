package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator2
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableWithParamsFactory

import java.util.logging.Logger

@CompileStatic
public abstract class AddFilesToClassLoaderGroovy extends AddFilesToClassLoaderCommon {

    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

    public JrrGroovyScriptRunner groovyScriptRunner = GroovyMethodRunner.groovyScriptRunner;


    List<File> addedGroovyClassPathFiles = []

    void addFromGroovyFile(File file) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
//        Thread.dumpStack()
        if (addedGroovyClassPathFiles.contains(file)) {
            log.info "${file} already added"
        } else {
            file = file.getAbsoluteFile().getCanonicalFile()
            assert groovyScriptRunner != null
            //groovyScriptRunner.addFilesToClassLoaderF(file, this);
            Object script = net.sf.jremoterun.utilities.groovystarter.runners.GroovyConfigLoaderGeneric.configLoaderGeneric.parseConfig(file)
            LoadScriptFromFileUtils.runWithParams(script,this,null)
//            groovyScriptRunner.runScript(script,this)
            log.fine "added files count : ${addedFiles2.size()}"
            addedGroovyClassPathFiles.add(file)
        }
    }


    @Deprecated
    void addFromGroovyTextFile(String groovyFileContent, String scriptName) {
        assert groovyScriptRunner != null
//        Script script1 = groovyScriptRunner.createScriptClass(groovyFileContent, scriptName, this)
        Object script = groovyScriptRunner.groovyConfigLoaderGeneric.parseConfig(groovyFileContent)
//        Object script = scriptClass.newInstance();
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
