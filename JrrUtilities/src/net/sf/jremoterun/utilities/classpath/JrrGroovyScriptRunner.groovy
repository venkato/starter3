package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator2

import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils


import java.security.MessageDigest
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @see net.sf.jremoterun.utilities.nonjdk.classpath.sl.GroovySettingsLoader
 * @see net.sf.jremoterun.utilities.nonjdk.classpath.sl.GroovySettingsLoader2
 */
@CompileStatic
class JrrGroovyScriptRunner {
    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

    ClassLoader parentClassLoaderToLoad

    GroovyShell groovyShell

    boolean useCache = true

    Map<String, Class> scriptCache = [:]

    MessageDigest messageDigest;

    java.nio.charset.Charset enconding;

    JrrGroovyScriptRunner() {
        this(JrrClassUtils.getCurrentClassLoader())

    }

    JrrGroovyScriptRunner(ClassLoader parentClassLoaderToLoad) {
        initCl(parentClassLoaderToLoad)
    }

    protected void initCl(ClassLoader parentClassLoaderToLoad) {
        this.parentClassLoaderToLoad = parentClassLoaderToLoad
        groovyShell = createGroovyShell(parentClassLoaderToLoad)
    }

    GroovyShell createGroovyShell(ClassLoader parentClassLoaderToLoad) throws Exception {
        GroovyShell groovyShell = new GroovyShell(parentClassLoaderToLoad);
        return groovyShell;
    }

    void addFilesToClassLoader(String scriptSource, String scriptName, AddFilesToClassLoaderCommon addCl, File file) {
        Class scriptClass = createScriptClass(scriptSource, scriptName)
        Object script = scriptClass.newInstance();
        //Script script = createScript(scriptSource, scriptName, addCl);
        if (script instanceof FileScriptSource) {
            FileScriptSource configLoaderLocationAware = script
            configLoaderLocationAware.setFileScriptSource(file)
        }
        runScript(script,addCl)
    }

    void runScript(Object script, AddFilesToClassLoaderCommon addCl){
        if (script instanceof GroovyConfigLoader2I) {
            GroovyConfigLoader2I ru = (GroovyConfigLoader2I) script;
//            ru.thisFile = file;
            //AddFilesToClassLoaderGroovy ad = addCl as AddFilesToClassLoaderGroovy
            ru.loadConfig(addCl)
        } else {
            Script script2 = script as Script
            Binding binding = new Binding()
            binding.setVariable(ClasspathConfigurator2.varName, addCl)
            script2.setBinding(binding)
            script2.run()
        }
    }


    Script createScript(String scriptSource, String scriptName, AddFilesToClassLoaderCommon addCl) {
        Class scriptClass = createScriptClass(scriptSource, scriptName)
        Binding binding = new Binding()
        binding.setVariable(ClasspathConfigurator2.varName, addCl)
        Script script = (Script) scriptClass.newInstance();
        script.setBinding(binding)
        return script
    }


    Class createScriptClass(String scriptSource, String scriptName) {
        if (scriptSource == null) {
            throw new NullPointerException('scriptSource is null')
        }

        if (scriptName == null) {
            throw new NullPointerException('scriptName is null')
        }
        String digest;
        if (useCache) {
            digest = calcDigest(scriptSource)
            Class script = scriptCache.get(digest)
            if (script != null) {
                return script;
            }
        }
        log.fine "creating script ${scriptName}"
        Class scriptClass = groovyShell.getClassLoader().parseClass(scriptSource, scriptName)
        if (digest != null) {
            scriptCache.put(digest, scriptClass);
        }
        return scriptClass;
    }


    String calcDigest(String scriptSource) {
        initDigest()
        String digest = new String(messageDigest.digest(scriptSource.getBytes(enconding)), enconding)
        return digest
    }

    Object loadSettingsNoParam(String scriptSource, String scriptName) {
        Class scriptClass = createScriptClass(scriptSource, scriptName)
        Object instance = scriptClass.newInstance()
        return LoadScriptFromFileUtils.runNoParams(instance, null)
    }


    Object loadSettingsNoParam(File file) {
        try {
            net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
            return loadSettingsNoParam(file.text, file.name)
        } catch (Throwable e) {
            log.log(Level.SEVERE, "failed load ${file}", e)
            throw e
        }
    }


    Object loadSettingsWithParam(String scriptSource, String scriptName, Object param) {
        Class scriptClass = createScriptClass(scriptSource, scriptName)
        Object instance = scriptClass.newInstance()
        return LoadScriptFromFileUtils.runWithParams(instance, param, null)
    }


    Object loadSettingsWithParam(File file, Object param) {
        try {
            net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
            return loadSettingsWithParam(file.text, file.name, param)
        } catch (Throwable e) {
            log.log(Level.SEVERE, "failed load ${file}", e)
            throw e
        }
    }

    void initDigest() {
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance('SHA-256');
        }
        if(enconding==null){
            enconding = java.nio.charset.Charset.forName('utf8')
        }
    }


    void addFilesToClassLoaderF(File file, AddFilesToClassLoaderCommon addCl) {
        if (LoadScriptFromFileUtils.checkFileEndsWithGroovy) {
            if (!file.getName().endsWith('.groovy')) {
                throw new Exception("that is not groovy file : ${file.getAbsolutePath()}")
            }
            long length = file.length()
            if (LoadScriptFromFileUtils.maxGroovyFileSize != 0 && length > LoadScriptFromFileUtils.maxGroovyFileSize) {
                throw new Exception("file length too big ${length / 1000} kbytes, ${file.getAbsolutePath()}")
            }
        }
        addFilesToClassLoader(file.text, file.getName(), addCl, file)
    }
}
