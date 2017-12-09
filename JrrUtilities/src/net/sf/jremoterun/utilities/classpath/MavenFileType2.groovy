package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic;

 @CompileStatic
public enum MavenFileType2 {

    binary(''), source('-sources');

    public String fileSuffix;

    MavenFileType2(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

}
