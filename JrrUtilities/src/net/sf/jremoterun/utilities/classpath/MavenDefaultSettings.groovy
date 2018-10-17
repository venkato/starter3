package net.sf.jremoterun.utilities.classpath

import groovy.transform.AutoClone;
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.finders.GradleFileFinder
import net.sf.jremoterun.utilities.classpath.finders.GrapeFileFinder
import net.sf.jremoterun.utilities.classpath.finders.MavenFileFinder;

import java.io.File;
import java.util.*;

@AutoClone
@CompileStatic
public class MavenDefaultSettings {



    public volatile String mavenCentralRepoName = "central";

    public volatile String mavenCustomRepoName = "custom";

    public volatile String mavenServer = "https://repo1.maven.org/maven2";

    public volatile File userHome = new File(System.getProperty("user.home"));

    public volatile GrapeFileFinder grapeFileFinder = new GrapeFileFinder(new File(userHome, ".ivy2/cache"));

    public volatile GradleFileFinder gradleFileFinder = new GradleFileFinder(new File(userHome, ".gradle/caches/modules-2/files-2.1"));

    public volatile MavenFileFinder mavenFileFinder = new MavenFileFinder(new File(userHome, ".m2/repository"));



    public volatile Collection<String> badMavenVersionWords = ["alpha", "beta", "-cr", "-rc","snapshot",".cr",".rc"];

    volatile MavenDependenciesResolver mavenDependenciesResolver;
    public volatile File jrrDownloadDir;
    public volatile CustomObjectHandler customObjectHandler;
    public volatile MavenVersionComparatorI mavenVersionComparator = new MavenVersionComparator();

    @Deprecated
    void setMavenLocalDir3(File f){
        mavenFileFinder.setMavenLocalDir2( f)
    }

    @Deprecated
    void setGrapeLocalDir3(File f){
        grapeFileFinder.setMavenLocalDir2( f)
    }

    @Deprecated
    void setGradleLocalDir3(File f){
        gradleFileFinder.setMavenLocalDir2(f)
    }


    public static volatile MavenDefaultSettings mavenDefaultSettings = new MavenDefaultSettings();


}
