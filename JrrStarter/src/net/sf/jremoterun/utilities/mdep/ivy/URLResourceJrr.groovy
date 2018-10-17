package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.UrlToFileConverter
import org.apache.ivy.core.settings.TimeoutConstraint
import org.apache.ivy.plugins.repository.url.URLResource;

import java.util.logging.Logger;

@CompileStatic
class URLResourceJrr extends URLResource {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public String jarFilePrefix = 'jar:file:/'
    public JrrBiblioResolver biblioResolver

    URLResourceJrr(URL url) {
        super(url)
    }

    URLResourceJrr(URL url, TimeoutConstraint timeoutConstraint) {
        super(url, timeoutConstraint)
    }

    URLResourceJrr(JrrBiblioResolver biblioResolver, URL url, TimeoutConstraint timeoutConstraint) {
        super(url, timeoutConstraint)
        this.biblioResolver = biblioResolver
    }

    @Override
    boolean exists() {
        File file1 = convertToFile(getURL())
        if (file1 != null) {
            return file1.exists()
        }
        if(!net.sf.jremoterun.utilities.mdep.ivy.UrlSourceChecker.defaultUrlSourceChecker.isCheckUrl(getURL())){
            return false
        }
        if(isNeedLogging(getURL())) {
            log.info "checking existence : ${getURL()}"
        }
        boolean exists =  super.exists()
        net.sf.jremoterun.utilities.mdep.ivy.UrlSourceChecker.defaultUrlSourceChecker.urlExists(exists, getURL())
        return exists
    }


    @Override
    InputStream openStream() throws IOException {
        String string1 = getURL().toString()
        if (string1.startsWith(jarFilePrefix)) {
            return super.openStream()
        }
        File file1 = convertToFile(getURL())
        if(file1!=null){
            return super.openStream()
        }
        if(isNeedLogging(getURL())) {
            log.info "opening stream : ${getURL()}"
        }
        return super.openStream()
    }

    boolean isNeedLogging(URL url){
        return true
    }

    @Override
    long getLastModified() {
        return super.getLastModified()
    }

    @Override
    long getContentLength() {
        return super.getContentLength()
    }


    File convertToFile(URL url) {
        String string1 = url.toString()
        if (string1.startsWith('http://') || string1.startsWith('https://')) {
            return null;
        }
        return UrlToFileConverter.c.convert(url)
    }
}
