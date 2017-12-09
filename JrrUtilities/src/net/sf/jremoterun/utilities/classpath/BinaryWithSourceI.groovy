package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
interface BinaryWithSourceI extends ToFileRef2 {



    List<File> resolveSource();

}
