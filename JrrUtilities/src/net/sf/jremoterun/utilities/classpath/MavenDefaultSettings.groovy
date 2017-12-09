package net.sf.jremoterun.utilities.classpath

import groovy.transform.AutoClone;
import groovy.transform.CompileStatic;

import java.io.File;
import java.util.*;

@AutoClone
@CompileStatic
public class MavenDefaultSettings {



    public volatile String mavenCentralRepoName = "central";

    public volatile String mavenCustomRepoName = "custom";

    public volatile String mavenServer = "https://repo1.maven.org/maven2";

    public volatile File userHome = new File(System.getProperty("user.home"));

    public volatile File mavenLocalDir = new File(userHome, ".m2/repository");

    public volatile File gradleLocalDir = new File(userHome, ".gradle/caches/modules-2/files-2.1");

    public volatile File grapeLocalDir = new File(userHome, ".ivy2/cache");

    public volatile Collection<String> badMavenVersionWords = ["alpha", "beta", "-cr", "-rc","snapshot",".cr",".rc"];

    public volatile MavenDependenciesResolver mavenDependenciesResolver;
    public volatile File jrrDownloadDir;
    public volatile CustomObjectHandler customObjectHandler;
    public volatile MavenVersionComparatorI mavenVersionComparator = new MavenVersionComparator();

    public static volatile MavenDefaultSettings mavenDefaultSettings = new MavenDefaultSettings();
}
