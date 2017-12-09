package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;

/**
 MavenId m;
 A() {m = new MavenId('', name(), '');}
 */
@CompileStatic
interface MavenIdContains {

    MavenId getM();


}
