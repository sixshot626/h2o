package h2o.jodd.bean.exception;

import h2o.jodd.bean.BeanException;
import h2o.jodd.bean.BeanProperty;

public class PropertyNotFoundBeanException extends BeanException {
	public PropertyNotFoundBeanException(String message, BeanProperty bp) {
		super(message + " Property: " + bp.toString());
	}
}
