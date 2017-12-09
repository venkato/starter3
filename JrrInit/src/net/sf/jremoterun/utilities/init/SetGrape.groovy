package net.sf.jremoterun.utilities.init

import groovy.grape.Grape
import groovy.grape.GrapeIvy
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.mdep.ivy.IvyDepResolver2
import org.apache.ivy.Ivy

import java.util.logging.Logger

@CompileStatic
class SetGrape implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    @Override
    void run() {
        IvyDepResolver2 ivyDepResolver2 = MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver as IvyDepResolver2
        Ivy ivy = ivyDepResolver2.ivy
        GrapeIvy grapeIvy = new GrapeIvy()
        JrrClassUtils.setFieldValue(grapeIvy, 'ivyInstance', ivy)
        JrrClassUtils.setFieldValue(Grape, 'instance', grapeIvy)
    }
}
