package net.sf.jremoterun.utilities.groovystarter.st

import groovy.transform.CompileStatic
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.ClassFile
import javassist.bytecode.annotation.Annotation
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.CompilePhase

import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
class DefaultClassPreProcessor implements ClassPreProcessor {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    GroovyMethodRunnerParams2 gmrp2;

    public volatile ClassNode groovyClassNode
    public volatile ClassFile javassistFile

//    List<String> jrrAnnoataions = [InitFileOrClass.getName(), InitClass.getName(), InitClass.simpleName, InitFileOrClass.simpleName]

    DefaultClassPreProcessor(GroovyMethodRunnerParams2 gmrp2, GroovyMethodRunnerParams gmrp) {
        this.gmrp2 = gmrp2
        if (gmrp2.annotationParser4 == null) {
            gmrp2.annotationParser4 = new DefaultClassByteHandler(gmrp)
        }
    }

    void detectAnnotationsOnMainCLass1(String className) {
        String classPath = className.replace('.', '/');
        InputStream stream = GroovyMethodRunnerParams.gmrpn.groovyClassLoader.getResourceAsStream(classPath + '.groovy');
        try {
            if (stream == null) {
                stream = GroovyMethodRunnerParams.gmrpn.groovyClassLoader.getResourceAsStream(classPath + '.class')
                if (stream != null) {
                    javassistParseAndLoad(stream);
                }
            } else {
                detectAnnotationsOnMainCLass2(stream.text)
            }
        }catch (Throwable e){
            log.log(Level.INFO, "failed parse annotaions for ${className}", e)
            throw e;
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (Exception e) {
                    log.log(Level.INFO, "failed close stream for ${className}", e)
                }
            }
        }
    }

    void javassistParseAndLoad(InputStream inputStream) {
        ClassFile classFile = new ClassFile(new DataInputStream(inputStream))
        javassistFile = classFile
        classFile.interfaces.toList().each { gmrp2.annotationParser4.onJavassistInterface(it) }
        gmrp2.annotationParser4.onJavassisClass(classFile)
        List attributes = classFile.getAttributes()
        List<AnnotationsAttribute> attributes2 = attributes.findAll {
            it instanceof AnnotationsAttribute
        } as List<AnnotationsAttribute>
        int attrSize = attributes2.size()
        switch (attrSize) {
            case 0:
                break
            case 1:

                AnnotationsAttribute attribute3 = attributes2.first();
                List<Annotation> list = attribute3.getAnnotations().toList();
                list.each {
                    gmrp2.annotationParser4.onJavassisAnnotaion(it)
                }
                break;
            default:
                log.info "found many attributes ${attrSize} for ${classFile.name} : ${attributes2}"
                break;
        }
    }


    void detectAnnotationsOnMainCLass2(String groovyText) {
        if (groovyText.contains('@Grab(')||groovyText.contains('@Grapes(')) {
        }else{
            detectAnnotationsOnMainCLassImpl(groovyText)
        }
    }

    void detectAnnotationsOnMainCLassImpl(String groovyText) {
        AstBuilder astBuilder = new AstBuilder();
        List<ASTNode> astNodes = astBuilder.buildFromString(CompilePhase.CONVERSION, true, groovyText);
        astNodes = astNodes.findAll { it instanceof ClassNode }
        int nodeSize = astNodes.size();
        if (nodeSize == 0) {
        } else {
            ClassNode first = astNodes.first() as ClassNode;
            groovyClassNode = first
            first.getAllInterfaces().each { gmrp2.annotationParser4.onGroovyInterface(it) }
            gmrp2.annotationParser4.onGroovyClass(first)
            List<AnnotationNode> annotations = first.annotations
            annotations.each {
                gmrp2.annotationParser4.onGroovyAnnotationClass(it)
            }
        }

    }

}
