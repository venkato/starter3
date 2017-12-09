package net.sf.jremoterun.utilities.test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import groovy.lang.Binding;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.jremoterun.utilities.JrrClassUtils;
import net.sf.jremoterun.utilities.JrrUtilities;
import net.sf.jremoterun.utilities.PrimiteClassesUtils;
import net.sf.jremoterun.utilities.groovystarter.st.GroovyMethodFinder;
import net.sf.jremoterun.utilities.groovystarter.st.GroovyMethodFinderException;

public class JrrClassUtilsTest extends TestCase {

	private static final Logger log = Logger.getLogger(JrrUtilities.getCurrentClass().getName());

	public void testFinalField4() throws Exception {
		Assert.assertEquals(TestClass2.fieeel, new Integer(9));
		final Field field2 = TestClass2.class.getDeclaredField("fieeel");
		field2.setAccessible(true);
		Object object = field2.get(null);
		Assert.assertEquals(object, new Integer(9));
		JrrClassUtils.setFinalFieldValue(null, field2, 12);
		// log.info(TestClass2.fieeel + "");
		Assert.assertEquals(TestClass2.fieeel, new Integer(12));
		JrrClassUtils.setFinalFieldValue(null, field2, 11);
		// log.info(TestClass2.fieeel + "");
		Assert.assertEquals(TestClass2.fieeel, new Integer(11));
	}

	public void testConstructor3() throws Exception {
		Map params = new HashMap();
		Class<?> bindingClass = Binding.class;
		Object binding = JrrClassUtils.invokeConstructor(bindingClass, params);
//		log.info(binding);
	}

	public void testConstructor1() throws Exception {
		TestClass2 invokeConstructor = JrrClassUtils.invokeConstructor(TestClass2.class);
		assertNotNull(invokeConstructor);
	}

	public void testConstructor2() throws Exception {
		TestClass2 testClass2 = JrrClassUtils.invokeConstructor(TestClass2.class, 213);
		assertEquals(TestClass2.field3, new Integer(213));
	}

