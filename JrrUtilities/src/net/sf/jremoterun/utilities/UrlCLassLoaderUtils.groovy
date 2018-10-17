package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.ClRef
import java.lang.management.ManagementFactory
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.logging.Logger



@CompileStatic
public class UrlCLassLoaderUtils {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    static List<File> getFilesFromUrlClassloader(URLClassLoader urlClassLoader) throws Exception {
        List<File> files = getFilesFromUrlClassloader2(urlClassLoader)
        if (files.size() == 0) {
            List<URL> urls = urlClassLoader.getURLs().toList()
            if (urls.size() != 0) {
                log.info "failed find files in urlclassloader"
            }
            List<File> collect = urls.collect { UrlToFileConverter.c.convert(it) };
            return collect
        }
        return files
    }

    static List<File> getFilesFromUrlClassloader2(URLClassLoader urlClassLoader) throws Exception {
        //sun.misc.URLClassPath

        Object ucp = JrrClassUtils.getFieldValue(urlClassLoader, "ucp")
        Collection loaders = JrrClassUtils.getFieldValueR(new ClRef('sun.misc.URLClassPath'), ucp, "loaders") as ArrayList;
        List<URL> urlss = loaders.findAll { it.class.name.contains('JarLoader') }.collect {
            JrrClassUtils.getFieldValueR(new ClRef('sun.misc.URLClassPath$JarLoader'), it, "csu")
        } as List<URL>

        //log.info("classloader class name ${urlClassLoader.class.name}")
        List<File> collect = urlss.collect { UrlToFileConverter.c.convert(it) };
        List<File> col2 = loaders.findAll { it.class.name.contains('FileLoader') }.collect {
            (File) JrrClassUtils.getFieldValueR(new ClRef('sun.misc.URLClassPath$FileLoader'), it, "dir")
        }
        collect.addAll(col2)
        return collect
    }

    static URL getClassLocation3(final Class clazz)
            throws MalformedURLException {
        if (clazz.equals(Class)) {
            throw new IllegalArgumentException("Strange class name")
        }
        final String tailJava = buildClassNameSuffix(clazz.getName());
        final String tailGroovy = buildClassNameSuffixGroovy(clazz.getName()); ;
        String tail = tailJava
//        log.info "j = ${tailJava}, g = ${tailGroovy}"
        URL urlRes;
        ClassLoader cl = clazz.getClassLoader()
        if (cl == null) {
            urlRes = clazz.getResource('/' + tailJava)
            if (urlRes == null) {
                tail = tailGroovy
                urlRes = clazz.getResource('/' + tailGroovy);
                if (urlRes == null) {
                    return null;
                }
            }
        } else {
            urlRes = cl.getResource(tailJava);
            if (urlRes == null) {
                tail = tailGroovy
                urlRes = cl.getResource(tailGroovy);
                if (urlRes == null) {
                    return null;
                }
            }
        }
        String url = urlRes.toString();
        url = url.substring(0, url.length() - tail.length());
        if (url.startsWith("jar:")) {
            url = url.substring(4, url.length() - 2);
        }
        return new URL(url);
    }

    static URL convertClassLoacation

    /**
     * @return path to jar-file or main directory, where clazz is located.
     */
    static File getClassLocation(Class aClass) {
        URL location = getClassLocation3(aClass);
        if (location == null) {
            throw new FileNotFoundException("can't find class location : ${aClass.name}")
        }
        return convertClassLocationToPathToJar2(location)
    }

    static String buildClassNameSuffix(String className) {
        final String tail = className.replace('.', '/') + ".class";
        return tail
    }

    static String buildClassNameSuffixGroovy(String className) {
        final String tail = className.replace('.', '/') + ".groovy";
        return tail
    }

    static File convertClassLocationToPathToJar(URL urlRes, String className) {
        if (urlRes == null) {
            throw new NullPointerException("class location is null for ${className}")
        }
        final String tail = buildClassNameSuffix(className);
        String url = urlRes.toString();
        url = url.substring(0, url.length() - tail.length());
        if (url.startsWith("jar:")) {
            url = url.substring(4, url.length() - 2);
        }
        return convertClassLocationToPathToJar2(new URL(url))

    }


    static File convertClassLocationToPathToJar2(URL location) {
        if (location == null) {
            throw new NullPointerException("class location is null")
        }

        File file = UrlToFileConverter.c.convert location
        // net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(file)
        return file

    }


    static List<File> calculateClassPathFromJmx() throws Exception {
        String s = ManagementFactory.getRuntimeMXBean().getClassPath();
        List<String> files = s.tokenize(';')
        List<File> files3 = files.collect { new File(it) };
        return files3
    }


    public static void addFileToClassLoader(final URLClassLoader classLoader, File file)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
        if (classLoaderAddUrlMethod == null) {
            Method classLoaderAddUrlMethod1 = JrrClassUtils.findMethodByParamTypes3(URLClassLoader, "addURL", URL);
            classLoaderAddUrlMethod1.setAccessible(true);
            classLoaderAddUrlMethod = classLoaderAddUrlMethod1
        }
        file = file.getAbsoluteFile();
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        classLoaderAddUrlMethod.invoke(classLoader, file.toURI().toURL());
    }

    public static Method classLoaderAddUrlMethod;



}
