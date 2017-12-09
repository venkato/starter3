import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.classpath.*;
import net.sf.jremoterun.utilities.groovystarter.*;

@CompileStatic
class FirstDownloadCustomConfig extends GroovyRunnerConfigurator2 {

    MavenDefaultSettings mds = MavenDefaultSettings.mavenDefaultSettings;

    @Override
    void doConfig() {
        mds.jrrDownloadDir = "" as File;
    }

}

