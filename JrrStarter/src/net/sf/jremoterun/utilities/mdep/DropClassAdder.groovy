package net.sf.jremoterun.utilities.mdep

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToClassLoaderCommon
import net.sf.jremoterun.utilities.classpath.ClRef

import java.util.logging.Logger

@CompileStatic
enum DropClassAdder {


    javassistRef(new ClRef("javassist.CtClass"), DropshipClasspath.javaAssist),
    httpCoreRef(new ClRef("org.apache.http.HttpConnection"), DropshipClasspath.httpCore),
//    ioUtilsRef(new ClRef("org.apache.commons.io.IOUtils"), DropshipClasspath.commonsIo),
    ivyRef(new ClRef("org.apache.ivy.Ivy"), DropshipClasspath.ivyMavenId),
    ;


    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ClRef clRef;
    DropshipClasspath el;


    DropClassAdder(ClRef clRef2, DropshipClasspath el2) {
        clRef = clRef2
        el = el2
    }


    public static List<DropClassAdder> all = values().toList()

    static void addDepWhichNeeded(AddFilesToClassLoaderCommon adder,ClassLoader cl){
        all.each {
            try {
                it.clRef.loadClass(cl)
            }catch (ClassNotFoundException cnfe){
                adder.add it.el
            }
        }
    }

}
