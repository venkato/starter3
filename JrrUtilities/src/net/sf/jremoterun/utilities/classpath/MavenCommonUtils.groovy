package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.finders.GrapeFileFinder

import java.util.logging.Logger
;

@CompileStatic
public class MavenCommonUtils {


    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);


    MavenDefaultSettings mavenDefaultSettings = MavenDefaultSettings.mavenDefaultSettings;

    public static String lastVersionInd = '+'


    String fileType = MavenFileType2.binary.fileSuffix

    @Deprecated
    File getDownloadDir() {
        return mavenDefaultSettings.jrrDownloadDir
    }

    @Deprecated
    void setDownloadDir(File downloadDir2) {
        mavenDefaultSettings.jrrDownloadDir = downloadDir2
    }

    String buildDownloadUrlSuffix(URL url) {
        String file2 = url.toString();
        file2 = file2.replace("://", "/")
        if (file2.startsWith('/')) {
            file2 =  file2.substring(1)
        }
        file2 = file2.replace('%25','%')
        file2 = file2.replace('%20','-')
        file2 = file2.replace('%','-')
        file2 = file2.replace(':','-')
        file2 = file2.replace('?','/')
        file2 = file2.replace('=','/')
        file2 = file2.replace('&','/')
        return file2
    }

    File buildDownloadUrl(URL url) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(mavenDefaultSettings.jrrDownloadDir);
        File file3 = new File(mavenDefaultSettings.jrrDownloadDir, buildDownloadUrlSuffix(url));
        return file3
    }

    File downloadJarFromUrl(URL url) {
        File file3 = buildDownloadUrl(url)
        if (!file3.exists()) {
            File parentFile3 = file3.getParentFile()
            if (!parentFile3.exists()) {
                parentFile3.mkdirs()
                assert parentFile3.exists()
            }
            UrlBytesDownloader.defaultDownloader.downloadToFile(url,file3)
        }
        return file3
    }

    URL detectUrlFromFile(File file) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(mavenDefaultSettings.jrrDownloadDir);
        assert isParent(mavenDefaultSettings.jrrDownloadDir, file)
        String parent = getPathToParent(mavenDefaultSettings.jrrDownloadDir, file)
