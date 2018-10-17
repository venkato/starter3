package net.sf.jremoterun.utilities.classpath.finders

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenId;

import java.util.logging.Logger;

@CompileStatic
interface RepoFileFileFinder {

    File getMavenLocalDir2()

    File findArtifact(MavenId mavenId, String fileType)

    List<String> findAllVersions(MavenId mavenId, String fileType)
}
