package net.sf.jremoterun.utilities.classpath.finders

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenCommonUtils
import net.sf.jremoterun.utilities.classpath.MavenId

import java.util.logging.Logger

@CompileStatic
class GradleFileFinder implements RepoFileFileFinder{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    File mavenLocalDir2;

    GradleFileFinder() {

    }
    GradleFileFinder(File mavenLocalDir2) {
        this.mavenLocalDir2 = mavenLocalDir2
    }

    File getMavenLocalDir2(){
        return mavenLocalDir2
    }


    File findArtifact(MavenId mavenId, String fileType) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        mavenId = mavenId.normalize()
        File[] files = new File(getMavenLocalDir2(), "${mavenId.groupId}/${mavenId.artifactId}/${mavenId.version}").listFiles()
        String suffixx = "${mavenId.artifactId}-${mavenId.version}${fileType}.jar"

        for (File file1 : files) {
            File jarFile = new File(file1, suffixx)
            if (jarFile.exists()) {
                return jarFile
            }
        }
        return null
    }


    MavenId detectMavenIdFromFileName(File file, boolean logMismatch, String fileType) {
        if(getMavenLocalDir2()==null||!getMavenLocalDir2().exists()){
            return null
        }
        if(!MavenCommonUtils.isParentS(getMavenLocalDir2(), file)){
            return null
        }
        File someFile = file.getParentFile()
        if(someFile==null){
            log.info("Parent file not found for ${file}")
            return null
        }

        File version = someFile.getParentFile();
        if(version==null){
            log.info("Parent file not found for ${file}")
            return null
        }
        File artifact = version.getParentFile()
        if(artifact==null){
            log.info("Parent parent file not found for ${file}")
            return null
        }
        File someParent2 = artifact.getParentFile()
        if(someParent2==null){
            throw new IOException("failed get parent from ${artifact}")
        }
        String group = MavenCommonUtils.getPathToParentS(getMavenLocalDir2(), someParent2);
        if(group==null){
            return null
        }
        String suffix = "${artifact.name}-${version.name}${fileType}.jar"
        if (suffix == file.name) {
            return new MavenId(group, artifact.name, version.name)
        }
        if (logMismatch) {
            log.info "not matched suffix : ${suffix} != ${file.name}"
        } else {
            log.fine "not matched suffix : ${suffix} != ${file.name}"
        }
        return null;
    }

    List<String> findAllVersions(MavenId mavenId, String fileType) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        mavenId = mavenId.normalize()
        File artifactDir = new File(getMavenLocalDir2(), "${mavenId.groupId}/${mavenId.artifactId}");
        if (!artifactDir.exists()) {
            return [];
        }
        File[] files3 = artifactDir.listFiles()
        List<File> versionExists = Arrays.asList(files3).findAll {
            String version = it.name
            File[] files = it.listFiles()
            String suffixx = "${mavenId.artifactId}-${version}${fileType}.jar"

            for (File file1 : files) {
                File jarFile = new File(file1, suffixx)
                if (jarFile.exists()) {
                    return true;
                }
            }
            return false;
        }
        return versionExists.collect { it.name }
    }



}
