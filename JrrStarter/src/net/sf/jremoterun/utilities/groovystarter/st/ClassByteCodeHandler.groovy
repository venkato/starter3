package net.sf.jremoterun.utilities.groovystarter.st;

import groovy.transform.CompileStatic;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;

@CompileStatic
public interface ClassByteCodeHandler {

    void onGroovyClass(ClassNode classNode);
    void onGroovyAnnotationClass(AnnotationNode classNode);
    void onGroovyInterface(ClassNode interfaceNode);

    void onJavassisClass(ClassFile classFile);
    void onJavassistInterface(String interfaceNode);
    void onJavassisAnnotaion(Annotation classFile);

}
