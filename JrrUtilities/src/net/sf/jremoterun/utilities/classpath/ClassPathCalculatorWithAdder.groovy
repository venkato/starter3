package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import org.codehaus.groovy.runtime.MethodClosure

import java.util.logging.Logger

@CompileStatic
public class ClassPathCalculatorWithAdder extends ClassPathCalculatorAbstract {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass()

	static MethodClosure addBinaryWithSource = (MethodClosure)(Closure)AddFilesToClassLoaderGroovy.&addBinaryWithSource

	static MethodClosure addMavenPath = (MethodClosure)(Closure)AddFilesToClassLoaderGroovy.&addMavenPath

	List javaSources = []

	AddFilesToClassLoaderGroovySave addFilesToClassLoaderGroovySave = new AddFilesToClassLoaderGroovySave(){

		@Override
		protected void addElement(Object obj) {
			addElement2(obj)
		}

		@Override
		List getSourceElements() {
			return javaSources
		}
	}

	ClassPathCalculatorWithAdder() {
		addFilesToClassLoaderGroovySave.mavenCommonUtils = mavenCommonUtils
	}

	protected void addElement2(Object obj) {
		filesAndMavenIds.add(obj)
	}



}
