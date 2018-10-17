package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.UrlCLassLoaderUtils

import java.util.logging.Logger

@CompileStatic
public class AddFilesToUrlClassLoaderGroovy extends AddFilesToClassLoaderGroovy{

	private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

	URLClassLoader classloader;
	HashSet<URL> urls;
	NewValueListener<File> addNewFile
	
	public AddFilesToUrlClassLoaderGroovy(URLClassLoader classloader) {
		this.classloader = classloader;
		urls = new HashSet<URL>(Arrays.asList(classloader.getURLs()));
		addNewFile = new NewValueListener<File>() {
			@Override
			void newValue(File file) {
				UrlCLassLoaderUtils.addFileToClassLoader(classloader, file);
			}
		}
	}

	AddFilesToUrlClassLoaderGroovy(NewValueListener<File> addNewFile) {
		this.addNewFile = addNewFile
		urls = new HashSet<>()
	}

	@Override
	public void addFileImpl(File file) throws Exception {
		URL urls1 = file.toURL()
		if(urls.contains(urls1)) {
			if(isLogFileAlreadyAdded) {
				log.info "already contains ${file}"
			}
		}else {
			addNewFile.newValue(file);
			urls.add(urls1)
		}
	}

}
