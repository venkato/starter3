package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.mdep.ivy.IBiblioRepository

@CompileStatic
interface MavenIdAndRepoContains {


    MavenIdAndRepo getMavenIdAndRepo();




}
