package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic


@CompileStatic
interface IBiblioRepository {

    String name();

    String getUrl();

}