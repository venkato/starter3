package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.UrlCLassLoaderUtils
import net.sf.jremoterun.utilities.UrlClassLoaderAddFileJava11Aware

import java.util.logging.Logger

@CompileStatic
public class AddFilesToJava11AwareClassLoaderGroovy extends AddFilesToUrlClassLoaderGroovy{

	private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

	public AddFilesToJava11AwareClassLoaderGroovy(ClassLoader classloader) {
		super(new ClassLoaderJava11AwareNewValue(classloader))
	}

}
