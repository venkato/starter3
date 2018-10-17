package net.sf.jremoterun.utilities.classpath.finders

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenCommonUtils
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenPath;

import java.util.logging.Logger;

@CompileStatic
class MavenFileFinder  implements RepoFileFileFinder{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    File mavenLocalDir2;

    MavenFileFinder() {

    }

    MavenFileFinder(File mavenLocalDir2) {
        this.mavenLocalDir2 = mavenLocalDir2
    }

    File findMavenPath(MavenPath mavenPath) {
        File file
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        file = new File(getMavenLocalDir2(), mavenPath.buildMavenPath());
        if (file.exists()) {
            return file;
        }
        return null;
    }

    File findArtifact(MavenId mavenId, String fileType) {
        String suffix = buildMavenPath(mavenId,fileType)
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        File file = new File(getMavenLocalDir2(), suffix)
        return file
    }

    String buildMavenPath(MavenId mavenId,String fileType) {
        mavenId = mavenId.normalize();
        String groupId = mavenId.groupId.replace('.', '/')
        String artifactId = mavenId.artifactId
        String version = mavenId.version
        return "${groupId}/${artifactId}/${version}/${artifactId}-${version}${fileType}.jar"
    }

    File getMavenLocalDir2(){
        return mavenLocalDir2
    }

    List<String> findAllVersions(MavenId mavenId, String fileType) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        mavenId = mavenId.normalize()
        String groupId = mavenId.groupId.replace('.', '/')
        //"${groupId}/${artifactId}/${version}/${artifactId}-${version}.jar"
        File file4 = new File(getMavenLocalDir2(), "${groupId}/${mavenId.artifactId}");
        if (!file4.exists()) {
            return []
        }
        File[] files3 = file4.listFiles()
        if (files3 == null) {
            return [];
        }
        List<File> versionExists = Arrays.asList(files3).findAll {
            String version = it.name
            File jarFile = new File(it, "${mavenId.artifactId}-${version}${fileType}.jar")
            return jarFile.exists()
        }
        return versionExists.collect { it.name }
    }


    MavenId detectMavenIdFromFileName(File file, boolean logMismatch, String fileType) {
        if(getMavenLocalDir2()==null||!getMavenLocalDir2().exists()){
            return null
        }
        if(!MavenCommonUtils.isParentS(getMavenLocalDir2(), file)){
            return null
        }
        File version = file.getParentFile();
        if(version==null){
            log.info("Parent file not found for ${file}")
            return null
        }
        File artifact = version.getParentFile()
        if(artifact==null){
            log.info("Parent Parent file not found for ${file}")
            return null
        }
        String pathToParent = MavenCommonUtils.getPathToParentS(getMavenLocalDir2(), artifact.parentFile);
        if(pathToParent==null){
            return null
        }
        String group = pathToParent.replace('/', '.');
        String suffix = "${artifact.name}-${version.name}${fileType}.jar"
        if (suffix == file.name) {
            return new MavenId(group, artifact.name, version.name)
        }
        if (logMismatch) {
            log.info "not matched : ${suffix} != ${file.name}"
        } else {
            log.fine "not matched : ${suffix} != ${file.name}"
        }
        return null
    }

}