//        int i = parent.indexOf("/")
//        assert i>0
        parent = parent.replaceFirst("/", "://")
        return new URL(parent);
    }

    public static boolean overrideNewAlgouse = true;

   File findMavenPath(MavenPath mavenPath) {
       return mavenDefaultSettings.mavenFileFinder.findMavenPath(mavenPath)
    }

    /**
     * Return last local version. Version in this mavenId will be ignored
     */
    MavenId findLatestMavenOrGradleVersionEx(MavenId mavenId) {
        MavenId mavenId2 = findLatestMavenOrGradleVersion(mavenId);
        if (mavenId2 == null) {
            throw new FileNotFoundException("Failed find latest for ${mavenId}")
        }
        return mavenId2;
    }

    /**
     * Version in this maven id will be used if higher
     */
    MavenId findLatestMavenOrGradleVersion2(MavenId mavenId) {
        MavenId lastestInRepo = findLatestMavenOrGradleVersion(mavenId);
        if (mavenId.version == lastVersionInd) {

        } else {
            if (lastestInRepo == null) {
                lastestInRepo = mavenId
            } else {
                if (mavenDefaultSettings.mavenVersionComparator.isOverrideMavenId2(mavenId, lastestInRepo.version, mavenId.version)) {
                    lastestInRepo = mavenId
                }
            }
        }
        return lastestInRepo
    }

      /**
     * Version in this maven id ignored. Return null, if not found
     */
    MavenId findLatestMavenOrGradleVersion(MavenId mavenId) {
        List<String> allVersion = findAllMavenOrGradleVersions(mavenId);
        if (allVersion == null || allVersion.size() == 0) {
            return null;
        }
        allVersion = allVersion.unique()
        String maxVersion = allVersion.max { String a, String b ->
            if (a == b) {
                return 0;
            }
            boolean bMoreReceint = mavenDefaultSettings.mavenVersionComparator.isOverrideMavenId2(mavenId, a, b)
            return bMoreReceint ? -1 : 1
        }
        MavenId mavenIdLastest = new MavenId(mavenId.groupId, mavenId.artifactId, maxVersion,mavenId.modification);
        assert findMavenOrGradle(mavenIdLastest) != null
        return mavenIdLastest
    }

    @Deprecated
    List<String> findMavenAllVersions(MavenId mavenId) {
        return mavenDefaultSettings.mavenFileFinder.findAllVersions(mavenId,fileType)
    }

    @Deprecated
    List<String> findGradleAllVersions(MavenId mavenId) {
        return mavenDefaultSettings.gradleFileFinder.findAllVersions(mavenId,fileType)
    }

    @Deprecated
    List<String> findGrapeAllVersions(MavenId mavenId) {
        return mavenDefaultSettings.grapeFileFinder.findAllVersions(mavenId,fileType)
    }

    @Deprecated
    String findGrapeVersion(String artifactId,File file){
        return mavenDefaultSettings.grapeFileFinder.findGrapeVersion(artifactId,file)
    }


    List<MavenId> findAllDependeciesWithDownload(MavenId artifact) throws IOException {
        return []
    }

    List<String> findAllMavenOrGradleVersions(MavenId mavenId) {
        List<String> versions = []
        if (mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2() != null && mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2().exists()) {
            versions.addAll(mavenDefaultSettings.grapeFileFinder.findAllVersions(mavenId,fileType))
        }
        if (mavenDefaultSettings.mavenFileFinder.getMavenLocalDir2() != null && mavenDefaultSettings.mavenFileFinder.getMavenLocalDir2()) {
            versions.addAll(findMavenAllVersions(mavenId))
        }
        if (mavenDefaultSettings.gradleFileFinder.getMavenLocalDir2() != null && mavenDefaultSettings.gradleFileFinder.getMavenLocalDir2().exists()) {
            versions.addAll(findGradleAllVersions(mavenId))
        }
        return versions.unique()
    }


    File findMavenOrGradle(MavenId mavenId) {
        if (mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2() != null && mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2().exists()) {
            File file1 = findGrapeAtrifact(mavenId)
            if (file1 != null && file1.exists()) {
                return file1
            }
        }
        if (mavenDefaultSettings.mavenFileFinder.getMavenLocalDir2() != null && mavenDefaultSettings.mavenFileFinder.getMavenLocalDir2().exists()) {
            File file1 = findMavenAtrifact(mavenId)
            if (file1 != null && file1.exists()) {
                return file1
            }
        }
        if (mavenDefaultSettings.gradleFileFinder.getMavenLocalDir2() != null && mavenDefaultSettings.gradleFileFinder.getMavenLocalDir2().exists()) {
            File file1 = findGradleAtrifact(mavenId)
            if (file1 != null && file1.exists()) {
                return file1
            }
        }
        return null
        //throw new FileNotFoundException(buildMavenPath(groupId, artifactId, version));
    }

    @Deprecated
    File findGradleAtrifact(MavenId mavenId) {
        return mavenDefaultSettings.gradleFileFinder.findArtifact(mavenId,fileType)
    }

    @Deprecated
    public static List<String> grapeBinaryPackages = GrapeFileFinder.grapeBinaryPackages


    @Deprecated
    File findGrapeAtrifact(MavenId mavenId) {
        return mavenDefaultSettings.grapeFileFinder.findArtifact(mavenId,fileType)
    }


    @Deprecated
    File findMavenAtrifact(MavenId mavenId) {
        return mavenDefaultSettings.mavenFileFinder.findArtifact(mavenId,fileType)
    }

    String buildMavenPath(MavenId mavenId) {
        mavenId = mavenId.normalize();
        String groupId = mavenId.groupId.replace('.', '/')
        String artifactId = mavenId.artifactId
        String version = mavenId.version
        return "${groupId}/${artifactId}/${version}/${artifactId}-${version}${fileType}.jar"
    }


    MavenId detectMavenIdFromFileName(File file) {
        MavenId mavenId

        mavenId= mavenDefaultSettings.grapeFileFinder.detectMavenIdFromFileName(file,true,fileType)
        if (mavenId != null) {
            return mavenId;
        }
        mavenId= mavenDefaultSettings.mavenFileFinder.detectMavenIdFromFileName(file,true,fileType)
        if (mavenId != null) {
            return mavenId;
        }
        mavenId= mavenDefaultSettings.gradleFileFinder.detectMavenIdFromFileName(file,true,fileType)
        if (mavenId != null) {
            return mavenId;
        }

        return null
    }

    @Deprecated
    MavenId detectMavenIdFromFileNameInGradleDir(File file, boolean logMismatch) {
        return mavenDefaultSettings.gradleFileFinder.detectMavenIdFromFileName(file,true,fileType)
    }

    // log4j uses bundle in pom.xml
