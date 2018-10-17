package net.sf.jremoterun.utilities.classpath;

public interface ByteSliceI {

    byte[] getArray();

    int getOffset();

    int getLength();


    void set(byte[] array, int offset, int length);

}
