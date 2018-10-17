package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic

@CompileStatic
class SystemExit {

    static void exit(int exitCode){
        System.exit(exitCode);
    }


}
