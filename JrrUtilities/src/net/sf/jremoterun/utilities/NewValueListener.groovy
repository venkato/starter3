package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic;

@CompileStatic
public interface NewValueListener<T> {

	void newValue(T t);
}
