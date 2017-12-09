package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.FileInputStream2
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3
import net.sf.jremoterun.utilities.UrlCLassLoaderUtils

import java.util.logging.Level
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@CompileStatic
class ClassPathCalculatorAbstract {
    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

    private File userHome = new File(System.getProperty('user.home'));
    public MavenCommonUtils mavenCommonUtils = new MavenCommonUtils()
    List filesAndMavenIds = [];

    static boolean isWindows() {
        String osname = System.getProperty('os.name');
        if (osname == null) {
            log.warning "os name is null";
            return false;
        }
        osname = osname.toLowerCase()
        return osname.startsWith('windows')
    }

    boolean convertPathToLowerCase = isWindows()


    void calcClassPathFromFiles12() throws Exception {
//        filesAndMavenIds = files3
        makeUnique2();
        addCustomObjects()
        makeUnique2();
        filesAndMavenIds = filesAndMavenIds.collect { filterOnAll1(it) }
        makeUnique2();
        saveFileWithSource()
        saveHighestMavenIdFirst()
        makeUnique2();
        onPhase1Finished()

        // filterOnAll2 useless
        filesAndMavenIds = filesAndMavenIds.collect { filterOnAll2(it) }
        makeUnique2();

        filesAndMavenIds = filesAndMavenIds.collect { filterOnAll3NormalizeMavenIds(it) }
        makeUnique2();

        filesAndMavenIds = filesAndMavenIds.collect { filterOnAll4(it) }
        makeUnique2();
        saveFileWithSource()
        saveHighestMavenIdSecond()
        makeUnique2();
        onPhase2Finished()

        filesAndMavenIds = filesAndMavenIds.collect { filterOnAll5(it) }
        makeUnique2();

        onClassPathCalculated()
    }

    void onPhase1Finished() {}

    void onPhase2Finished() {}

    void onClassPathCalculated() throws Exception {

    }

    void addCustomObjects() throws Exception {
    }

    Object filterOnAll1(Object object) throws Exception {
        if (object instanceof File) {
            File file1 = (File) object;
            return convertFileToObject(file1)
        }
        if (object instanceof URL) {
            URL url = object as URL;
            return filterOnUrl1(url)
        }
        return object
    }

    Object filterOnAll2(Object object) throws Exception {
        return object;
    }

    Object filterOnAll5(Object object) throws Exception {
        return object;
    }

    Object filterOnAll4(Object object) throws Exception {
        if (object instanceof MavenId) {
            MavenId mavenId = object as MavenId
            return filterOnMavenId(mavenId, mavenId.groupId, mavenId.artifactId)
        }
        return object;
    }

    Object filterOnAll3NormalizeMavenIds(Object object) throws Exception {
        return object;
    }

    Object filterOnUrl1(URL url) {
        String string = url.toString();
        int i = string.lastIndexOf('/');
        assert i > 0
        String fileName;
        if (string.endsWith("/")) {
            fileName = null
        } else {
            fileName = string.substring(i);
            assert string.contains("/${fileName}")
        }
        return filterOnUrl2(url, string, fileName)
    }

    Object filterOnUrl2(URL url, String asString, String fileName) {
        mavenCommonUtils.downloadJarFromUrl(url);
        return url
    }

    Object filterOnMavenId(MavenId mavenId, String group, String artifact) {
        return mavenId
    }

    Object filterOnFile(File file, String fullpath, String name) {
        return file
    }


    void addClassPathFromURLClassLoader(URLClassLoader urlClassLoader) {
        List<File> collect = UrlCLassLoaderUtils.getFilesFromUrlClassloader(urlClassLoader)
        collect.each {
            if (it.exists()) {
                filesAndMavenIds.add(it)
            } else {
                log.warning("file not exist : ${it}")
            }
        }
    }


    void makeUnique2() throws Exception {
        filesAndMavenIds = filesAndMavenIds.findAll { it != null }.unique()
        filesAndMavenIds = filesAndMavenIds.collect { makeUnique2Each(it) }
    }

