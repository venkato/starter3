package net.sf.jremoterun.utilities.test

import groovy.transform.CompileStatic
import junit.framework.TestCase
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.groovystarter.st.GroovyMethodFinder
import net.sf.jremoterun.utilities.groovystarter.st.GroovyMethodFinderException
import org.codehaus.groovy.runtime.InvokerHelper

import java.util.logging.Logger

@CompileStatic
class MethodFinderTest extends TestCase {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    TestClass2 ins = new TestClass2()

    GroovyMethodFinder groovyMethodFinder = new GroovyMethodFinder()


    @Override
    protected void setUp() throws Exception {
        ins = new TestClass2()
        groovyMethodFinder = new GroovyMethodFinder()
    }

    void testSimple() {
        String res = groovyMethodFinder.runMethod(ins, ["staticMethod1"])
        assert res == "1"
    }


    void testWithParamString() {
        String res = groovyMethodFinder.runMethod(ins, ["staticMethod2WithParams", "str"])
        assert res == "str-1"
    }

    void testWithArray1() {
        int res = groovyMethodFinder.runMethod(ins, ["withArray", ""]) as int
        assert res == 2
    }

    void testWithArray2() {
        int res = groovyMethodFinder.runMethod(ins, ["withArray2", "1"]) as int
        assert res == 2
    }

    void testWithArray3() {
        int res = groovyMethodFinder.runMethod(ins, ["withArray3", "1"]) as int
        assert res == 2
    }

    void testWithArrayExc() {
        try{
        groovyMethodFinder.runMethod(ins, ["withArray3", "1a"]) as int
        } catch (NumberFormatException e) {
            assert e.message== 'For input string: "1a"'
        }
    }


    void testWithParamInt() {
        String res = groovyMethodFinder.runMethod(ins, ["staticMethod3WithParams", "3"])
        assert res == "3-1"
    }

    void testNoSuchMethod1() {
        try {
            groovyMethodFinder.runMethod(ins, ["nosuchmethod", "3"])
            fail()
        } catch (GroovyMethodFinderException e) {
//            e.printStackTrace()
            assert e.message.contains("wrong method nosuchmethod, available")
            assert e.message.contains("staticMethod2WithParams")
        }
    }

    void testNoSuchMethod2() {
        try {
            groovyMethodFinder.runMethod(ins, ["staticMethod3WithParams", "3", "3", "23"])
            fail()
        } catch (GroovyMethodFinderException e) {
            assert e.message.contains("wrong method staticMethod3WithParams arguments count, got : 3")
            assert e.message.contains("(int)")
        }
    }

    void testMethodErr() {
        try {
            groovyMethodFinder.runMethod(ins, ["methodThrowExc"])
            fail()
        } catch (Exception e) {
            assert e.message == "force"
        }
    }

    void testWithParamIntErr() {
        try {
            groovyMethodFinder.runMethod(ins, ["staticMethod3WithParams", "str"])
            fail()
        } catch (NumberFormatException e) {
            assert e.message == "For input string: \"str\""
        }
    }

    void testScript() {
        Script script = new Script() {
            @Override
            Object run() {
                return "56"
            }
        }
        String res = groovyMethodFinder.runMethod(script, [])
        assert res == "56"
    }

    void testRunnable() {
        assert ins.field6 == 5
        Runnable runnable = {
            ins.field6 = 65
        }
        Object res = groovyMethodFinder.runMethod(runnable, [])
        assert res == null
        assert ins.field6 == 65
    }

    void testScriptNoSuchMethod() {
        Script script = new Script() {
            @Override
            Object run() {
                return "56"
            }
        }
        try {
            groovyMethodFinder.runMethod(script, ["nosuchMenthod"])
            fail()
        } catch (GroovyMethodFinderException e) {
            assert e.message == "wrong method nosuchMenthod, available : run"
        }
    }

    void testRunnableNoSuchMethod() {
        Runnable script = {
            ins.field6 = 65
        }
        try {
            groovyMethodFinder.runMethod(script, ["nosuchMenthod"])
            fail()
        } catch (GroovyMethodFinderException e) {
//            assert e.message == "wrong method nosuchMenthod, available : run"
        }
    }


    void testArgsConverter() {
        String res = groovyMethodFinder.runMethod(ins, ["withMavenId", 'org.codehaus.groovy:groovy-all:2.4.12'])
        assert res == 'org.codehaus.groovy';
    }

    void testArgsConverterException() {
        try {
            groovyMethodFinder.runMethod(ins, ["withMavenId", 'org.codehaus.groovy:groovy-all:2.4.12:someinvaldstauff:'])
        } catch (IllegalArgumentException e) {
            assert e.message.contains("someinvaldstauff")
        }

    }

    void test21() {
        String s = 'org.codehaus.groovy:groovy-all:2.4.12'
        def of = InvokerHelper.invokeConstructorOf(MavenId, s)
        log.info "${of}"
        new MavenId('org.codehaus.groovy:groovy-all:2.4.12');
//        MavenId m23 =s as MavenId
//        MavenId m2 =[s] as MavenId
//        log.info "${m2}"
    }




}
