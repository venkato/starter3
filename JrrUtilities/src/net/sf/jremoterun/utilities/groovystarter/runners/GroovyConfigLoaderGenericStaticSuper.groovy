package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilitiesFile

import java.security.MessageDigest
import java.util.logging.Logger

@CompileStatic
abstract class GroovyConfigLoaderGenericStaticSuper {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public long maxFileLengthInBytes = 20_000_000
    public boolean useCache = true
    public Map<String, Class> scriptCache = [:]

    public MessageDigest messageDigest;

    public java.nio.charset.Charset encoding;

    public static volatile boolean checkFileEndsWithGroovyS = true
    public volatile boolean checkFileEndsWithGroovy = checkFileEndsWithGroovyS

    public String hashAlgo = 'SHA-256'

    Class parseConfigClass(File f) {
        JrrUtilitiesFile.checkFileExist(f)
        assert f.isFile()
        if (checkFileEndsWithGroovy) {
            assert f.getName().endsWith('.groovy')
        }
        checkFileSize(f)
        try {
            Class config1 = parseConfigClass(f.text, f.getName())
            return config1
        } catch (Throwable e) {
            log.info("failed load ${f} ${e}")
            throw e
        }
    }

    void checkTextSize(String scriptSource) {
        if (scriptSource.length() > maxFileLengthInBytes) {
            int length1 = (int) Math.min(maxFileLengthInBytes, 100)
            String srcBegin = scriptSource.substring(0, length1)
            throw new FileTooBigException("Text too big ${scriptSource.length()} : ${srcBegin}..")
        }
    }

    void checkFileSize(File f) {
        if (f.length() > maxFileLengthInBytes) {
            throw new FileTooBigException("file too big ${f.length()} ${f}")
        }
    }

    String calcDigest(String scriptSource) {
        initDigest()
        String digest = new String(messageDigest.digest(scriptSource.getBytes(encoding)), encoding)
        return digest
    }

    Class parseConfigClass(String scriptSource, String fileName) {
        checkTextSize(scriptSource)
        String digest;
        Class clazz1
        if (useCache) {
            digest = calcDigest(scriptSource)
            Class script = scriptCache.get(digest)
            if (script != null) {
                clazz1 = onCacheUsed(script, digest)
            }
        }
        if (clazz1 == null) {
            clazz1 = parseClassImpl(scriptSource, fileName)
            if (isCacheResult(clazz1)) {
                if (digest != null) {
                    scriptCache.put(digest, clazz1);
                }
            }
        }
        return clazz1
    }

    Class onCacheUsed(Class clazz1, String digest) {
        return clazz1
    }


    abstract Class parseClassImpl(String scriptSource, String fileName)

    boolean isCacheResult(Class clazz1) {
        if (!useCache) {
            return false
        }
        if (NonCacheableConfig.isAssignableFrom(clazz1)) {
            return false
        }
        return true
    }


    void initDigest() {
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance(hashAlgo);
        }
        if (encoding == null) {
            encoding = java.nio.charset.Charset.forName('utf8')
        }
    }

}
