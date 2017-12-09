package net.sf.jremoterun.utilities.test;

import java.util.logging.Logger;

import net.sf.jremoterun.utilities.JrrClassUtils;
import net.sf.jremoterun.utilities.classpath.MavenId;

public class TestClass2 extends ParentClass {
	private static final Logger log = Logger.getLogger(JrrClassUtils
			.getCurrentClass().getName());

	public final Integer fieeelNotStatic = 9;
	public final int fieeelNotStaticPrim = 9;
	public static final Integer fieeel = 9;
	public static final Integer fieeel4 = 9;
	public static final Integer fieeel3 = 9;
	private static final Integer fieeel2 = 5;
	public static Integer field3 = 5;
	public Integer field6 = 5;

	public TestClass2() {
	}

	public TestClass2(int field3) {
		TestClass2.field3 = field3;
	}

	public static String staticMethod1() {
		return "1";
	}

	public static String staticMethod2WithParams(String v) {
		return v + "-1";
	}

	public static String staticMethod3WithParams(int i) {
		return i + "-1";
	}

	public static String staticMethod4WithParams(Integer i) {
		return i + "-1";
	}

	public static String staticMethod8WithParams(Integer i, String k) {
		return i + " " + k + "-1";
	}

	public static String method5WithParams(Integer i) {
		return i + "-1";
	}
	
	public static int getFieldStatic_fieeel2() {
		return fieeel2;
	}

	public static void methodThrowExc() throws Exception {
		throw new Exception("force");
	}

	public static String withMavenId(MavenId mavenId){
		return mavenId.getGroupId();
	}

	public static int withArray(String[] array){
		return 2;
	}

	public static int withArray2(String[] array){
		int a = Integer.parseInt(array[0]) ;
		return a+1;
	}
	public static int withArray3(int[] array){
		int a = array[0] ;
		return a+1;
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
 