package net.sf.jremoterun.utilities.groovystarter.st


import groovy.transform.CompileStatic

@CompileStatic
public interface ClassPreProcessor {

    void detectAnnotationsOnMainCLass1(String className);
    void detectAnnotationsOnMainCLass2(String groovyText);

}
