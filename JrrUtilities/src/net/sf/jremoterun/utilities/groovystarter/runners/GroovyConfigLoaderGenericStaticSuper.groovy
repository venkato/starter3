package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilitiesFile

import java.security.MessageDigest
import java.util.logging.Logger

@CompileStatic
abstract class GroovyConfigLoaderGenericStaticSuper {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public long maxFileLengthInBytes = 1_000_000
    public boolean useCache = true
    public Map<String, Class> scriptCache = [:]

    public MessageDigest messageDigest;

    public java.nio.charset.Charset enconding;


    Class parseConfigClass(File f) {
        JrrUtilitiesFile.checkFileExist(f)
        assert f.isFile()
        if (f.length() > maxFileLengthInBytes) {
            throw new Exception("file too big ${f.length()} ${f}")
        }
        try {
            Class config1 = parseConfigClass(f.text)
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

    Class parseConfigClass(String scriptSource) {
        if (scriptSource.length() > maxFileLengthInBytes) {
            int length1 = (int) Math.min(maxFileLengthInBytes, 100)
            String src = scriptSource.substring(0, length1)
            throw new Exception("Text too big ${scriptSource.length()} : ${src}")
        }
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
            clazz1 = parseClassImpl(scriptSource)
            if (isCacheResult(clazz1)) {
                if (digest != null) {
                    scriptCache.put(digest, clazz1);
                }
            }
        }
        return clazz1
    }

    abstract Class parseClassImpl(String scriptSource)

    boolean isCacheResult(Class clazz1) {
        if (!NonCacheableConfig.isAssignableFrom(clazz1)) {
            return false
        }
        return true
    }

    String hashAlgo = 'SHA-256'

    void initDigest() {
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance(hashAlgo);
        }
        if (enconding == null) {
            enconding = java.nio.charset.Charset.forName('utf8')
        }
    }

}
