package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.mdep.ivy.IBiblioRepository

@CompileStatic
class MavenIdAndRepo implements MavenIdAndRepoContains, ToFileRef2 {


    MavenId m;
    IBiblioRepository repo;

    /**
     *
     * @param m
     * @param repo can be null, which means default
     */
    MavenIdAndRepo(MavenId m, IBiblioRepository repo) {
        this.m = m
        this.repo = repo
        if(m==null){
            throw new NullPointerException('MavenId is null')
        }
    }


    @Override
    File resolveToFile() {
        AddFilesToClassLoaderCommonDummy classLoaderCommonDummy = new AddFilesToClassLoaderCommonDummy()
        classLoaderCommonDummy.addM this
        if (classLoaderCommonDummy.addedFiles2.size() == 0) {
            throw new FileNotFoundException(this.toString())
        }
        return classLoaderCommonDummy.addedFiles2[0]
    }

    @Override
    MavenIdAndRepo getMavenIdAndRepo() {
        return this
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.getClass()) return false

        MavenIdAndRepo that = (MavenIdAndRepo) o

        if (m != that.m) return false
        if (repo != that.repo) return false

        return true
    }

    int hashCode() {
        int result
        result = (m != null ? m.hashCode() : 0)
        result = 31 * result + (repo != null ? repo.hashCode() : 0)
        return result
    }

    @Override
    String toString() {
        return "${m} ${repo}"
    }
}
