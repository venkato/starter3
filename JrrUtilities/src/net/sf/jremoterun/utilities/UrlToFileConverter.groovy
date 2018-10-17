package net.sf.jremoterun.utilities;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class UrlToFileConverter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static UrlToFileConverter c = new UrlToFileConverter();

    File convert(URL url){
        if(url==null){
            throw new NullPointerException("arg is null");
        }
        if(url.getProtocol() != 'file'){
            throw new UnsupportedOperationException("protocol not supported : ${url}")
        }

        String file = url.getFile()
        if(file==null){
            throw new IOException("failed convert url ${url}")
        }
        file = file.replace('%20',' ')
        return new File(file);
    }

}
