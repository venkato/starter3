package net.sf.jremoterun.utilities.classpath.finders

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenCommonUtils
import net.sf.jremoterun.utilities.classpath.MavenFileType2
import net.sf.jremoterun.utilities.classpath.MavenId

import java.util.logging.Logger
// https://www.jfrog.com/confluence/display/JFROG/Repository+Layouts#RepositoryLayouts-BundledLayouts
@CompileStatic
class GrapeFileFinder  implements RepoFileFileFinder {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    File mavenLocalDir2;



    GrapeFileFinder(){

    }
    GrapeFileFinder(File mavenLocalDir2) {
        this.mavenLocalDir2 = mavenLocalDir2
    }
    public static List<String> grapeBinaryPackages = ['jars', 'bundles', 'eclipse-plugins']

    File getMavenLocalDir2(){
        return mavenLocalDir2
    }

    public static volatile boolean tryVariant = true


    File findArtifact(MavenId mavenId, String fileType) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        mavenId = mavenId.normalize()
        File file;
        if(fileType == MavenFileType2.source.fileSuffix){
            file = new File(getMavenLocalDir2(), "${mavenId.groupId}/${mavenId.artifactId}/sources/${mavenId.artifactId}-${mavenId.version}-sources.jar")
        }else {
            File f =new File(getMavenLocalDir2(), "${mavenId.groupId}/${mavenId.artifactId}")
            if(tryVariant && mavenId.modification!=null) {
                // Not work for https://repo1.maven.org/maven2/org/apache/maven/wagon/wagon-http/3.3.3/wagon-http-3.3.3.-shared.jar
                // need add dot : when maven download jar it is adding . before modifictaion
                File file2 = new File(f, "${mavenId.modification}s/${mavenId.artifactId}-${mavenId.version}.${mavenId.modification}")
                if(file2.exists()){
                    return file2
                }
                file2 = new File(f, "${mavenId.modification}s/${mavenId.artifactId}-${mavenId.version}.jar")
                if(file2.exists()){
                    return file2
                }
            }
            grapeBinaryPackages.find {
                file = new File(f,"${it}/${mavenId.artifactId}-${mavenId.version}.jar")
                return file.exists()
            }
        }
        return file
    }


    MavenId detectMavenIdFromFileName(File file, boolean logMismatch, String fileType) {
        if(getMavenLocalDir2()==null||!(getMavenLocalDir2().exists())){
            return null
        }
        if(!MavenCommonUtils.isParentS(getMavenLocalDir2(), file)){
            return null
        }
        boolean  b1=fileType == MavenFileType2.binary.fileSuffix
        Collection<String> typeFill = b1?grapeBinaryPackages:[fileType.replace('-','')];

        if(!typeFill.contains(file.getParentFile().getName())){
            String msg = "bad name for parent dir ${file.absolutePath}, expected ${typeFill}"
            if(logMismatch) {
                log.info(msg)
            }else{
                log.fine(msg)
            }
            return null
        }
        File artifact = file.getParentFile().getParentFile()
        String group = artifact.parentFile.name;
        String version = findGrapeVersion(artifact.name,file);
        return new MavenId(group, artifact.name, version)
    }

    String findGrapeVersion(String artifactId,File file){
        String name = file.name;
        name = name.substring(0, name.length() - 4)
        if(file.parentFile.name=='sources'){
            name =name.replace('-sources','');
        }
        name = name.replace("${artifactId}-", "")
        return name;
    }

    // TODO Source paring not work
    List<String> findAllVersions(MavenId mavenId, String fileType) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(getMavenLocalDir2())
        mavenId = mavenId.normalize()
        String groupId = mavenId.groupId
        // File file = new File(grapeLocalDir, "${mavenId.groupId}/${mavenId.artifactId}/jars/${mavenId.artifactId}-${mavenId.version}.jar")
        List<String> result = []
        File files7 = new File(getMavenLocalDir2(), "${groupId}/${mavenId.artifactId}")
        if(tryVariant && fileType!= MavenFileType2.source.fileSuffix && mavenId.modification!=null) {
            //File file2 = new File(f, "${mavenId.modification}s/${mavenId.artifactId}-${mavenId.version}.${mavenId.modification}")
            File files6 = new File(files7, mavenId.modification+'s')
            if(files6.exists()){
                result.addAll( files6.listFiles().toList().findAll { it.name.startsWith(mavenId.artifactId) }
                        .findAll { it.name.endsWith('.jar')||it.name.endsWith('.'+mavenId.modification) }
                        .collect {findGrapeVersion(mavenId.artifactId,it)})
            }
        }
        Collection<String> subDirNames = fileType== MavenFileType2.source.fileSuffix ? ['sources']:grapeBinaryPackages;

        subDirNames.each {String grapeBinDir->
            File files6 = new File(files7, grapeBinDir)
            if (!files6.exists()) {
                log.fine "dir not found : ${files6}"
                return []
            }
            result.addAll( files6.listFiles().toList().findAll { it.name.startsWith(mavenId.artifactId) }
                    .findAll { it.name.endsWith('.jar') }
                    .collect {findGrapeVersion(mavenId.artifactId,it)})

        }
        return result
    }




}
