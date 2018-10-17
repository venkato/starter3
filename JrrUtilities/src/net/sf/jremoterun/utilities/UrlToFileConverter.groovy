package net.sf.jremoterun.utilities;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class UrlToFileConverter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static UrlToFileConverter c = new UrlToFileConverter();

    public String filePrefix = 'file:/'
    public char column = ':'

    public String jarFilePrefix = 'jar:file:/'

    List<String> convertJarUrlToFile(URL url){
        String string1 = normalizeUrl(url)
        String fileAndZipPath
        if(isWindowsUrl(jarFilePrefix,string1)){
            fileAndZipPath = string1.substring(jarFilePrefix.length())
        }else{
            fileAndZipPath = string1.substring(jarFilePrefix.length()-1)
        }

        List<String> tokenize1 = fileAndZipPath.tokenize('!')
        if (tokenize1.size() < 2) {
            log.info "failed parse ${string1}"
            throw new IOException("failed parse ${string1}")
        }
        if (tokenize1.size() > 2) {
            log.info "starnge size ${string1}"
            throw new IOException("starnge size ${string1}")
        }
        return tokenize1
    }


    boolean isWindowsUrl(String filePrefix2, String url) {
        if (url.length() > (filePrefix2.length() + 2)) {
            char at1 = url.charAt(filePrefix2.length() + 2)
            return at1 == column
        }
        return false

    }

    String normalizeUrl(URL url){
        String string1 = url.toString()
        string1 = string1.replace('%20', ' ')
        return string1
    }

    File convert(URL url) {
        if(url==null){
            throw new NullPointerException('arg is null')
        }
        String string1 = normalizeUrl(url)
        if (!string1.startsWith(filePrefix)) {
            log.info("protocol not supported : ${string1}")
            return null
        }
        File f

        if (isWindowsUrl(filePrefix, string1)) {
            String fs = string1.substring(filePrefix.length())
            f = new File(fs);
        } else {
            String fs = string1.substring(filePrefix.length() - 1)
            f = new File(fs);
        }
        return f
    }


    File convertOld(URL url){
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
