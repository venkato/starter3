package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream;

@CompileStatic
class JrrZipUtils {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static boolean isZipArchive(File zipArchive) {
        BufferedInputStream inputStream = zipArchive.newInputStream()
        try {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream)
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            return nextEntry != null
        } finally {
            inputStream.close()
        }
    }

    static List<String> listEntries(File zipArchive) {
        BufferedInputStream inputStream = zipArchive.newInputStream()
        try {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream)
            List<String> entries = []
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break
                }
                entries.add(nextEntry.getName())
            }
            if(entries.size()==0){
                throw new Exception("no entry found in ${zipArchive}")
            }
            return entries;
        } finally {
            inputStream.close()
        }
    }


}
