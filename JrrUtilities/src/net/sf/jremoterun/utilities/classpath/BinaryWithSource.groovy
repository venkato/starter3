package net.sf.jremoterun.utilities.classpath

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.Sortable
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@Canonical
@CompileStatic
@Sortable
class BinaryWithSource implements Serializable,ToFileRef2,BinaryWithSourceI {


    File binary;

    File source;

    @Override
    File resolveToFile() {
        return binary
    }

    @Override
    List<File> resolveSource() {
        return [source]
    }

}
