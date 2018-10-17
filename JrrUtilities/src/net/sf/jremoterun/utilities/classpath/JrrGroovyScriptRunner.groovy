package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader
import net.sf.jremoterun.utilities.groovystarter.LoadScriptFromFileUtils


import java.security.MessageDigest
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @see net.sf.jremoterun.utilities.nonjdk.classpath.GroovySettingsLoader
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

    protected void initCl(ClassLoader parentClassLoaderToLoad){
        this.parentClassLoaderToLoad = parentClassLoaderToLoad
        enconding = java.nio.charset.Charset.forName('utf8')
        groovyShell = createGroovyShell(parentClassLoaderToLoad)
    }

    GroovyShell createGroovyShell(ClassLoader parentClassLoaderToLoad) throws Exception {
        GroovyShell groovyShell = new GroovyShell(parentClassLoaderToLoad);
        return groovyShell;
    }

    void addFilesToClassLoader(String scriptSource, String scriptName, AddFilesToClassLoaderCommon addCl) {
        Script script = createScript(scriptSource, scriptName, addCl);
        if (script instanceof GroovyConfigLoader) {
            GroovyConfigLoader ru = (GroovyConfigLoader) script;
            AddFilesToClassLoaderGroovy ad = addCl as AddFilesToClassLoaderGroovy
            ru.loadConfig(ad)
        } else {
            script.run()
        }
    }


    Script createScript(String scriptSource, String scriptName, AddFilesToClassLoaderCommon addCl) {
        Class scriptClass = createScriptClass(scriptSource, scriptName)
        Binding binding = new Binding()
        binding.setVariable(ClasspathConfigurator.varName, addCl)
        Script script = (Script) scriptClass.newInstance();
        script.setBinding(binding)
        return script
    }



    Class createScriptClass(String scriptSource, String scriptName) {
        if(scriptSource==null){
            throw new NullPointerException('scriptSource is null')
        }

        if(scriptName==null){
            throw new NullPointerException('scriptName is null')
        }
        if(enconding==null){
            throw new NullPointerException('encoding is null')
        }
        String digest;
        if (useCache) {
            initDigest()
            digest = new String(messageDigest.digest(scriptSource.getBytes(enconding)), enconding)
            Class script = scriptCache.get(digest)
            if (script != null) {
                return script;
            }
        }
        log.fine "creating script ${scriptName}"
        Class scriptClass = groovyShell.getClassLoader().parseClass(scriptSource, scriptName)
        if (useCache) {
            scriptCache.put(digest, scriptClass);
        }
        return scriptClass;
    }

    Object loadSettingsNoParam(String scriptSource, String scriptName) {
        Class scriptClass = createScriptClass(scriptSource, scriptName)
        Object instance = scriptClass.newInstance()
        return LoadScriptFromFileUtils.runNoParams(instance,  null)
    }


    Object loadSettingsNoParam(File file) {
        try {
            JrrUtilities3.checkFileExist(file)
            return loadSettingsNoParam(file.text, file.name)
        } catch (Throwable e) {
            log.log(Level.SEVERE,"failed load ${file}",e)
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
            JrrUtilities3.checkFileExist(file)
            return loadSettingsWithParam(file.text, file.name, param)
        } catch (Throwable e) {
            log.log(Level.SEVERE,"failed load ${file}",e)
            throw e
        }
    }

    void initDigest() {
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance('SHA-256');
        }
    }



    void addFilesToClassLoaderF(File file, AddFilesToClassLoaderCommon addCl) {
        if(LoadScriptFromFileUtils.checkFileEndsWithGroovy){
            if(!file.getName().endsWith('.groovy')){
                throw new Exception("that is not groovy file : ${file.getAbsolutePath()}")
            }
        }
        addFilesToClassLoader(file.text, file.name, addCl)
    }
}
