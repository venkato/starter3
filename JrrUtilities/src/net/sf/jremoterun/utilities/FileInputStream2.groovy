package net.sf.jremoterun.utilities;

import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class FileInputStream2 extends FileInputStream{

    File file;


    public FileInputStream2(String name) throws FileNotFoundException {
        this(name as File)
    }

    public FileInputStream2(File file) throws FileNotFoundException {
        super(file)
        this.file = file;
    }
}
