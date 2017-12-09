package net.sf.jremoterun.utilities.classpath

import groovy.io.FileType
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.UrlCLassLoaderUtils
import net.sf.jremoterun.utilities.UrlToFileConverter
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import java.util.logging.Logger

@CompileStatic
public abstract class AddFilesToClassLoaderCommon {

    public static final Logger logStatic = Logger.getLogger(JrrClassUtils.currentClass.name);

    public Logger logAdder = logStatic;

    public boolean isLogFileAlreadyAdded = true

    public volatile MavenCommonUtils mavenCommonUtils = new MavenCommonUtils()
    NewValueListener<File> addFileListener

    HashSet<File> addedFiles2 = []
    HashSet<MavenId> addedMavenIds = []
//    boolean downloadSources = false;


    abstract void addFileImpl(File file) throws Exception;


    @Deprecated
    File getDownloadDir() {
        return mavenCommonUtils.downloadDir
    }

    @Deprecated
    void setDownloadDir(File downloadDir) {
        mavenCommonUtils.downloadDir = downloadDir
    }


    void addBinaryWithSource(BinaryWithSourceI fileWithSource) throws Exception {
        addF(fileWithSource.resolveToFile());
    }

    void addFile(File file) throws Exception {
        JrrUtilities3.checkFileExist(file)
        file = file.canonicalFile.absoluteFile;
        if (addedFiles2.contains(file)) {
            if (isLogFileAlreadyAdded) {
                logAdder.info "file already added ${file}"
//                Thread.dumpStack()
            }
        } else {
            addFileImpl(file);
            addedFiles2.add(file)
        }
    }

    void addMavenPath(MavenPath mavenPath) {
        mavenPath = mavenPath.normalize()
        File mavenPathF = mavenCommonUtils.findMavenPath(mavenPath)
        if (mavenPathF == null) {
            if (mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver == null) {
                URL url = new URL(mavenCommonUtils.mavenDefaultSettings.mavenServer + '/' + mavenPath.buildMavenPath());
                mavenPathF = mavenCommonUtils.downloadJarFromUrl(url)
            } else {
                mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver.downloadMavenPath(mavenPath, false);
                mavenPathF = mavenCommonUtils.findMavenPath(mavenPath)
            }
        }
        addFile(mavenPathF)
    }


    void addMavenLatestVersionOrDownloadIfHigher(MavenId artifact) throws IOException {
        MavenId lastestInRepo = mavenCommonUtils.findLatestMavenOrGradleVersion2(artifact);
        addM(lastestInRepo)
    }

    void addMavenLatestDownloadedVersion(MavenId artifact) throws IOException {
        MavenId lastestInRepo = mavenCommonUtils.findLatestMavenOrGradleVersion(artifact);
        if (lastestInRepo == null) {
            throw new FileNotFoundException("${artifact}")
        }
        addM(lastestInRepo)
//        MavenId lastestInRepo = mavenCommonUtils.findLatestMavenOrGradleVersion(artifact);

    }

    void addMavenAnyOrDownloadedVersion(MavenId artifact) throws IOException {
        boolean found = false
        if (!found) {
            File file = mavenCommonUtils.findMavenOrGradle(artifact);
            if (file != null && file.exists()) {
                addM(artifact)
                found = true;
            }
        }
        if (!found && mavenCommonUtils.mavenDefaultSettings.jrrDownloadDir != null) {
            URL url = buildMavenArtifactUrl(artifact);
            assert url != null
            File file = mavenCommonUtils.buildDownloadUrl(url)
            if (file.exists()) {
                addFile(file)
                found = true
            }
        }
        if (!found) {
            MavenId lastestInRepo = mavenCommonUtils.findLatestMavenOrGradleVersion(artifact);
            if (lastestInRepo != null) {
                addM(lastestInRepo)
                found = true
            }
        }
        if (!found && mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver != null) {
            addM(artifact)
            found = true
        }
        if (!found && mavenCommonUtils.mavenDefaultSettings.jrrDownloadDir != null) {
            URL url = buildMavenArtifactUrl(artifact);
            assert url != null
            File file = mavenCommonUtils.downloadJarFromUrl(url)
            addFile(file)
            found = true
        }
        if (found) {
            addedMavenIds.add(artifact)
        } else {
            throw new FileNotFoundException("${artifact} and mavenDependenciesResolver is null and jrrDownloadDir is null")
        }
    }

    void addMMany(List<MavenId> mavenIds){
        addAll(mavenIds)
    }

    void addMWithDepsMany(List<MavenId> mavenIds){
        mavenIds.each {
            addMWithDependeciesDownload(it)
        }
    }

    void addFMany(List<File> mavenIds){
        addAll(mavenIds)
    }

