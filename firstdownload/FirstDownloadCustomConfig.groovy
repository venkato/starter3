import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.classpath.*;
import net.sf.jremoterun.utilities.groovystarter.*;

@CompileStatic
class FirstDownloadCustomConfig implements Runnable {

    MavenDefaultSettings mds = MavenDefaultSettings.mavenDefaultSettings;

    @Override
    void run() {
        mds.jrrDownloadDir = "" as File;
    }

}

