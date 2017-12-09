package net.sf.jremoterun.utilities.classpath;


import groovy.transform.CompileStatic;

import java.io.File;

@CompileStatic
public interface AddFilesWithSourcesI {

    void addSourceF(File source) throws Exception;

    void addSourceM(MavenId mavenId);

    void addSourceGeneric(Object object);

    void addSourceGenericAll(List objects)

    void addSourceS(String source) throws Exception;

}