    /**
     *  What diff from addM : it download via URL.bytes jar
     */
    void addU(MavenIdContains artifact) throws IOException {
        addU(artifact.getM())
    }

    void addU(MavenId artifact) throws IOException {
        File file = findMavenOrGradleOrUrl(artifact)
        addFile(file)
        addedMavenIds.add(artifact)
    }

    /**
     *
     * throw exception if not found
     */
    File findMavenOrGradleOrUrl(MavenId mavenId) {
        if (mavenId.version == mavenCommonUtils.lastVersionInd) {
            MavenId mavenId2 = mavenCommonUtils.findLatestMavenOrGradleVersionEx(mavenId);
            return mavenCommonUtils.findMavenOrGradle(mavenId2)
        }
        File file = mavenCommonUtils.findMavenOrGradle(mavenId)
        if (file != null) {
            return file
        }
        if (mavenCommonUtils.downloadDir == null) {
            throw new DownloadDirNotSetException()
        }
        URL url = buildMavenArtifactUrl(mavenId);
        file = mavenCommonUtils.downloadJarFromUrl(url)
        return file
    }

    URL buildMavenArtifactUrl(MavenId mavenId) {
        String suffix = mavenCommonUtils.buildMavenPath(mavenId)
        URL url = new URL(mavenCommonUtils.mavenDefaultSettings.mavenServer + '/' + suffix);
        return url;
    }

    void addF(File file) {
        addFile(file)
    }


    void addMWithDependeciesDownload(MavenIdContains artifact) throws IOException {
        addMWithDependeciesDownload(artifact.getM())
    }

    void addMWithDependeciesDownload(MavenId artifact) throws IOException {
        if (mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver == null) {
            throw new NullPointerException("mavenDependenciesResolver is null : add dropship, artifact = ${artifact}")
        }
        List<MavenId> dependencies = mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver.resolveAndDownloadDeepDependencies(artifact, mavenCommonUtils.fileType == MavenFileType2.source.fileSuffix, true)
        if (dependencies.size() == 0) {
            throw new Exception("Failed resolve : ${artifact}")
        }
        dependencies = dependencies.findAll { mavenCommonUtils.findMavenOrGradle(it) != null }
        if (dependencies.size() == 0) {
            throw new Exception("Failed resolve : ${artifact}")
        }
        addGenericEnteries(dependencies)
//        addMavenExisted(dependencies)
    }

    File resolveMavenId(MavenId artifact) {
        File file
        if (artifact.version == mavenCommonUtils.lastVersionInd) {
            MavenId version = mavenCommonUtils.findLatestMavenOrGradleVersion(artifact)
            if (version != null) {
                file = mavenCommonUtils.findMavenOrGradle(version)
            }
        } else {
            file = mavenCommonUtils.findMavenOrGradle(artifact);
        }
        if (file != null) {
            return file
        }
        if (mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver == null) {
            file = onMissingMavenId(artifact)
            assert file != null
            return file
        } else {
            List<MavenId> dependencies3 = mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver.resolveAndDownloadDeepDependencies(artifact, false, false)
//            logAdder.info "found dep = ${dependencies3}"
            file = mavenCommonUtils.findMavenOrGradle(artifact)
            if (file == null) {
                if (dependencies3.size() == 0) {
                    throw new Exception("failed find dep for ${artifact}")
                }
                MavenId mavenId3 = dependencies3.find {
                    it.artifactId == artifact.artifactId && it.groupId == artifact.groupId && it.version == artifact.version
                }
                if (mavenId3 == null) {
                    throw new Exception("failed find dep for ${artifact}, got from resolver : ${dependencies3}")
                }
                artifact = mavenId3
                file = mavenCommonUtils.findMavenOrGradle(artifact)
//                logAdder.info "found file = ${file}"
            }
            if (file == null) {
                file = onMissingMavenId(artifact)
                assert file != null
                return file
            } else {
                return file
            }

        }
    }

    void addM(MavenIdContains artifact) throws IOException {
        addM(artifact.getM());
    }

    void addM(MavenId artifact) throws IOException {
        File file = resolveMavenId(artifact)
        addF(file);
    }

    File onMissingMavenId(MavenId artifact) {
        if (mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver == null) {
            if (mavenCommonUtils.mavenDefaultSettings.jrrDownloadDir != null) {
                URL url = buildMavenArtifactUrl(artifact);
                assert url != null
                File file = mavenCommonUtils.buildDownloadUrl(url)
                if (file.exists()) {
                    return file;
                }
            }
            throw new FileNotFoundException("Failed resolve ${artifact},   dependenciesResolver is null")
        }
        throw new Exception("Failed resolve : ${artifact}")
    }

