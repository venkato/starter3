package net.sf.jremoterun.utilities


import net.sf.jremoterun.utilities.classpath.ClRef


import groovy.transform.CompileStatic;


@CompileStatic
class EmptyGroovyRunner implements Runnable {

    public static ClRef cnr = new ClRef(EmptyGroovyRunner);

    @Override
    void run() {
    }
}
