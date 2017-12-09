package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3

import java.util.logging.Logger

@CompileStatic
public class AddFilesToUrlClassLoaderGroovy extends AddFilesToClassLoaderGroovy{

	private static final Logger log = Logger.getLogger(JrrClassUtils.currentClass.name);

	URLClassLoader classloader;
	HashSet<URL> urls;
	
	public AddFilesToUrlClassLoaderGroovy(URLClassLoader classloader) {
		this.classloader = classloader;
		urls = new HashSet<URL>(Arrays.asList(classloader.getURLs()));
	}
	
	@Override
	public void addFileImpl(File file) throws Exception {
		if(urls.contains(file.toURL())) {
			if(isLogFileAlreadyAdded) {
				log.info "already contains ${file}"
			}
		}else {
			JrrUtilities3.addFileToClassLoader(classloader, file);
		}
	}

}
