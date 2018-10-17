package net.sf.jremoterun.utilities.classpath

import groovy.io.FileType
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.UrlCLassLoaderUtils
import net.sf.jremoterun.utilities.UrlToFileConverter

import java.lang.reflect.Array
import java.util.logging.Logger

@CompileStatic
public abstract class AddFilesToClassLoaderCommon {

    public static final Logger logStatic = Logger.getLogger(JrrClassUtils.getCurrentClass().getName());

    public Logger logAdder = logStatic;

    public boolean isLogFileAlreadyAdded = true
    public boolean checkJarFileIsZipArchive = true

    public volatile MavenCommonUtils mavenCommonUtils = new MavenCommonUtils()

    // used ?
    NewValueListener<File> addFileListener

    HashSet<File> addedFiles2 = []
    HashSet<MavenId> addedMavenIds = []
    NewValueListener<Throwable> onExceptionAction;
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


    // use add method
    @Deprecated
    void addBinaryWithSource(BinaryWithSourceI fileWithSource) throws Exception {
        addF(fileWithSource.resolveToFile());
    }

    // use add method
    @Deprecated
    void addFile(File file) throws Exception {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
        file = file.getCanonicalFile().getAbsoluteFile();
        if (addedFiles2.contains(file)) {
            if (isLogFileAlreadyAdded) {
                logAdder.info "file already added ${file}"
//                Thread.dumpStack()
            }
        } else {
            boolean addFile12334 = true
            if (checkJarFileIsZipArchive) {
                String fileName = file.getName()
                if (fileName.endsWith('.zip') || fileName.endsWith('.jar')) {
                    if(!file.isFile()){
                        addFile12334 = false
                        onException new Exception("Not a file : ${file}")
                    }else {
                        boolean isZip = true
                        try {
                            isZip = JrrZipUtils.isZipArchive(file)
                        }catch (Exception e){
                            addFile12334 = false
                            onException(e)
                        }
                        if (!isZip) {
                            addFile12334 = false
                            onException new Exception("Not zip archive : ${file}")
                        }
                    }
                }
                if(file.isDirectory()){

                }
            }
            if(addFile12334) {
                addFileImpl(file);
                addedFiles2.add(file)
            }
        }
    }

