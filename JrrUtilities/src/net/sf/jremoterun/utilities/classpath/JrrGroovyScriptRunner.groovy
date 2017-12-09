package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader

import java.security.MessageDigest
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
        this(JrrClassUtils.currentClassLoader)
        enconding = java.nio.charset.Charset.forName('utf8')
    }

    JrrGroovyScriptRunner(ClassLoader parentClassLoaderToLoad) {
        this.parentClassLoaderToLoad = parentClassLoaderToLoad
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
        Class scriptClass = groovyShell.classLoader.parseClass(scriptSource, scriptName)
        if (useCache) {
            scriptCache.put(digest, scriptClass);
        }
        return scriptClass;
    }

    void initDigest() {
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance('SHA-256');
        }
    }

    void addFilesToClassLoaderF(File file, AddFilesToClassLoaderCommon addCl) {
        addFilesToClassLoader(file.text, file.name, addCl)
    }
}