    Object makeUnique2Each(Object obj) {
        if (obj instanceof Collection) {
            throw new IllegalArgumentException("Collection : ${obj}")
        }
        if (obj instanceof File) {
            File f=(File) obj
            return f.canonicalFile.absoluteFile
        }
        if (obj instanceof BinaryWithSource) {
            BinaryWithSource bs = (BinaryWithSource) obj;
            File source = bs.source
            if (source == null) {
                log.info "empty source for ${bs.binary}, convert to file"
                return bs.binary
            }
            if (source.exists()) {
                return new BinaryWithSource(bs.getBinary().getCanonicalFile().getAbsoluteFile(), bs.getSource().getCanonicalFile().getAbsoluteFile())
            } else {
                throw new Exception("source ${bs.source} not exists for ${bs.binary}")
            }
        }
        if (obj instanceof MavenId) {
            return obj;
        }
        if (obj instanceof MavenIdContains) {
            MavenIdContains m = (MavenIdContains) obj;
            return m.getM();
        }
        return obj
    }


    void calcAndAddClassesToAdded(AddFilesToClassLoaderCommon adder) {
        calcClassPathFromFiles12()
        if (filesAndMavenIds.size() == 0) {
            log.warning "no files to add"
        } else {
            adder.addGenericEnteries(filesAndMavenIds)
        }
    }

    @Deprecated
    List<String> makeUnique(List<String> files) {
        return files.findAll { it != null }.unique()
    }

    void addClassPathFromJmx() throws Exception {
        List<File> pathFromJmx = UrlCLassLoaderUtils.calculateClassPathFromJmx();
        pathFromJmx.each {
            if (it.exists()) {
                filesAndMavenIds.add(it)
            } else {
                log.warning("file not exist : ${it}")
            }
        }
    }


    void saveHighestMavenIdFirst() throws Exception {
        saveHighestMavenId()
    }


    void saveHighestMavenIdSecond() throws Exception {
        saveHighestMavenId()
    }

    // use saveHighestMavenIdFirst/saveHighestMavenIdSecond
    @Deprecated
    void saveHighestMavenId() throws Exception {
        makeUnique2()
        Map<String, String> savedMavenIds = [:]
        Map<String, Integer> savedPositions = [:]
        List res = []
        filesAndMavenIds.each {
            if (it instanceof MavenId) {
                MavenId mavenId = (MavenId) it;
                if (mavenId.version == mavenCommonUtils.lastVersionInd) {
                    mavenId = mavenCommonUtils.findLatestMavenOrGradleVersionEx(mavenId)
                }
                String prefixNoversion = mavenId.groupId + ':' + mavenId.artifactId
                String savedVersion = savedMavenIds.get(prefixNoversion)
                if (savedVersion == null) {
                    int savedPosition = res.size()
                    savedPositions.put(prefixNoversion, savedPosition)
                    savedMavenIds.put(prefixNoversion, mavenId.version)
                    res.add(mavenId)
                } else if (MavenDefaultSettings.mavenDefaultSettings.mavenVersionComparator.isOverrideMavenId2(mavenId, savedVersion, mavenId.version)) {
                    int savedPosition = savedPositions.get(prefixNoversion)
                    savedMavenIds.put(prefixNoversion, mavenId.version)
                    res.set(savedPosition, mavenId)
                    log.info "overriding ${prefixNoversion} from ${savedVersion} to ${mavenId.version} , position ${savedPosition}"
                } else {
                    int savedPosition = savedPositions.get(prefixNoversion)
                    log.info "skip ${prefixNoversion}  ${mavenId.version} as has newer ${savedVersion} , position ${savedPosition}"
                }
            } else {
                res.add(it)
            }
        }
        filesAndMavenIds = res;
    }

    void saveFileWithSource() throws Exception {
        makeUnique2()
        Map<File, Integer> savedPositions = [:]
        List res = []
        filesAndMavenIds.each {
            File fileInConsidaration = null
            if (it instanceof File) {
                fileInConsidaration = (File) it;
            } else {
                fileInConsidaration = convertSpecificToFile(it)
            }
            if (fileInConsidaration != null) {
                if (savedPositions.containsKey(fileInConsidaration)) {
                    int savedPosition = savedPositions.get(fileInConsidaration)
                    Object saved = res.get(savedPosition);
                    if (saved instanceof File) {
                        File saved2 = saved as File
                        if (it instanceof File) {
                            throw new Exception("2 files ${fileInConsidaration} ${saved2}")
                        } else {
                            Object candidate = it
                            assert candidate != null
//                            BinaryWithSource candidate = (BinaryWithSource) it;
                            log.info "overriding ${saved2} from file  to ${candidate.class.simpleName} at position ${savedPosition}"
                            res.set(savedPosition, candidate)

                        }
                    } else {
                        Object candidate = it;
                        if (candidate instanceof File) {
                            File candidate2 = (File) it
                            log.info "ignore ${candidate2} as ${saved.class.simpleName} exists on position ${savedPosition}"
                        } else if (candidate.class == saved.class) {
                            throw new Exception("2 ${saved.class.simpleName} file = ${fileInConsidaration} , sources ${saved} ${candidate}")
                        } else {
                            throw new Exception("Different types ${saved} ${candidate}")
                        }
//                        if (saved instanceof BinaryWithSource) {
//                            BinaryWithSource saved2 = (BinaryWithSource) saved;
//                            if (it instanceof BinaryWithSource) {
//                                BinaryWithSource candidate = (BinaryWithSource) it;
//                                throw new Exception("2 binary with different sources : file = ${fileInConsidaration} , sources ${saved2.source} ${candidate.source}")
//
//                            } else {
//                                File candidate = (File) it
//                                log.info "ignore ${candidate} as BinaryWithSource exists on position ${savedPosition}"
//                            }
//                        }
                    }
                } else {
                    int savedPosition = res.size()
                    savedPositions.put(fileInConsidaration, savedPosition)
                    res.add(it)
                }
            } else {
                res.add(it)
            }
        }
        filesAndMavenIds = res;
    }


