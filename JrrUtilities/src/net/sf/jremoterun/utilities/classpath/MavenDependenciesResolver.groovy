package net.sf.jremoterun.utilities.classpath;

import groovy.transform.CompileStatic;

import java.io.File;
import java.net.URL;
import java.util.List;

@CompileStatic
public interface MavenDependenciesResolver {

//    List<MavenId> resolveNoDowloadDeepDependencies(MavenId mavenId);

    void downloadMavenPath(MavenPath path, boolean dep);


    // TODO return file location
    void downloadPathImplSpecific(String path, boolean dep);


    List<MavenId> resolveAndDownloadDeepDependencies(MavenId mavenId, boolean downloadSource, boolean dep);

    void downloadSource(MavenId mavenId);

    File getMavenLocalDir();

    URL getMavenRepoUrl();

}
