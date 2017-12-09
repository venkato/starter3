package net.sf.jremoterun.utilities.init.utils


import net.sf.jremoterun.utilities.init.commonrunner.CommonRunner;



/**
 * Don't delete
 */
@Deprecated
class AddFileToUrlClassloaderUtil {



    static void addFileToUrlClassLoader(URLClassLoader cll, File file) {
        assert file.exists()
        assert file.canRead()
        cll.addURL(file.toURL())
    }


}
