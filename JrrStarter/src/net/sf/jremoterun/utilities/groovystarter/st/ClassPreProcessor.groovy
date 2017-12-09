package net.sf.jremoterun.utilities.groovystarter.st;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClassNameReference;

import java.util.logging.Logger;

import groovy.transform.CompileStatic;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;

@CompileStatic
public interface ClassPreProcessor {

    void detectAnnotationsOnMainCLass1(String className);
    void detectAnnotationsOnMainCLass2(String groovyText);

}