//    static Collection<String> grapeBinaryDirs = new HashSet<String>( ['jars','bundles'])

    @Deprecated
    MavenId detectMavenIdFromFileNameInGrapeDir(File file, boolean logMismatch) {
        return mavenDefaultSettings.grapeFileFinder.detectMavenIdFromFileName(file,true,fileType)
    }

    @Deprecated
    MavenId detectMavenIdFromFileNameInMavenDir(File file, boolean logMismatch) {
        return mavenDefaultSettings.mavenFileFinder.detectMavenIdFromFileName(file,true,fileType)
    }


    String getPathToParent(File parent, File child) {
        return getPathToParentS(parent,child)
    }

    static String getPathToParentS(File parent, File child) {
        if(parent==null){
            throw new NullPointerException("parent file is null, child ${child}")
        }
        if(child==null){
            throw new NullPointerException("child file is null, parent ${parent}")
        }
        String parentPath = normalizePathToParent(parent)
        String childPath =  normalizePathToParent(child)

        if(!childPath.toLowerCase().startsWith(parentPath.toLowerCase())){
            return null
        }

        String res = childPath.substring(parentPath.length())
        if (res.startsWith('/')) {
            return res.substring(1)
        }
        return res;
    }


    boolean isParent(File parent, File child) {
        return isParentS(parent,child)
    }

    static String normalizePathToParent(File file){
        if(file==null){
            throw new NullPointerException("file is null")
        }
        String parentPath = file.getCanonicalFile().getAbsolutePath().replace('\\', '/')
        return parentPath
    }

    static String normalizePathIsParent(File file){
        if(file==null){
            throw new NullPointerException("file is null")
        }
        String parentPath = file.getAbsolutePath().replace('\\', '/').toLowerCase()
        return parentPath
    }

    static boolean isParentS(File parent, File child) {
        if(parent==null){
            throw new NullPointerException("parent file is null, child ${child}")
        }
        if(child==null){
            throw new NullPointerException("child file is null, parent ${parent}")
        }
        String parentPath = normalizePathIsParent(parent)
        String childPath =  normalizePathIsParent(child)
        return childPath.startsWith(parentPath)
    }

//    MavenId detectMavenIdFromMavenDir(File file) {
//        File version = file.parentFile;
//        File artifact = version.parentFile
//        String pathToParent = getPathToParent(mavenDefaultSettings.mavenLocalDir, artifact.parentFile);
//        String group = pathToParent.replace('/', '.');
//        String suffix = "${artifact.name}-${version.name}${fileType}.jar"
//        if (suffix == file.name) {
//            return new MavenId(group, artifact.name, version.name)
//        }
//        log.info "not matched : ${suffix} != ${file.name}"
//        return null
//    }


    private static File toolsJarFile;

    File getToolsJarFile() {
        if (toolsJarFile == null) {
            File javahome = System.getProperty('java.home') as File
            assert javahome.exists()
            File toolsJarFile2 = new File(javahome, "../lib/tools.jar")
            if (toolsJarFile2.exists()) {
                toolsJarFile2 = toolsJarFile2.getCanonicalFile().getAbsoluteFile()
            }
            toolsJarFile = toolsJarFile2
        }
        return toolsJarFile
    }

}