    // use add method
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
            onException new FileNotFoundException("${artifact}")
        }else {
            addM(lastestInRepo)
        }
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
            onException new FileNotFoundException("${artifact} and mavenDependenciesResolver is null and jrrDownloadDir is null")
        }
    }

    void addMMany(List<MavenId> mavenIds) {
        addAll(mavenIds)
    }

    void addMWithDepsMany(List<MavenId> mavenIds) {
        mavenIds.each {
            addMWithDependeciesDownload(it)
        }
    }

    void addFMany(List<File> mavenIds) {
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
        if (mavenId.version == MavenCommonUtils.lastVersionInd) {
            MavenId mavenId2 = mavenCommonUtils.findLatestMavenOrGradleVersionEx(mavenId);
            return mavenCommonUtils.findMavenOrGradle(mavenId2)
        }
        File file = mavenCommonUtils.findMavenOrGradle(mavenId)
        if (file != null) {
            return file
        }
        if (mavenCommonUtils.downloadDir == null) {
            onException new DownloadDirNotSetException()
            throw  new DownloadDirNotSetException()
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

    // use add method
    @Deprecated
    void addF(File file) {
        addFile(file)
    }


    void addMWithDependeciesDownload(MavenIdContains artifact) throws IOException {
        addMWithDependeciesDownload(artifact.getM())
    }

    void addMWithDependeciesDownload(MavenId artifact) throws IOException {
        MavenIdAndRepo mavenIdAndRepo = new MavenIdAndRepo(artifact, null)
        addMWithDependeciesDownload(mavenIdAndRepo)
    }

    void addMWithDependeciesDownload(MavenIdAndRepoContains artifact) throws IOException {
        addMWithDependeciesDownload (artifact.mavenIdAndRepo)
    }

    boolean isNeedDownloadMavenSources(){
        return mavenCommonUtils.fileType == MavenFileType2.source.fileSuffix || this instanceof AddFilesWithSourcesI
    }

    void addMWithDependeciesDownload(MavenIdAndRepo artifact) throws IOException {
        try {
            if (mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver == null) {
                onException new NullPointerException("mavenDependenciesResolver is null : add dropship, artifact = ${artifact}")
            }
            List<MavenId> dependencies = mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver.resolveAndDownloadDeepDependencies(artifact.m, isNeedDownloadMavenSources(), true, artifact.repo)
            if (dependencies.size() == 0) {
                onException new Exception("Failed resolve : ${artifact}")
            }
            dependencies = dependencies.findAll { mavenCommonUtils.findMavenOrGradle(it) != null }
            if (dependencies.size() == 0) {
                onException new Exception("Failed resolve : ${artifact}")
            }
            addGenericEnteries(dependencies)
//        addMavenExisted(dependencies)
        }catch(Throwable e){
            onException e
        }
    }

    File resolveMavenId(MavenIdAndRepo artifact) {
        File file
        if (artifact.m.version == MavenCommonUtils.lastVersionInd) {
            MavenId version = mavenCommonUtils.findLatestMavenOrGradleVersion(artifact.m)
            if (version != null) {
                file = mavenCommonUtils.findMavenOrGradle(version)
            }
        } else {
            file = mavenCommonUtils.findMavenOrGradle(artifact.m);
        }
        if (file != null) {
            return file
        }
        if (mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver == null) {
            file = onMissingMavenId(artifact.m)
            assert file != null
            return file
        } else {
            List<MavenId> dependencies3 = mavenCommonUtils.mavenDefaultSettings.mavenDependenciesResolver.resolveAndDownloadDeepDependencies(artifact.m,isNeedDownloadMavenSources(), false, artifact.repo)
//            logAdder.info "found dep = ${dependencies3}"
            MavenId mavenId4 = artifact.m
            file = mavenCommonUtils.findMavenOrGradle(artifact.m)
            if (file == null) {
                if (dependencies3.size() == 0) {
                    onException new Exception("failed find dep for ${artifact}")
                }
                MavenId mavenId3 = dependencies3.find {
                    it.artifactId == artifact.m.artifactId && it.groupId == artifact.m.groupId && it.version == artifact.m.version
                }
                if (mavenId3 == null) {
                    onException new Exception("failed find dep for ${artifact}, got from resolver : ${dependencies3}")
                }
                mavenId4 = mavenId3
                file = mavenCommonUtils.findMavenOrGradle(mavenId3)
//                logAdder.info "found file = ${file}"
            }
            if (file == null) {
                file = onMissingMavenId(mavenId4)
                assert file != null
                return file
            } else {
                return file
            }

        }
    }

    File resolveMavenId(MavenId artifact) {
        MavenIdAndRepo mavenIdAndRepo = new MavenIdAndRepo(artifact, null);
        return resolveMavenId(mavenIdAndRepo)
    }

    void addM(MavenIdContains artifact) throws IOException {
        addM(artifact.getM());
    }

    void addM(MavenIdAndRepoContains artifact) throws IOException {
        File file = resolveMavenId(artifact.getMavenIdAndRepo())
        addF(file);

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
            onException new FileNotFoundException("Failed resolve ${artifact},   dependenciesResolver is null")
        }
        onException new Exception("Failed resolve : ${artifact}")
    }

    void addUrl(URL url) {
        assert url != null
        String asS = url.toString()
        File file1;
        if (url.protocol == 'file') {
            file1 = UrlToFileConverter.c.convert url
            if (file1 == null) {
                onException new IllegalArgumentException(asS)
            }
        } else {
            file1 = mavenCommonUtils.downloadJarFromUrl(url)
        }
        addFile(file1)
    }

    Collection<File> addAllJarsInDir(File dir) {
        Collection<File> files5 = addAllJarsInDirImpl(dir)
        if (files5.size() == 0) {
            onException new FileNotFoundException("not jars in dir ${dir}")
        }
        return files5
    }

    Collection<File> addAllJarsInDirImpl(File dir) {
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(dir);
        assert dir.isDirectory()
        Collection<File> files = dir.listFiles().findAll { it.isFile() }
        files = files.findAll { it.name.endsWith('.jar') }
        files.each {
            try {
                addFile(it);
            } catch (Exception e) {
                logAdder.severe("failed add file : ${it}")
                onException e
            }
        }
        return files
    }

    List<File> getJarsInDirAndSubDirs(File dir){
        Map<String, Object> params = [
                type      : FileType.FILES,
                nameFilter: ~/.*\.jar/,
        ] as Map;
        List<File> addedFiles5 = []
        dir.traverse(params, { File f ->
            addFile(f);
            addedFiles5.add(f)
        });
        return addedFiles5
    }

    Collection<File> addAllJarsInDirAndSubdirsDeep(File dir) {
        List<File> addedFiles5 = getJarsInDirAndSubDirs(dir)
        if (addedFiles5.size() == 0) {
            onException new FileNotFoundException("not jars in dir and subdir ${dir}")
        }
        return addedFiles5
    }

    Collection<File> addAllJarsInDirAndSubdirs(File dir) {
        if(!dir.exists()){
            onException new FileNotFoundException(dir.toString())
        }
        Collection<File> addedFiles5 = []
        addedFiles5.addAll addAllJarsInDirImpl(dir)
        File[] childs = dir.listFiles()
        if(childs==null){
            onException new Exception("Failed list files ${dir}")
        }
        childs.findAll { it.directory }.each {
            addedFiles5.addAll addAllJarsInDirImpl(it);
        }
        if (addedFiles5.size() == 0) {
            onException new FileNotFoundException("not jars in dir and subdir ${dir}")
        }
        return addedFiles5
    }

    void addGenericEnteries(Collection collection1) {
        if(collection1 == null){
            throw new NullPointerException("object is null")
        }
        if (collection1.isEmpty()) {
            onException new IllegalArgumentException("Empty collection")
        }
        int count3 =-1
        for (Object entry : collection1) {
            count3++
            try {
                addGenericEntery(entry)
            } catch (Throwable e) {
                String entryS;
                try {
                    entryS = "${entry}"
                }catch(Throwable e2){
                    entryS = entry.getClass().getName()
                    logAdder.log(java.util.logging.Level.WARNING,"failed on ${entryS}",e2)
                }
                logAdder.info "failed add ${count3} ${entryS} ${e}"
                onException e
            }
        }
    }

    void add(Object object) {
        addGenericEntery(object)
    }

    void addClassPathFromURLClassLoader(URLClassLoader urlClassLoader){
        List<File> files3 = UrlCLassLoaderUtils.getFilesFromUrlClassloader2(urlClassLoader)
        if(files3.size()==0){
            onException new Exception("no files in ${urlClassLoader}")
        }
        files3.each {
            if(it.exists()){
                addF(it)
            }else{
                logAdder.info "file not found : ${it}"
            }
        }
    }

    void addAllWithDeps(Collection<? extends MavenIdContains> mavenIds) {
        mavenIds.each { addMWithDependeciesDownload(it) }
    }

    void addArray(Object[] objects) {
        addAll(objects.toList())
    }

    void addAll(Collection objects) {
        addGenericEnteries(objects)
    }

    void addGenericEntery(Object object) {
        try {
            switch (object) {
                case { object == null }:
                    throw new NullPointerException("object is null")
                case { object instanceof Collection }:
                    throw new IllegalArgumentException("Collection : ${object}")
                case { object instanceof MavenId }:
                    MavenId mavenId1 = (MavenId) object;
                    addM(mavenId1);
                    break;
                case { object instanceof MavenIdAndRepoContains }:
                    MavenIdAndRepoContains mavenId1 = (MavenIdAndRepoContains) object;
                    addM(mavenId1);
                    break;
                case { object instanceof File }:
                    File file = object as File
                    if (file.isFile() && file.getName().endsWith('.groovy')) {
                        throw new Exception("File ends with groovy ${file}")
                    }
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
                case{ object.getClass().isArray()}:
                    throw new IllegalArgumentException("Object is array ${Array.getClass()}")
                    break
                default:
                    CustomObjectHandler customObjectHandler = MavenDefaultSettings.mavenDefaultSettings.customObjectHandler
                    if (customObjectHandler == null) {
                        String string1
                        try {
                            string1 = object.toString()
                        }catch (Throwable e){
                            String name1 = object.getClass().getName()
                            logAdder.log(java.util.logging.Level.WARNING,"failed to string ${name1}",e)
                            throw new IllegalArgumentException("${name1}");
                        }
                        throw new IllegalArgumentException("${string1}");
                    } else {
                        customObjectHandler.add(this, object)
                    }
            }
        }catch(Throwable e){
            onException(e);
        }
    }

    void onException(Throwable e){
        if(onExceptionAction==null){
            throw e;
        }else{
            onExceptionAction.newValue(e)
        }

    }


    void addFileWhereClassLocated(Class aClass) {
        File location = UrlCLassLoaderUtils.getClassLocation(aClass)
//        logAdder.info "detected location : ${location} , for class ${aClass.name}"
        if (location == null) {
            onException new Exception("Failed detect location for class ${aClass}")
        }
        if (location.path.length() < 2) {
            onException new Exception("Strange location ${location.path} for class ${aClass}")
        }
        if (!location.exists()) {
            onException new Exception("Location ${location.path} not exists for class ${aClass}")
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
