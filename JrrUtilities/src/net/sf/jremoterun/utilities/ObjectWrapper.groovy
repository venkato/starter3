package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic;

@CompileStatic
public class ObjectWrapper<T> {

    public volatile T object;

    public ObjectWrapper(final T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(final T object) {
        this.object = object;
    }
    
    @Override
    public String toString() {
        return "${object}";
    }

}
