package net.sf.jremoterun.utilities.init.utils;

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.init.commonrunner.CommonRunner

import java.util.logging.Logger;


@CompileStatic
class CopyFileUtil {
    private static Logger log = Logger.getLogger(CopyFileUtil.name)

    static void copyFileIfNeeded(File src, File dest) {
        assert src.exists()
        assert dest.parentFile.exists()
        boolean needed = false;
        if (!needed) {
            needed = !dest.exists()
        }
        if (!needed) {
            needed = src.length() != dest.length()
        }
        if (!needed) {
            needed = src.lastModified() != dest.lastModified()
        }
        if (needed) {
            log.info("coping ${src} to ${dest}")
            dest.bytes = src.bytes
            dest.setLastModified(src.lastModified())
        }
    }


    static void copyLibs(File grHome) {
        assert grHome != null
        File originDir = new File(grHome, "libs/origin/")
        File copyDir = new File(grHome, "libs/copy/")
        originDir.listFiles().toList().each {
            File copyF = new File(copyDir, it.name)
            CopyFileUtil.copyFileIfNeeded(it, copyF)
        }
    }


    static void runCustomizer() {
        GroovyClassLoader cll = (GroovyClassLoader) CopyFileUtil.getClassLoader()
        File customDir = new File(CommonRunner.GR_HOME, "custom/src")
        if (customDir.exists()) {
            AddFileToUrlClassloaderUtil.addFileToUrlClassLoader(cll, customDir)
            File customLoader3 = new File(customDir, "jrrstarter/CustomRunner.groovy")
            if (customLoader3.exists()) {
                Runnable customRunnerInstance = (Runnable) cll.loadClass('jrrstarter.CustomRunner').newInstance()
                customRunnerInstance.run()
            }
        }
    }

    static String getFirstParam() {
        if (CommonRunner.args.size() <= 1) {
            return null;
        }
        return CommonRunner.args[1]
    }

    public static String questionString = ':h'

    static String getFirstParam2(String shortcuts) {
        String fParam = getFirstParam()
        if (fParam == questionString) {
            log.info shortcuts
        }
        return fParam
    }

    static String removeFirstParam() {
        return CommonRunner.args.remove(1)
    }


    static void addAllJarsInDir2(URLClassLoader cll, File dir) {
        boolean jarFound = addAllJarsInDirImpl(cll, dir)
        if (!jarFound) {
            throw new FileNotFoundException("no jars in dir ${dir}")
        }
    }

    static boolean addAllJarsInDirImpl(URLClassLoader cll, File dir) {
        assert dir.exists()
        assert dir.canRead()
        assert dir.isDirectory()
        List<File> jars = dir.listFiles().toList().findAll { it.name.endsWith('.jar') }
        jars.each { AddFileToUrlClassloaderUtil.addFileToUrlClassLoader(cll, it) }
        return jars.size() > 0
    }

    static File detectBaseDir() {
        ClassLoader classLoader = GroovyObject.classLoader
        URL resource = classLoader.getResource(GroovyObject.name.replace('.', '/') + '.class')
        String res2 = resource.toString();
        if (res2.startsWith("jar:")) {
            res2 = res2.substring(4, res2.length());
        }
        res2 = res2.replace('!/groovy/lang/GroovyObject.class', '')
        URL url3 = new URL(res2)
        File baseDir2 = url3.file as File
        assert baseDir2.exists();
        return baseDir2.parentFile.parentFile.parentFile
    }


    private File detectBaseDirOld() {
        URLClassLoader classLoader = GroovyObject.classLoader as URLClassLoader
        List<URL> all = classLoader.getURLs().toList().findAll { it.toString().tokenize('/').last().contains('groovy') }
        File baseDir = all.first().file as File
        assert baseDir.exists()
        baseDir = baseDir.parentFile.parentFile.parentFile;
        // println("base dir = ${baseDir}")
        return baseDir
    }

//        println "resource = ${resource}"
//        println "resource 2 = ${resource.file}"
//        res2 = res2.replace('jar:file:/','')

}
