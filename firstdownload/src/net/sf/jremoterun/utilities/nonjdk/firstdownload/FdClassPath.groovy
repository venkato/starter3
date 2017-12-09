package net.sf.jremoterun.utilities.nonjdk.firstdownload

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenIdContains
import net.sf.jremoterun.utilities.mdep.DropshipClasspath

@CompileStatic
enum FdClassPath implements MavenIdContains {

    git('org.eclipse.jgit:org.eclipse.jgit:5.0.1.201806211838-r'),
    commnonsLang('commons-lang:commons-lang:2.6'),
    log4jOld('log4j:log4j:1.2.17'),
    jcraft('com.jcraft:jsch:0.1.54'),
    javaewah('com.googlecode.javaewah:JavaEWAH:1.1.6'),
    slf4jApi('org.slf4j:slf4j-api:1.7.25'),
    commonsLoggingMavenId('commons-logging:commons-logging:1.2'),
    slf4jImplJdkLogger('org.slf4j:slf4j-jdk14:1.7.25'),
    // needed ?
    commonsIo('commons-io:commons-io:2.5'),
    ;


    MavenId m;

    FdClassPath(String m2) {
        this.m = new MavenId(m2)
    }


    public static List<FdClassPath> allThis = values().toList()

    public static List mavenIds = DropshipClasspath.allLibsWithoutGroovy + (List) allThis


}
