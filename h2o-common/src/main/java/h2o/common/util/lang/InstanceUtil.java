package h2o.common.util.lang;

import h2o.common.exception.ExceptionUtil;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public abstract class InstanceUtil {

    private static final Logger log = LoggerFactory.getLogger( InstanceUtil.class.getName() );

    private InstanceUtil() {}
	
	@SuppressWarnings("rawtypes")
	public static <T> T newInstance( Class<T> clazz , Class[] argsTypes ,  Object[] args ) {
		
		try {
		
			if( args == null || args.length == 0 ) {
				 return clazz.newInstance();
			}
			
			
			if( argsTypes == null || argsTypes.length == 0 ) {
				argsTypes = h2o.jodd.util.ClassUtil.getClasses(args);
			}
			
			
			Constructor<T> c = clazz.getConstructor(argsTypes);
			
			return c.newInstance(args);		
		
		} catch( Exception e ) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}
	
	
	
	public static <T> T newInstance( Class<T> clazz ,  Object... args ) {
		return newInstance(clazz,null,args);
	}
	
	
	public static <T> T newInstance( Class<T> clazz ) {
		return newInstance(clazz,null,null);
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Class getClass(String clazzName) {
		try {
			return ClassUtils.getClass(clazzName);
		} catch (ClassNotFoundException e) {
			log.error("getClass", e);
			throw ExceptionUtil.toRuntimeException(e);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Class getClass(ClassLoader classLoader , String clazzName) {
		try {
			return ClassUtils.getClass(classLoader, clazzName);
		} catch (ClassNotFoundException e) {
			log.error("getClass", e);
			throw ExceptionUtil.toRuntimeException(e);
		}
	}


}
