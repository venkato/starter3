package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilitiesFile
import net.sf.jremoterun.utilities.classpath.FileScriptSource

import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I

import java.security.MessageDigest;
import java.util.logging.Logger;

/**
 * @see net.sf.jremoterun.utilities.classpath.JrrGroovyScriptRunner
 */
@CompileStatic
class GroovyConfigLoaderJrr {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static GroovyConfigLoaderJrr configLoader = new GroovyConfigLoaderJrr()
    public long maxFileLengthInBytes = 1_000_000
    public boolean useCache = true
    public Map<String, Class> scriptCache = [:]

    public MessageDigest messageDigest;

    public java.nio.charset.Charset enconding;

    GroovyConfigLoaderJrr() {

    }

    GroovyConfigLoader2I parseConfig(File f) {
        JrrUtilitiesFile.checkFileExist(f)
        assert f.isFile()
        if (f.length() > maxFileLengthInBytes) {
            throw new Exception("file too big ${f.length()} ${f}")
        }
        try {
            GroovyConfigLoader2I config1 = parseConfig(f.text)
            if (config1 instanceof FileScriptSource) {
                FileScriptSource configLoaderLocationAware = (FileScriptSource) config1;
                configLoaderLocationAware.setFileScriptSource(f)
            }
            return config1
        } catch (Throwable e) {
            log.info("failed load ${f} ${e}")
            throw e
        }
    }

    String calcDigest(String scriptSource) {
        initDigest()
        String digest = new String(messageDigest.digest(scriptSource.getBytes(enconding)), enconding)
        return digest
    }

    GroovyConfigLoader2I parseConfig(String scriptSource) {
        String digest;
        Class clazz1
        if (useCache) {
            digest = calcDigest(scriptSource)
            Class script = scriptCache.get(digest)
            if (script != null) {
                clazz1 = script
            }
        }
        if (clazz1 == null) {
            clazz1 = RunnableFactory.groovyClassLoader.parseClass(scriptSource)
            if (digest != null) {
                scriptCache.put(digest, clazz1);
            }
        }
        GroovyConfigLoader2I configLoader2 = clazz1.newInstance()
        return configLoader2
    }


    void initDigest() {
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance('SHA-256');
        }
        if (enconding == null) {
            enconding = java.nio.charset.Charset.forName('utf8')
        }
    }

}
