package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class CopyFileUtils {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public File destinationDir;
    public List<String> ignoreNameStartWith=[];
    public List<String> ignoreNameEndsWith=[];

    public static long fileLengthWarn = 100_000_000

    CopyFileUtils(File destinationDir) {
        this.destinationDir = destinationDir
    }

    File copyDir(File f) {
        File localFile = buildFile(f)
        localFile.mkdirs()
        if(!localFile.exists()){
            throw new IOException("failed created ${localFile}")
        }
        f.listFiles().toList().each {
            if(it.isFile()){
                file(it)
            }else{
                copyDir(it)
            }
        }
        return localFile
    }


    File file(File f) {
        try {
            File localFile = buildFile(f)
            copyFileIfNeeded2(f, localFile)
            return localFile
        }catch(Throwable e){
            return onException(f,e)
        }
    }

    File onException(File f , Throwable e){
        log.info "failed copy ${f} ${e}"
        throw e
    }

    File buildFile(File src) {
        return new File(destinationDir, buildSuffix(src))
    }

    String buildSuffix(File src) {
        String suffix = src.getAbsolutePath().replace(':', '/');
        return suffix
    }

    static boolean isCopyFileNeeded(File src, File dest) {
        assert src.exists()
        dest.parentFile.mkdirs()
        assert dest.parentFile.exists()
        if (!dest.exists()) {
            return true
        }
        if (src.length() != dest.length()) {
            return true
        }
        if (src.lastModified() != dest.lastModified()) {
            return true
        }
        return false
    }

    boolean isCopyNeededNonStatic(File src){
        String name1 =src.getName()
        if(!ignoreNameStartWith.isEmpty()) {
            String find1 = ignoreNameStartWith.find { name1.startsWith(it) }
            if (find1 != null) {
                return false
            }
        }
        if(!ignoreNameEndsWith.isEmpty()) {
            String find1 = ignoreNameEndsWith.find { name1.endsWith(it) }
            if (find1 != null) {
                return false
            }
        }
        return true
    }

    void copyFileIfNeeded2(File src, File dest) {
        if(isCopyNeededNonStatic(src)) {
            copyFileIfNeeded(src, dest)
        }
    }

    static void copyFileIfNeeded(File src, File dest) {
        boolean needCopy = isCopyFileNeeded(src, dest)
        if (needCopy) {
            log.info("coping ${src} to ${dest}")
            long fileLength = src.length()
            if(fileLength>fileLengthWarn){
                log.warning "file length is big ${fileLength} ${src}"
            }
            dest.bytes = src.bytes
            dest.setLastModified(src.lastModified())
        }
    }

}
