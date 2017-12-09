package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
//import sun.misc.URLClassPath

import java.lang.management.ManagementFactory
import java.util.logging.Logger
; ;


@CompileStatic
public class UrlCLassLoaderUtils {
    private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);


    static List<File> getFilesFromUrlClassloader(URLClassLoader urlClassLoader) throws Exception {

        //sun.misc.URLClassPath
        Object ucp = JrrClassUtils.getFieldValue(urlClassLoader, "ucp")
        Collection loaders = JrrClassUtils.getFieldValue(ucp, "loaders") as ArrayList;
        List<URL> urlss = loaders.findAll { it.class.name.contains('JarLoader') }.collect {
            JrrClassUtils.getFieldValue(it, "csu")
        } as List<URL>

        //log.info("classloader class name ${urlClassLoader.class.name}")
        List<File> collect = urlss.collect { UrlToFileConverter.c.convert(it) };

        collect.addAll((List) loaders.findAll { it.class.name.contains('FileLoader') }.collect {
            JrrClassUtils.getFieldValue(it, "dir")
        })
        return collect
    }

    static URL getClassLocation3(final Class clazz)
            throws MalformedURLException {
        final String tailJava = buildClassNameSuffix(clazz.getName());
        final String tailGroovy = buildClassNameSuffixGroovy(clazz.getName());;
        String tail = tailJava
//        log.info "j = ${tailJava}, g = ${tailGroovy}"
        ClassLoader cl = clazz.getClassLoader()
        final URL urlRes = cl.getResource(tailJava);
        if (urlRes == null) {
            tail = tailGroovy
            urlRes = cl.getResource(tailGroovy);
            if (urlRes == null) {
                return null;
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

    static String buildClassNameSuffix(String className){
        final String tail = className.replace('.', '/') + ".class";
        return tail
    }

    static String buildClassNameSuffixGroovy(String className){
        final String tail = className.replace('.', '/') + ".groovy";
        return tail
    }

    static File convertClassLocationToPathToJar(URL urlRes,String className){
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



    static File convertClassLocationToPathToJar2(URL location){
        if (location == null) {
            throw new NullPointerException("class location is null")
        }

        File file = UrlToFileConverter.c.convert location
        // JrrUtilities3.checkFileExist(file)
        return file

    }


    static List<File> calculateClassPathFromJmx() throws Exception {
        String s = ManagementFactory.getRuntimeMXBean().getClassPath();
        List<String> files = s.tokenize(';')
        List<File> files3 = files.collect { new File(it) };
        return files3
    }





}
