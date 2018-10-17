package net.sf.jremoterun.utilities

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field
import java.lang.reflect.Method;
import java.lang.reflect.Modifier
import java.util.logging.Logger

import groovy.transform.CompileStatic

@CompileStatic
public class JrrClassUtils2 {
	private static final Logger log = Logger.getLogger(JrrClassUtils2.getName());


	static boolean fieldsIterator(Class clazzInit,
						   @ClosureParams(value=SimpleType.class, options="java.lang.reflect.Field")
						   final Closure<Boolean> fieldMatcher) throws NoSuchFieldException {
		assert 	fieldMatcher.parameterTypes.length ==1
		//assert 	fieldMatcher.parameterTypes[0] == Field
		Class clazz = clazzInit;
		while (true) {
			for (final Field field : JrrClassUtils.getDeclaredFields(clazz)) {
				field.setAccessible(true);
				if(fieldMatcher(field)) {
					return true
				}
			}
			clazz = clazz.getSuperclass();
			if (clazz == null) {
				// can be null for interfaces
				break
			}
			if (clazz == Object) {
				break;
			}
		}
		return false
	}

	static Field findField(Class clazzInit,
//						   @ClosureParams(Field)
		@ClosureParams(value=SimpleType.class, options="java.lang.reflect.Field")
		final Closure<Boolean> fieldMatcher) throws NoSuchFieldException {
		Field field1;
		Closure<Boolean> fieldMatcher2 = {Field f->
			if(fieldMatcher.call(f)){
				field1 = f
				return true
			}
			return false
		};
		fieldsIterator(clazzInit,fieldMatcher2)
		if(field1==null) {
			throw new NoSuchFieldException("in class ${clazzInit.getName()}");
		}
		return field1;
	}


	static boolean methodIterator(Class clazz,

							 @ClosureParams(value=SimpleType.class, options="java.lang.reflect.Method")
							 final Closure<Boolean> methodMatcher)	throws NoSuchMethodException {
		assert 	methodMatcher.parameterTypes.length ==1
		assert 	methodMatcher.parameterTypes[0] == Method
		while (true) {
			for (final Method method : JrrClassUtils.getDeclaredMethods(clazz)) {
				method.setAccessible(true);
				if(methodMatcher(method)) {
					return true
				}
			}
			clazz = clazz.getSuperclass();
			if (clazz == Object.class) {
				break;
			}
		}
		return false;
	}


	static Method findMethod(Class clazz,
//							 @ClosureParams(Method)
		@ClosureParams(value=SimpleType.class, options="java.lang.reflect.Method")
//		@ClosureParams(Method)
		final Closure<Boolean> methodMatcher)	throws NoSuchMethodException {
		Method method1;
		Closure<Boolean> fieldMatcher2 = {Method f->
			if(methodMatcher.call(f)){
				method1 = f
				return true
			}
			return false
		};
		methodIterator(clazz,fieldMatcher2)
		if(method1==null) {
			throw new NoSuchFieldException("in class ${clazz.getName()}");
		}
		return method1;

	}


	static <T> Constructor<T> findContructor(Class<T> clazz,
		@ClosureParams(value=SimpleType.class, options="java.lang.reflect.Constructor")
//		@ClosureParams(Constructor)
		final Closure<Boolean> methodMatcher) throws NoSuchMethodException {
		assert 	methodMatcher.parameterTypes.length ==1
		assert 	methodMatcher.parameterTypes[0] == Constructor
		for (final Constructor method : JrrClassUtils.getDeclaredConstructors(clazz)) {
			method.setAccessible(true);
			if(methodMatcher(method)) {
				return method
			}
		}
		throw new NoSuchMethodException(clazz.getName());
	}


	static void setFieldValue(Object onObjectOrClass,  
		Object fieldValue,
		@ClosureParams(value=SimpleType.class, options="java.lang.reflect.Field")  
		Closure<Boolean> fieldMatcher)
	throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		if (onObjectOrClass == null) {
			throw new NullPointerException("object is null for field ");
		}
		Object onInvoke;
		Class clazz;
		if (onObjectOrClass instanceof Class) {
			clazz = (Class) onObjectOrClass;
			onInvoke = null;
		} else {
			clazz = onObjectOrClass.getClass();
			onInvoke = onObjectOrClass;
		}

		Field field = findField(clazz, fieldMatcher);
		if (onInvoke == null && !Modifier.isStatic(field.getModifiers())) {
			throw new IllegalArgumentException("field is not static, but onInvoke is null");
		}
		boolean fieldFinal = Modifier.isFinal(field.getModifiers());
		if (fieldFinal) {
			JrrClassUtils.setFinalFieldValue(onInvoke, field, fieldValue);
		} else {
			field.set(onInvoke, fieldValue);
		}
	}

	static Object getFieldValue(Object object, 
		@ClosureParams(value=SimpleType.class, options="java.lang.reflect.Field")
		 Closure<Boolean> fieldMatcher)
		throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		if (object == null) {
			throw new NullPointerException("object is null");
		}
		Object onInvoke;
		Class clazz;
		if (object instanceof Class) {
			clazz = (Class) object;
			onInvoke = null;
		} else {
			clazz = object.getClass();
			onInvoke = object;
		}
		Field field = findField(clazz, fieldMatcher);
		if (onInvoke == null && !Modifier.isStatic(field.getModifiers())) {
			throw new IllegalArgumentException("field is not static, but onInvoke is null");
		}

		return field.get(onInvoke);
	}
}