	public void testStaticMethod1() throws Exception {
		assertEquals("1", JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod1"));
	}

	public void testStaticMethod2WithParams() throws Exception {
		assertEquals("test-1", JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod2WithParams", "test"));
	}

	public void testStaticMethod3WithParams() throws Exception {
		assertEquals("1-1", JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod3WithParams", 1));
	}

	public void testStaticArray() throws Exception {
		Object args = new String[]{"1","2","3"};
		assertEquals(2, JrrClassUtils.invokeJavaMethod(TestClass2.class, "withArray", args));
	}

	public void testStaticMethod4WithParams() throws Exception {
		assertEquals("1-1", JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod4WithParams", 1));
	}

	public void testStaticMethod4WithParams3() throws Exception {
		try {
			JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod4WithParams", 2);
		} catch (NoSuchMethodException e) {
			// e.printStackTrace();
		}
	}

	public void testStaticMethod4WithParams2() throws Exception {
		assertEquals("null-1",
				JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod4WithParams", new Object[] { null }));
	}

	public void testStaticMethod8WithParams() throws Exception {
		assertEquals("null null-1",
				JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod8WithParams", null, null));
	}

	public void testStaticMethod7WithParams() throws Exception {
		assertEquals("1-1", JrrClassUtils.invokeJavaMethod(TestClass2.class, "staticMethod7WithParams", 1));
	}

	public void testMethod5WithParams() throws Exception {
		assertEquals("1-1", JrrClassUtils.invokeJavaMethod(new TestClass2(), "method5WithParams", 1));
	}

	public void testMethod6WithParams() throws Exception {
		assertEquals("1-1", JrrClassUtils.invokeJavaMethod(new TestClass2(), "method6WithParams", 1));
	}

	public void testPrimitives2() throws Exception {
		assertEquals(int.class, PrimiteClassesUtils.loadPrimitiveClass("int"));
		assertEquals(int.class, PrimiteClassesUtils.loadPrimitiveClass("I"));
		assertEquals(int[].class, PrimiteClassesUtils.loadPrimitiveClass("[I"));
		assertEquals(int.class, PrimiteClassesUtils.wrapperToPrimitive(Integer.class));
		assertEquals(Integer.class, PrimiteClassesUtils.primitiveToWrapper(int.class));
	}

	public void testPrimitives() throws Exception {
		testEquals(8, PrimiteClassesUtils.primitiveArraysClasses);
		testEquals(8, PrimiteClassesUtils.primitiveClassesArraysShortName);
		assertEquals("[[I, [J, [Z, [C, [S, [B, [F, [D]", PrimiteClassesUtils.primitiveClassesArraysShortName + "");
		testEquals(9, PrimiteClassesUtils.primitiveClassesNames);
		assertEquals("[int, long, boolean, char, short, byte, float, double, void]",
				PrimiteClassesUtils.primitiveClassesNames + "");
		testEquals(9, PrimiteClassesUtils.primitiveClasses);
		testEquals(9, PrimiteClassesUtils.primitiveClassesShortName);
		assertEquals("[I, J, Z, C, S, B, F, D, V]", PrimiteClassesUtils.primitiveClassesShortName + "");
		testEquals(9, PrimiteClassesUtils.primitiveWrapperClasses);
	}

	private void testEquals(int exptected, Collection list) {
		assertEquals(exptected, list.size());
		assertEquals(exptected, new HashSet(list).size());
	}

	public void testGetCurrentClass() throws Exception {
		assertEquals(JrrClassUtilsTest.class, JrrClassUtils.getCurrentClass());
		assertEquals(JrrClassUtilsTest.class.getClassLoader(), JrrClassUtils.getCurrentClassLoader());
	}

	int param;

	public void setParamTo2() {
		param = 2;
	}

	public void setParamList(List<Integer> list) {
		param =  list.get(1);
	}

	public void setParam(int param) {
		this.param = param;
	}

	public void testInvokeMethodNoArgs() throws Exception {
		GroovyMethodFinder finder = new GroovyMethodFinder();
		param = 0;
		String[] args = { "setParamTo2" };
		finder.runMethod(this, Arrays.asList(args));
		assertEquals(param, 2);
	}

	public void testInvokeMethodWthArgs() throws Exception {
		GroovyMethodFinder finder = new GroovyMethodFinder();
		param = 0;
		String[] args = { "setParam", "7" };
		finder.runMethod(this, Arrays.asList(args));
		assertEquals(param, 7);
	}

	public void testInvokeMethodWthArgsList() throws Exception {
		GroovyMethodFinder finder = new GroovyMethodFinder();
		param = 0;
		String[] args = { "setParamList", "7,9" };
		finder.runMethod(this, Arrays.asList(args));
		assertEquals(param, 9);
	}

	public void testInvokeMethodWthWrongArgs() throws Exception {
		GroovyMethodFinder finder = new GroovyMethodFinder();
		param = 0;
		String[] args = { "setParam", "7", "23" };
		try {
			finder.runMethod(this, Arrays.asList(args));
			fail();
		} catch (GroovyMethodFinderException e) {			
		}
	}
	

	public void testInvokeMethodWthWrongName() throws Exception {
		GroovyMethodFinder finder = new GroovyMethodFinder();
		param = 0;
		String[] args = { "wrongMethodName", "7", "23" };
		try {
			finder.runMethod(this, Arrays.asList(args));
			fail();
		} catch (GroovyMethodFinderException e) {			
		}
	}

	public void testFinalField44() throws Exception {
		final Field field2 = TestClass2.class.getDeclaredField("fieeel3");
		field2.setAccessible(true);
		JrrClassUtils.setFinalFieldValue(null,field2, 12);
		Assert.assertEquals(TestClass2.fieeel3, new Integer(12));
	}

	public void testFinalField5() throws Exception {
		Assert.assertEquals(TestClass2.fieeel4, new Integer(9));
		final Field field2 = TestClass2.class.getDeclaredField("fieeel4");
		field2.setAccessible(true);
		JrrClassUtils.setFieldValue(TestClass2.class, "fieeel4", 12);
		log.info(String.valueOf(TestClass2.fieeel4));
		Assert.assertEquals(TestClass2.fieeel4, new Integer(12));
		JrrClassUtils.setFinalFieldValue(TestClass2.class, field2, 11);
		log.info(String.valueOf(TestClass2.fieeel4));
		Assert.assertEquals(TestClass2.fieeel4, new Integer(11));
	}


}

/* 
 * JRemoteRun.sf.net. License:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
 