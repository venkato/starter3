package net.sf.jremoterun.utilities.mdep

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.classpath.MavenDependenciesResolver
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenIdContains
import net.sf.jremoterun.utilities.classpath.ToFileRef2

import java.util.logging.Logger

@CompileStatic
enum DropshipClasspath implements MavenIdContains {

    /**
     * Needed add as GrapeIvy not in groovy custom jar
     */
    groovy('org.codehaus.groovy:groovy:3.0.17')
    , javaAssist('org.javassist:javassist:3.30.2-GA')

    /**
     *  needed for ivy
     */
    , httpCore('org.apache.httpcomponents:httpcore:4.4.16')

    , ivyMavenId('org.apache.ivy:ivy:2.5.2')

    /**
     * ivy uses
     * org.apache.ivy.core.settings.XmlSettingsParser#doParse, uses
     * org.apache.xerces.parsers.AbstractDOMParser, which uses org.w3c.dom.ElementTraversal class. This class located here.
     * Contains a lot of other classes, handle with care.
     */
    , xmlApis('xml-apis:xml-apis:1.4.01')
    ,
    ;


    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    MavenId m;

    DropshipClasspath(String m2) {
        this.m = new MavenId(m2)
    }


//    @Override
//    File resolveToFile() {
//        return m.resolveToFile()
//    }


    public
    static List<? extends MavenIdContains> allLibsWithoutGroovy = [javaAssist, ivyMavenId, httpCore]; // commonsIo,


    public
    static List<? extends MavenIdContains> allLibsWithoutGroovyPlusXmlApi = allLibsWithoutGroovy + [xmlApis.m]

    public
    static List<? extends MavenIdContains> allLibsWithGroovy = allLibsWithoutGroovy + [groovy.m]

    public
    static List<? extends MavenIdContains> allLibsWithGroovyAndXmlApi = allLibsWithoutGroovy + [groovy.m] + [xmlApis.m]

    @Deprecated
    public static List<? extends MavenIdContains> usefullMavenIds = [httpCore.m,]

    @Deprecated
    public
    static List<? extends MavenIdContains> ivyDeps = [javaAssist, httpCore,].collect { it.m }; // commonsIo,

    @Deprecated
    static List<? extends MavenIdContains> ivyDeps2 = allLibsWithGroovy;

    @Deprecated
    public
    static List<? extends MavenIdContains> dropshipBefore = allLibsWithGroovy


    @Deprecated
    public static List<? extends MavenIdContains> dropshipMavenIds = dropshipBefore;

    static void addDropship(AddFilesToClassLoaderGroovy b) {
        allLibsWithGroovy.each { b.addU(it) }
    }


    static void downloadyIvydepToIvyDir() {
        MavenDependenciesResolver mavenDependenciesResolver = MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver;
        if (mavenDependenciesResolver == null) {
            throw new IllegalStateException("MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver is null")
        }
        allLibsWithGroovyAndXmlApi.each {
            mavenDependenciesResolver.resolveAndDownloadDeepDependencies(it.m, false, true)
        }
    }

    static String getGroovyVersion() {
        return DropshipClasspath.groovy.m.version;
    }

}
