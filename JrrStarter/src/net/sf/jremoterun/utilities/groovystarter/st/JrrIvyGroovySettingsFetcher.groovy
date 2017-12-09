package net.sf.jremoterun.utilities.groovystarter.st

import groovy.grape.Grape
import groovy.grape.GrapeIvy;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.Ivy;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JrrIvyGroovySettingsFetcher {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    static GrapeIvy getGrapeIvy(){
        GrapeIvy instance = Grape.getInstance() as GrapeIvy;
        return instance;
    }

}
