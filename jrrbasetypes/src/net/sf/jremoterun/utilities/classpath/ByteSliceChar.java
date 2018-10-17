package net.sf.jremoterun.utilities.classpath;

public interface ByteSliceChar {

    char[] getArray();

    int getOffset();

    int getLength();


    void set(char[] array, int offset, int length);

}