    void addUrl(URL url) {
        assert url != null
        String asS = url.toString()
        File file1;
        if (url.protocol == 'file') {
            file1 = UrlToFileConverter.c.convert url
            if (file1 == null) {
                throw new IllegalArgumentException(asS)
            }
        } else {
            file1 = mavenCommonUtils.downloadJarFromUrl(url)
        }
        addFile(file1)
    }

    Collection<File> addAllJarsInDir(File dir) {
        Collection<File> files5 = addAllJarsInDirImpl(dir)
        if (files5.size() == 0) {
            throw new FileNotFoundException("not jars in dir ${dir}")
        }
        return files5
    }

    Collection<File> addAllJarsInDirImpl(File dir) {
        JrrUtilities3.checkFileExist(dir);
        assert dir.directory
        Collection<File> files = dir.listFiles().findAll { it.isFile() }
        files = files.findAll { it.name.endsWith('.jar') }
        files.each {
            try {
                addFile(it);
            } catch (Exception e) {
                logAdder.severe("failed add file : ${it}")
                throw e
            }
        }
        return files
    }

    Collection<File> addAllJarsInDirAndSubdirsDeep(File dir) {
        Map<String, Object> params = [
                type      : FileType.FILES,
                nameFilter: ~/.*\.jar/,
        ] as Map;
        List<File> addedFiles5 = []
        dir.traverse(params, { File f ->
            addFile(f);
            addedFiles5.add(f)
        });
        if (addedFiles5.size() == 0) {
            throw new FileNotFoundException("not jars in dir and subdir ${dir}")
        }
        return addedFiles5
    }

    Collection<File> addAllJarsInDirAndSubdirs(File dir) {
        Collection<File> addedFiles5 = []
        addedFiles5.addAll addAllJarsInDirImpl(dir)
        dir.listFiles().findAll { it.directory }.each {
            addedFiles5.addAll addAllJarsInDirImpl(it);
        }
        if (addedFiles5.size() == 0) {
            throw new FileNotFoundException("not jars in dir and subdir ${dir}")
        }
        return addedFiles5
    }

    void addGenericEnteries(Collection collection) {
        if(collection.isEmpty()){
            throw new IllegalArgumentException("Empty collection")
        }
        for (Object entry : collection) {
            try {
                addGenericEntery(entry)
            } catch (Throwable e) {
                logAdder.info "failed add ${entry} ${e}"
                throw e
            }
        }
    }

    void add(Object object) {
        addGenericEntery(object)
    }


    void addAllWithDeps(Collection<? extends MavenIdContains> mavenIds) {
        mavenIds.each { addMWithDependeciesDownload(it) }
    }

    void addAll(Collection objects) {
        addGenericEnteries(objects)
    }

    void addGenericEntery(Object object) {
        switch (object) {
            case { object == null }:
                throw new NullPointerException("object is null")
            case { object instanceof Collection }:
                throw new IllegalArgumentException("Collection : ${object}")
            case { object instanceof MavenId }:
                MavenId mavenId1 = (MavenId) object;
                addM(mavenId1);
                break;
            case { object instanceof File }:
                File file = object as File
                addF(file)
                break;
            case { object instanceof MavenPath }:
                MavenPath file = object as MavenPath
                addMavenPath(file)
                break;
            case { object instanceof URL }:
                URL file = object as URL
                addUrl(file)
                break;
            case { object instanceof BinaryWithSourceI }:
                BinaryWithSourceI file = object as BinaryWithSourceI
                addBinaryWithSource(file)
                break;
            case { object instanceof MavenIdContains }:
                MavenIdContains mavenId3 = object as MavenIdContains
                addGenericEntery mavenId3.getM()
                break;
            case { object instanceof ToFileRefSelf }:
                ToFileRefSelf toFileRefSelf = object as ToFileRefSelf
                addF toFileRefSelf.resolveToFile()
                break;
            default:
                CustomObjectHandler customObjectHandler = MavenDefaultSettings.mavenDefaultSettings.customObjectHandler
                if (customObjectHandler == null) {
                    throw new IllegalArgumentException("${object}");
                } else {
                    customObjectHandler.add(this, object)
                }
        }
    }


    void addFileWhereClassLocated(Class aClass) {
        File location = UrlCLassLoaderUtils.getClassLocation(aClass)
//        logAdder.info "detected location : ${location} , for class ${aClass.name}"
        if (location == null) {
            throw new Exception("Failed detect location for class ${aClass}")
        }
        if (location.path.length() < 2) {
            throw new Exception("Strange location ${location.path} for class ${aClass}")
        }
        if (!location.exists()) {
            throw new Exception("Location ${location.path} not exists for class ${aClass}")
        }

        addF(location)
    }


    @Deprecated
    void copySettingsToAnotherClass(AddFilesToClassLoaderCommon other) {
        other.downloadDir = downloadDir
        other.mavenCommonUtils = mavenCommonUtils
        Thread.dumpStack()
    }



}
