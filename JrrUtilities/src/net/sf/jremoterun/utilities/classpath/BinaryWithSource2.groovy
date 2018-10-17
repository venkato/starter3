package net.sf.jremoterun.utilities.classpath

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.Sortable
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@Canonical
@CompileStatic
class BinaryWithSource2 implements Serializable, ToFileRef2, BinaryWithSourceI {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ToFileRef2 binary;

    List<ToFileRef2> source;

    BinaryWithSource2(ToFileRef2 binary, ToFileRef2 source1) {
        this.binary = binary
        this.source = [source1]
        if(binary==null){
            throw new NullPointerException('Binary is null')
        }
        if(source1==null){
            throw new NullPointerException('Source is null')
        }
    }

    BinaryWithSource2(ToFileRef2 binary, List<ToFileRef2> source) {
        this.binary = binary
        this.source = source
        if(binary==null){
            throw new NullPointerException('Binary is null')
        }
        if(source==null){
            throw new NullPointerException('Source is null')
        }
    }

    @Override
    File resolveToFile() {
        return binary.resolveToFile()
    }

    @Override
    List<File> resolveSource() {
        return source.collect { it.resolveToFile() }
    }


}
