package net.sf.jremoterun.utilities.mdep

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.classpath.MavenDependenciesResolver
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenIdContains

import java.util.logging.Logger

@CompileStatic
enum DropshipClasspath implements MavenIdContains {

    /**
     * Needed add as GrapeIvy not in groovy custom jar
     */
    groovy('org.codehaus.groovy:groovy:2.4.13')
    , javaAssist('org.javassist:javassist:3.22.0-GA')

    /**
     *  needed for ivy
     */
    , httpCore('org.apache.httpcomponents:httpcore:4.4.9')

    , ivyMavenId("org.apache.ivy:ivy:2.4.0")

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

//    public static List<MavenId> dropshipMavenIdsCore = [
//            commonsIo,
//            'org.sonatype.aether:aether-api:1.13.1'),
//            'org.sonatype.aether:aether-util:1.13.1'),
//            'org.sonatype.aether:aether-impl:1.13.1'),
//            'org.sonatype.aether:aether-spi:1.13.1'),
//            'org.sonatype.aether:aether-connector-wagon:1.13.1'),
//            'org.apache.maven.wagon:wagon-provider-api:1.0-beta-6'),
//            'org.codehaus.plexus:plexus-classworlds:2.4'),
//            'org.codehaus.plexus:plexus-utils:2.0.7'),
//            'org.sonatype.sisu:sisu-inject-plexus:2.2.3'),
//            'org.sonatype.sisu:sisu-inject-bean:2.2.3'),
//
//            'org.apache.maven:maven-aether-provider:3.0.4'),
//            'org.apache.maven:maven-model:3.0.4'),
//            'org.apache.maven:maven-model-builder:3.0.4'),
//            'org.codehaus.plexus:plexus-interpolation:1.14'),
//            'org.apache.maven:maven-repository-metadata:3.0.4'),
//            'org.codehaus.plexus:plexus-component-annotations:1.5.5'),
//            'org.apache.maven.wagon:wagon-file:2.3'),
//            'org.apache.maven.wagon:wagon-http-lightweight:2.3'),
//            'org.apache.maven.wagon:wagon-http-shared4:2.3'),
//            'org.hamcrest:hamcrest-core:1.3'),
//    ]
//    public static MavenId sisiFileSrc = 
//            'org.sonatype.sisu:sisu-guice:3.0.3')

//    public static MavenPath sisiFileBin = new MavenPath(
//            'org.sonatype.sisu', 'sisu-guice', '3.0.3', '-no_aop.jar')

