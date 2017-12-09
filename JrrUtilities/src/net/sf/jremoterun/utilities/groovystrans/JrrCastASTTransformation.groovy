package net.sf.jremoterun.utilities.groovystrans

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation

import java.util.logging.Logger

@CompileStatic
class JrrCastASTTransformation implements ASTTransformation, Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static ClRef clRef = new ClRef("net.sf.jremoterun.utilities.nonjdk.langi.JrrStaticCompileTransformation")

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        JrrCastTransform transform = new JrrCastTransform(source)
        ClassNode node = nodes.find { it instanceof ClassNode } as ClassNode
//            System.out.println("hi cp2 : ${node} ")
//            System.out.println("hi cp2 : ${nodes.length} : ${node.class.getName()} ")
        if (node != null) {
//                System.out.println("hi cp3")
            JrrCastTransform jrrCastTransform = new JrrCastTransform(source)
            ClassNode classNode = (ClassNode) node;
            jrrCastTransform.visitClass(classNode);
        }
    }

    @Override
    void run() {
        f1()
    }

    void f1() {
        Class<?> class2 = clRef.loadClass2()
        List<ASTTransformation> trans = JrrClassUtils.getFieldValue(class2, 'astTransformationsBefore') as List<ASTTransformation>
        ASTTransformation find = trans.find { it.class.name == JrrCastASTTransformation.name }
        if (find == null) {
            trans.add(this)
        } else {
            log.info "already added ${find}"
        }

    }
}
