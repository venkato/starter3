package net.sf.jremoterun.utilities.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;


import javassist.util.proxy.Proxy;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.jremoterun.JrrUtils;
import net.sf.jremoterun.URLClassLoaderExt;
import net.sf.jremoterun.utilities.JrrUtilities;
import net.sf.jremoterun.utilities.MBeanClient;
import net.sf.jremoterun.utilities.MBeanFromJavaBean;
import net.sf.jremoterun.utilities.MbeanConnectionCreator;
import net.sf.jremoterun.utilities.ThreadUtils;
import net.sf.jremoterun.utilities.UrlCLassLoaderUtils;
import net.sf.jremoterun.utilities.javassist.InvokcationAccessor;
import net.sf.jremoterun.utilities.javassist.JavassistProxyFactory;

public class JrrUtilititiesTest extends TestCase {

	private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

	public void testThreadLocal() throws Exception {
		Map<Thread, Object> findThreadLocalValues = ThreadUtils.findThreadLocalValues();
	}

	public void testMbeanClientAsStandartMbean() throws Exception {
		MBeanServer beanServer2 = JrrUtils.findLocalMBeanServer();
		SimpleBean realBean = new SimpleBean();
		ObjectName objectName = new ObjectName("somedomain:type=sometype1");
		beanServer2.registerMBean(realBean, objectName);
		SimpleBeanMBean proxy = MBeanClient.buildMbeanClient(SimpleBeanMBean.class,
				MbeanConnectionCreator.localConnectionCreator, objectName);
		testMbeanClient22(proxy, realBean, beanServer2, objectName);
	}

	public void testMbeanClientMBeanFromJavaBean() throws Exception {
		MBeanServer beanServer2 = JrrUtils.findLocalMBeanServer();
		SimpleBean realBean = new SimpleBean();
		ObjectName objectName = new ObjectName("somedomain:type=sometype2");
		MBeanFromJavaBean.registerMBean(realBean, objectName);
		SimpleBeanMBean proxy = MBeanClient.buildMbeanClient(SimpleBeanMBean.class,
				MbeanConnectionCreator.localConnectionCreator, objectName);
		testMbeanClient22(proxy, realBean, beanServer2, objectName);
		{
			Assert.assertEquals(beanServer2.getAttribute(objectName, "SomeAttributeNoMethods"), 3);
		}
		{
			beanServer2.setAttribute(objectName, new Attribute("SomeAttributeNoMethods", 8));
			Assert.assertEquals(realBean.someAttributeNoMethods, 8);
		}
		try {
			beanServer2.setAttribute(objectName, new Attribute("SomeAttributeStatic", 8));
			fail();
		} catch (AttributeNotFoundException e) {
		}
	}

	public void testMbeanClientMBeanFromJavaBeanStatic() throws Exception {
		MBeanServer beanServer2 = JrrUtils.findLocalMBeanServer();
		SimpleBean realBean = new SimpleBean();
		ObjectName objectName = new ObjectName("somedomain:type=sometype3");
		JrrUtils.findLocalMBeanServer()
				.registerMBean(new MBeanFromJavaBean(realBean, realBean.getClass(), true, true, false), objectName);
		SimpleBeanMBean proxy = MBeanClient.buildMbeanClient(SimpleBeanMBean.class,
				MbeanConnectionCreator.localConnectionCreator, objectName);
		testMbeanClient22(proxy, realBean, beanServer2, objectName);
		{
			beanServer2.setAttribute(objectName, new Attribute("SomeAttributeStatic", 8));
			Assert.assertEquals(realBean.someAttributeStatic, 8);
		}
	}

	private static void testMbeanClient22(SimpleBeanMBean proxy, SimpleBean realBean, MBeanServerConnection connection,
			ObjectName objectName) throws Exception {
		proxy.toString();
		Proxy proxy3 = (Proxy) proxy;
		log.info("proxy classloder: " + proxy3.getClass().getClassLoader() + "");
		InvokcationAccessor proxy2 = (InvokcationAccessor) proxy;
		JavassistProxyFactory mBeanClient = proxy2._getJavassistProxyFactory();

		log.info(mBeanClient.toString());
		Assert.assertTrue(proxy.equals(proxy));
		int result = proxy.someMethod(43);
		Assert.assertEquals(result, 44);
		{
			int someAttribute = proxy.getSomeAttribute();
			Assert.assertEquals(someAttribute, 2);
		}

		proxy.setSomeAttribute(4);
		Assert.assertEquals(realBean.someAttribute, 4);
		Assert.assertEquals(proxy.getAB(), 8);
		proxy.setAB(9);
		Assert.assertEquals(realBean.aB, 9);
		{
			realBean.someAttribute = 7;
			int someAttribute = (Integer) connection.getAttribute(objectName, "SomeAttribute");
			Assert.assertEquals(someAttribute, 7);
		}
		{
			connection.setAttribute(objectName, new Attribute("SomeAttribute", 8));
			Assert.assertEquals(realBean.someAttribute, 8);
		}

		{
			connection.setAttribute(objectName, new Attribute("AB", 18));
			Assert.assertEquals(realBean.aB, 18);
		}
		{
			int res = (Integer) connection.invoke(objectName, "someMethod", new Object[] { 19 },
					new String[] { int.class.getName() });
			Assert.assertEquals(res, 20);
		}
	}

	public void testFindLocalVmsJava5() throws Exception {
		List localVms = net.sf.jremoterun.utilities.JmxLocalVmConnectionJava5678.findLocalVms();
		for (Object properties : localVms) {
			log.info(properties.toString());
		}
		
	}


	
	public void testFindLocalVmsJava7() throws Exception {
		List localVms = net.sf.jremoterun.utilities.JmxLocalVmConnectionJava7.findLocalVms();
		for (Object properties : localVms) {
			log.info(properties.toString());
		}
		
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
 