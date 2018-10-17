package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class FileOutputStream2 extends FileOutputStream {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public final File file;



    FileOutputStream2(String file) throws FileNotFoundException {
        this(file as File)
    }

    FileOutputStream2( File file) throws FileNotFoundException {
        super(file)
        this.file = file;
    }

    FileOutputStream2( File file, boolean append) throws FileNotFoundException {
        super(file, append)
        this.file = file;
    }
}
