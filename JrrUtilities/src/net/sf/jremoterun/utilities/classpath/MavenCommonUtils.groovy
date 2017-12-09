package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3

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
            file2 = file2.substring(1)
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
        JrrUtilities3.checkFileExist(mavenDefaultSettings.jrrDownloadDir);
        File file3 = new File(mavenDefaultSettings.jrrDownloadDir, buildDownloadUrlSuffix(url));
        return file3
    }

    File downloadJarFromUrl(URL url) {
        File file3 = buildDownloadUrl(url)
        if (!file3.exists()) {
            if (!file3.parentFile.exists()) {
                assert file3.parentFile.mkdirs()
            }
            file3.bytes = url.bytes
        }
        return file3
    }

    URL detectUrlFromFile(File file) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.jrrDownloadDir);
        assert isParent(mavenDefaultSettings.jrrDownloadDir, file)
        String parent = getPathToParent(mavenDefaultSettings.jrrDownloadDir, file)
//        int i = parent.indexOf("/")
//        assert i>0
        parent = parent.replaceFirst("/", "://")
        return new URL(parent);
    }

    public static boolean overrideNewAlgouse = true;

   File findMavenPath(MavenPath mavenPath) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.mavenLocalDir)
        File file = new File(mavenDefaultSettings.mavenLocalDir, mavenPath.buildMavenPath());
        if (file.exists()) {
            return file;
        }
        return null;
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
        MavenId mavenIdLastest = new MavenId(mavenId.groupId, mavenId.artifactId, maxVersion);
        assert findMavenOrGradle(mavenIdLastest) != null
        return mavenIdLastest
    }

    List<String> findMavenAllVersions(MavenId mavenId) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.mavenLocalDir)
        mavenId = mavenId.normalize()
        String groupId = mavenId.groupId.replace('.', '/')
        //"${groupId}/${artifactId}/${version}/${artifactId}-${version}.jar"
        File file4 = new File(mavenDefaultSettings.mavenLocalDir, "${groupId}/${mavenId.artifactId}");
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

    List<String> findGradleAllVersions(MavenId mavenId) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.gradleLocalDir)
        mavenId = mavenId.normalize()
        File artifactDir = new File(mavenDefaultSettings.gradleLocalDir, "${mavenId.groupId}/${mavenId.artifactId}");
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

    // TODO Source paring not work
    List<String> findGrapeAllVersions(MavenId mavenId) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.grapeLocalDir)
        mavenId = mavenId.normalize()
        String groupId = mavenId.groupId
        // File file = new File(grapeLocalDir, "${mavenId.groupId}/${mavenId.artifactId}/jars/${mavenId.artifactId}-${mavenId.version}.jar")
        Collection<String> subDirNames = fileType== MavenFileType2.source.fileSuffix ? ['sources']:grapeBinaryPackages;
        List<String> result = []
        subDirNames.each {String grapeBinDir->
            File files6 = new File(mavenDefaultSettings.grapeLocalDir, "${groupId}/${mavenId.artifactId}/${grapeBinDir}")
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

    String findGrapeVersion(String artifactId,File file){
        String name = file.name;
        name = name.substring(0, name.length() - 4)
        if(file.parentFile.name=='sources'){
            name =name.replace('-sources','');
        }
        name = name.replace("${artifactId}-", "")
        return name;
    }


    List<MavenId> findAllDependeciesWithDownload(MavenId artifact) throws IOException {
        return []
    }

    List<String> findAllMavenOrGradleVersions(MavenId mavenId) {
        List<String> versions = []
        if (mavenDefaultSettings.mavenLocalDir != null && mavenDefaultSettings.mavenLocalDir.exists()) {
            versions.addAll(findMavenAllVersions(mavenId))
        }
        if (mavenDefaultSettings.gradleLocalDir != null && mavenDefaultSettings.gradleLocalDir.exists()) {
            versions.addAll(findGradleAllVersions(mavenId))
        }
        if (mavenDefaultSettings.grapeLocalDir != null && mavenDefaultSettings.grapeLocalDir.exists()) {
            versions.addAll(findGrapeAllVersions(mavenId))
        }
        return versions.unique()
    }


    File findMavenOrGradle(MavenId mavenId) {
        if (mavenDefaultSettings.mavenLocalDir != null && mavenDefaultSettings.mavenLocalDir.exists()) {
            File file1 = findMavenAtrifact(mavenId)
            if (file1 != null && file1.exists()) {
                return file1
            }
        }
        if (mavenDefaultSettings.gradleLocalDir != null && mavenDefaultSettings.gradleLocalDir.exists()) {
            File file1 = findGradleAtrifact(mavenId)
            if (file1 != null && file1.exists()) {
                return file1
            }
        }
        if (mavenDefaultSettings.grapeLocalDir != null && mavenDefaultSettings.grapeLocalDir.exists()) {
            File file1 = findGrapeAtrifact(mavenId)
            if (file1 != null && file1.exists()) {
                return file1
            }
        }
        return null
        //throw new FileNotFoundException(buildMavenPath(groupId, artifactId, version));
    }

    File findGradleAtrifact(MavenId mavenId) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.gradleLocalDir)
        mavenId = mavenId.normalize()
        File[] files = new File(mavenDefaultSettings.gradleLocalDir, "${mavenId.groupId}/${mavenId.artifactId}/${mavenId.version}").listFiles()
        String suffixx = "${mavenId.artifactId}-${mavenId.version}${fileType}.jar"

        for (File file1 : files) {
            File jarFile = new File(file1, suffixx)
            if (jarFile.exists()) {
                return jarFile
            }
        }
        return null
    }

    public static List<String> grapeBinaryPackages = ['jars', 'bundles', 'eclipse-plugins']


    File findGrapeAtrifact(MavenId mavenId) {
        JrrUtilities3.checkFileExist(mavenDefaultSettings.grapeLocalDir)
        mavenId = mavenId.normalize()
        File file;
        if(fileType == MavenFileType2.source.fileSuffix){
            file = new File(mavenDefaultSettings.grapeLocalDir, "${mavenId.groupId}/${mavenId.artifactId}/sources/${mavenId.artifactId}-${mavenId.version}-sources.jar")
        }else {
            File f =new File(mavenDefaultSettings.grapeLocalDir, "${mavenId.groupId}/${mavenId.artifactId}")
            grapeBinaryPackages.find {
                file = new File(f,"${it}/${mavenId.artifactId}-${mavenId.version}.jar")
                return file.exists()
            }
        }
        return file
    }


    File findMavenAtrifact(MavenId mavenId) {
        String suffix = buildMavenPath(mavenId)
        JrrUtilities3.checkFileExist(mavenDefaultSettings.mavenLocalDir)
        File file = new File(mavenDefaultSettings.mavenLocalDir, suffix)
        return file
    }

    String buildMavenPath(MavenId mavenId) {
        mavenId = mavenId.normalize();
        String groupId = mavenId.groupId.replace('.', '/')
        String artifactId = mavenId.artifactId
        String version = mavenId.version
        return "${groupId}/${artifactId}/${version}/${artifactId}-${version}${fileType}.jar"
    }


    MavenId detectMavenIdFromFileName(File file) {
        if (mavenDefaultSettings.mavenLocalDir!=null && mavenDefaultSettings.mavenLocalDir.exists()) {
            if (isParent(mavenDefaultSettings.mavenLocalDir, file)) {
                MavenId mavenId = detectMavenIdFromFileNameInMavenDir(file, true)
                if (mavenId != null) {
                    return mavenId;
                }
//                return new MavenPath2(pathToParent);
            }
        }
        if (mavenDefaultSettings.gradleLocalDir!=null && mavenDefaultSettings.gradleLocalDir.exists()) {
            if (isParent(mavenDefaultSettings.gradleLocalDir, file)) {
                MavenId mavenId = detectMavenIdFromFileNameInGradleDir(file, true)
                if (mavenId != null) {
                    return mavenId;
                }

            }
        }
        if (mavenDefaultSettings.grapeLocalDir!=null && mavenDefaultSettings.grapeLocalDir.exists()) {
            if (isParent(mavenDefaultSettings.grapeLocalDir, file)) {
                MavenId mavenId = detectMavenIdFromFileNameInGrapeDir(file, true)
                if (mavenId != null) {
                    return mavenId;
                }
            }
        }
        return null
    }

    MavenId detectMavenIdFromFileNameInGradleDir(File file, boolean logMismatch) {
        File version = file.parentFile.parentFile;
        File artifact = version.parentFile
        String group = getPathToParent(mavenDefaultSettings.gradleLocalDir, artifact.parentFile);
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

    // log4j uses bundle in pom.xml
//    static Collection<String> grapeBinaryDirs = new HashSet<String>( ['jars','bundles'])

    MavenId detectMavenIdFromFileNameInGrapeDir(File file, boolean logMismatch) {
        Collection<String> typeFill = fileType == MavenFileType2.binary.fileSuffix?grapeBinaryPackages:[fileType.replace('-','')];

        if(!typeFill.contains(file.parentFile.name)){
            GString msg = "bad name for parent dir ${file.absolutePath}, expected ${typeFill}"
            if(logMismatch) {
                log.info(msg)
            }else{
                log.fine(msg)
            }
            return null
        }
        File artifact = file.parentFile.parentFile
        String group = artifact.parentFile.name;
        String version = findGrapeVersion(artifact.name,file);
        return new MavenId(group, artifact.name, version)
    }

    MavenId detectMavenIdFromFileNameInMavenDir(File file, boolean logMismatch) {
        File version = file.parentFile;
        File artifact = version.parentFile
        String pathToParent = getPathToParent(mavenDefaultSettings.mavenLocalDir, artifact.parentFile);
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


    String getPathToParent(File parent, File child) {
        String parentPath = parent.absolutePath.replace('\\', '/')
        String childPath = child.absolutePath.replace('\\', '/')
        assert childPath.length() >= parentPath.length()
        String res = childPath.substring(parentPath.length())
        if (res.startsWith('/')) {
            return res.substring(1)
        }
        return res;
    }


    boolean isParent(File parent, File child) {
        String parentPath = parent.absolutePath.replace('\\', '/').toLowerCase()
        String childPath = child.absolutePath.replace('\\', '/').toLowerCase()
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
                toolsJarFile2 = toolsJarFile2.canonicalFile.absoluteFile
            }
            toolsJarFile = toolsJarFile2
        }
        return toolsJarFile
    }

}