    File convertSpecificToFile(Object obj) {
        if (obj instanceof File) {
            return obj as File
        }
        if (obj instanceof BinaryWithSource) {
            BinaryWithSource binaryWithSource = (BinaryWithSource) obj;
            return binaryWithSource.binary
        }
        return null;
    }


    public MavenId tryFindMavenIdFromJar(File jarFile) throws Exception {
        FileInputStream2 fileInputStream = new FileInputStream2(jarFile);
        try {
            ZipInputStream zip = new ZipInputStream(fileInputStream)
            String foundFile
            Properties props

            while (true) {
                ZipEntry entry = zip.nextEntry
                if (entry == null) {
                    break;
                }
                if (entry.name.startsWith('META-INF/maven') && entry.name.endsWith('pom.properties')) {
                    if (foundFile != null) {
                        log.info("found maven props in 2 files : ${foundFile} and ${entry.name} in ${jarFile}")
                        return null
                    }
                    Properties props2 = new Properties()
                    props2.load(zip)
                    props = props2
                    foundFile = entry.name
                }

            }
            if (props == null) {
                return null;
            }
            String version = props.getProperty('version')
            String groupId = props.getProperty('groupId')
            String artifactId = props.getProperty('artifactId')
            if (version == null || groupId == null || artifactId == null) {
                log.info("strange maven props : ${props}")
                return null
            }
            MavenId mavenId = new MavenId(groupId, artifactId, version)
            return mavenId
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                log.log(Level.SEVERE, "failed on file", e)
            }
        }
    }


    Object convertFileToObject(File file) throws Exception {
        JrrUtilities3.checkFileExist(file)
        file = file.absoluteFile.canonicalFile
        String absPath = file.absoluteFile.canonicalPath.replace('\\', '/')
        if (convertPathToLowerCase) {
            absPath = absPath.toLowerCase()
        }
        Object filterResult = filterOnFile(file, absPath, file.name)
        if (!(filterResult instanceof File)) {
            return filterResult
        }
        file = filterResult as File
        file = file.canonicalFile.absoluteFile
        MavenId mavenId = mavenCommonUtils.detectMavenIdFromFileName(file)
        if (mavenId != null) {
            return mavenId;
        }
        if (file.isFile() && file.name.endsWith('.jar') && mavenCommonUtils.fileType == '') {
            try {
                mavenId = tryFindMavenIdFromJar(file)
            } catch (Throwable e) {
                log.info "failed handle file : ${file} ${e}"
                throw e;
            }
            if (mavenId != null) {
                File mavenLocalFile = mavenCommonUtils.findMavenOrGradle(mavenId)
                if (mavenLocalFile == null) {
                    onMissingMavenId(file, mavenId)
                } else {
                    return mavenId;
                }

            }
        }
        return file
    }

    void onMissingMavenId(MavenId mavenId) throws Exception {
        throw new FileNotFoundException(mavenId.toString());
    }


    void onMissingMavenId(File file, MavenId mavenId) throws Exception {
        log.info("${mavenId} resolve from ${file}, but not found in local repo")
    }

    @Deprecated
    static String getPathToParent(File parent, File child) {
        return new MavenCommonUtils().getPathToParent(parent, child);
    }

    @Deprecated
    static boolean isParent(File parent, File child) {
        return new MavenCommonUtils().isParent(parent, child);
    }


}
