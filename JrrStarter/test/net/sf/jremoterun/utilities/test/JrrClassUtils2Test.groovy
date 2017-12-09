package net.sf.jremoterun.utilities.test

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrUtilities

import java.util.logging.Logger;

@CompileStatic
public class JrrClassUtils2Test {

	private static final Logger log = Logger.getLogger(JrrUtilities.getCurrentClass().getName());

/*
	void testFinalField44() throws Exception {
		Field field2=JrrClassUtils2.findField(TestClass2,{
			it.name == 'fieeel3'
		});
		assert field2!=null
	}

	void testNoSuchField() throws Exception {
		try {
			Field field2 = JrrClassUtils2.findField(TestClass2, {
				it.name == 'nosuchfield'
			});
			fail()
		}catch (NoSuchFieldException e){

		}

	}


	void testFindMethod1() throws Exception {
		Method field2 = JrrClassUtils2.findMethod(TestClass2, {
			it.name == 'staticMethod3WithParams'
		});
		assert field2.name =='staticMethod3WithParams'
	}


	void testFindMethod() throws Exception {
		try {
			Method field2 = JrrClassUtils2.findMethod(TestClass2, {
				it.name == 'nosuchmethod'
			});
			fail()
		}catch (NoSuchFieldException e){

		}
	}
*/

}
