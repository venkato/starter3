package net.sf.jremoterun.utilities.groovystarter.st

import groovy.transform.CompileStatic
import javassist.bytecode.ClassFile
import javassist.bytecode.annotation.Annotation
import javassist.bytecode.annotation.ClassMemberValue
import javassist.bytecode.annotation.MemberValue
import javassist.bytecode.annotation.StringMemberValue
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression

import java.util.logging.Logger

@CompileStatic
class DefaultClassByteHandler implements ClassByteCodeHandler {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    GroovyMethodRunnerParams gmrp;

    public ClassNode groovyClassNode
    public ClassFile javassistFile

    List<String> jrrAnnoataions = [InitFileOrClass.getName(), InitClass.getName(),InitAddFile.getName(), //
                                   InitClass.simpleName, InitFileOrClass.simpleName,InitAddFile.simpleName]

    DefaultClassByteHandler(GroovyMethodRunnerParams gmrp) {
        this.gmrp = gmrp
    }


    @Override
    void onGroovyClass(ClassNode classNode) {
        groovyClassNode = classNode
    }

    @Override
    void onGroovyAnnotationClass(AnnotationNode it) {
        if (jrrAnnoataions.contains(it.classNode.name)) {
            Expression member = it.getMember('value');
            switch (it.classNode.name) {
//                case Grape.simpleName:
//                case Grape.name:
//                case Grapes.name:
//                case Grapes.simpleName:
//                    GroovyRunnerConfigurator.addIvyToGroovy()
//                    break
                case InitFileOrClass.simpleName:
                case InitFileOrClass.name:
                    ConstantExpression constantExpression = member as ConstantExpression;
                    loadAnnotationFileNameOrClass(constantExpression.text)
                    break;
                case InitAddFile.simpleName:
                case InitAddFile.name:
                    ConstantExpression constantExpression = member as ConstantExpression;
                    addFileToClassPath(constantExpression.text)
                    break
                case InitClass.simpleName:
                case InitClass.name:
                    ClassExpression classExpression = member as ClassExpression
                    String className = classExpression.text
                    loadAnnotationClass(className)
                    break;
            }
        }
    }

    @Override
    void onJavassisClass(ClassFile className) {
        javassistFile = className
    }

    @Override
    void onJavassistInterface(String interfaceNode){

    }

    @Override
    void onGroovyInterface(ClassNode interfaceNode){

    }

    @Override
    void onJavassisAnnotaion(Annotation it) {
        if(jrrAnnoataions.contains(it.typeName)) {
            MemberValue value = it.getMemberValue('value')
            if (it.typeName == InitFileOrClass.name) {
                StringMemberValue value2 = value as StringMemberValue
                loadAnnotationFileNameOrClass(value2.value)
            }
            if (it.typeName == InitClass.name) {
                ClassMemberValue memberValue2 = value as ClassMemberValue
                loadAnnotationClass(memberValue2.value)
            }
        }
    }


    void loadAnnotationFileNameOrClass(String className) {
//        log.info className
        File file = className as File
        if (file.exists()) {
            Class clazz = gmrp.groovyClassLoader.parseClass(file)
            loadAnnotationClass2(clazz)
        } else {
            loadAnnotationClass(className);
        }
    }



    void addFileToClassPath(String fileName) {
//        log.info className
        File file = fileName as File
        assert file.exists()
        gmrp.addFilesToClassLoader.addF(file)
    }

    void loadAnnotationClass(String className) {
//        log.info "${className}"
        Class<?> clazz = gmrp.groovyClassLoader.loadClass(className)
        loadAnnotationClass2(clazz)
    }

    void loadAnnotationClass2(Class clazz) {
        def onObject = clazz.newInstance()
        loadAnnotationObject(onObject)
    }

    void loadAnnotationObject(Object onObject) {
        if (onObject instanceof Script) {
            Script script = onObject as Script
            script.getBinding().setVariable('a', gmrp.addFilesToClassLoader)
            script.run()
        } else if (onObject instanceof Runnable) {
            Runnable runnable = onObject as Runnable
            runnable.run()
        } else {
            log.severe "not aware how run : ${onObject.class.name}"
        }
    }
}
