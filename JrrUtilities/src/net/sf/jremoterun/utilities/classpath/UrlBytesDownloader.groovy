package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class UrlBytesDownloader {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static volatile UrlBytesDownloader defaultDownloader = new UrlBytesDownloader();


    byte[] getBytes(URL u){
        return u.bytes
    }

    void downloadToFile(URL u, File f){
        f.bytes = u.bytes
    }

}
