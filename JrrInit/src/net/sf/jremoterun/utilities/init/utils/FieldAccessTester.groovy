package net.sf.jremoterun.utilities.init.utils;

import groovy.transform.CompileStatic
import java.util.logging.Logger

@CompileStatic
class FieldAccessTester extends Script {

    private static final Logger log = Logger.getLogger(FieldAccessTester.name)

    @Override
    Object run() {
        fieldAccess()
        smartCast()
        isChildFile2()
        child()
        logTester()
        return null
    }

    static void fieldAccess() {
        URLClassLoader cl = new URLClassLoader(new URL[0], FieldAccessTester.classLoader)
        Object pdcache = cl.@pdcache
        pdcache.toString()
    }

    static void smartCast() {
        URL url = 'http://aaa.com' as URL
        url.toString()
    }


    static void isChildFile2() {
        File parent = new File("/a1")
        File child = new File("/a1/b")
        assert parent.isChildFile(child)
    }

    static void child() {
        File parent = new File("/a1")
        File child = parent.child("b")
        child.toString()
    }

    static void logTester(){
        log.debug("some msg")
    }
}
