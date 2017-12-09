package net.sf.jremoterun.utilities.test

import groovy.transform.CompileStatic
import junit.framework.TestCase
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.URLClassLoaderExt
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.MavenDependenciesResolver
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.javaservice.CallProxy
import net.sf.jremoterun.utilities.javaservice.CallProxyHandler
import net.sf.jremoterun.utilities.javaservice.ObjectPass
import net.sf.jremoterun.utilities.javassist.InvokcationAccessor
import net.sf.jremoterun.utilities.mdep.DropshipClasspath

import java.util.logging.Logger

@CompileStatic
class ClassProxyTest extends TestCase {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    SimpleService service;

//    private static boolean inited = false;

    ClassProxyTest() {
//        if(!inited) {
//            inited = true;
            createDropshipDependencyResolvedInSeparetClassloader()
//        }else{
//            Thread.dumpStack()
//        }
    }



    void createDropshipDependencyResolvedInSeparetClassloader() {
//        log.info("cp1")
//        Thread.dumpStack()
        ClassLoader parent = ClassLoader.getSystemClassLoader().parent
        assert parent.class.name == 'sun.misc.Launcher$ExtClassLoader';
        URLClassLoaderExt urlClassLoader = new URLClassLoaderExt(new URL[0], parent)
        AddFilesToUrlClassLoaderGroovy adder = new AddFilesToUrlClassLoaderGroovy(urlClassLoader)
        adder.add(DropshipClasspath.httpCore)
        adder.addGenericEntery(DropshipClasspath.groovy)
        adder.addFileWhereClassLocated(SimpleService)
//        adder.addFileWhereClassLocated(JrrClassUtils)
        adder.addFileWhereClassLocated(JrrUtils)
        adder.addFileWhereClassLocated(MavenDependenciesResolver)

        ClassLoader loaderBefore = Thread.currentThread().getContextClassLoader()
        try {
            Thread.currentThread().setContextClassLoader(urlClassLoader)
            Class<?> clazz = urlClassLoader.loadClass(SimpleService.name)
            Object newInstance = clazz.newInstance()
            assert clazz.classLoader == urlClassLoader
            CallProxy.regServive(newInstance)
            service = CallProxy.makeProxy2(SimpleService, newInstance) as SimpleService
            InvokcationAccessor ia = service as InvokcationAccessor
            CallProxyHandler handler = ia._getJavassistProxyFactory().methodHandler as CallProxyHandler;
            assert handler.methodsMap.size() ==0
            handler.useSerializationForCollection = true;
            handler.paramsPass.put(MavenDependenciesResolver.name,ObjectPass.proxy);
        } finally {
            Thread.currentThread().setContextClassLoader(loaderBefore)
        }

    }



    void test2() {
        assert service.noParams() == 'net.sf.jremoterun.URLClassLoaderExt';
        assert service.v1(true, 'hi') == "boot classloader";
    }

    void test3() {
        String res = service.v2(true, new MavenId("log4j:log4j:1.1.1"));
        assert res == 'net.sf.jremoterun.URLClassLoaderExt'
    }

    void test4() {
        String res = service.v3(true, new MavenId("log4j:log4j:1.1.1"));
        assert res == '1.1.1'
    }

    void test5() {
        String res = service.protectedMethod(new MavenId("log4j:log4j:1.1.1"));
        assert res == '1.1.1'
    }

    void test6() {
        MavenId res = service.returnDiffClassloader();
        assert res.version == '1.1.1'
    }

    void test7() {
        String res = service.listStringParam(['a', 'b']);
        assert res == 'a'
    }

    void test9() {
        int i = 1
        Runnable r = {
            i = 2
        };
        int hashCode = service.nonSerializableParam r
        assert hashCode == r.hashCode()
        assert i == 2
    }

    void testToString() {
        String asString = service.toString()
        assert asString .contains(SimpleService.name)
    }


    void test8() {
        List<MavenId> arg= [new MavenId("log4j:log4j:1.1.1"),new MavenId("log4j:log4j:1.2.1")];
        log.info "${arg.class.name}"
        String res = service.listMavenIdParam(arg);
        assert res == '1.1.1'
    }

    void test11() {
        List<MavenId> res = service.resultListMavenIdParam();
        assert res.first().version == '1.1'
    }

    void testException() {
        try {
            service.passException()
            fail()
        }catch (IllegalArgumentException e){
            assert e.message =='check'
        }


    }

    void test14() {
        MavenDependenciesResolver res = service.mavenDependenciesResolver;
        service.passMavenDependenciesResolver(res,0)
        service.passMavenDependenciesResolver(res,1)
    }


    void test12() {
        MavenDependenciesResolver res = service.mavenDependenciesResolver;
        MavenId id = new MavenId("log4j:log4j:1.1.1");
        res.downloadSource(id)
        List<MavenId> dependencies = res.resolveAndDownloadDeepDependencies(id, true, true)

        assert dependencies.first().version == '1.1.1'
    }


}
