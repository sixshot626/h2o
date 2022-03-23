package h2o.jodd.bean.exception;

import h2o.jodd.bean.BeanException;
import h2o.jodd.bean.BeanProperty;

public class NullPropertyBeanException extends BeanException {
	public NullPropertyBeanException(String message, BeanProperty bp) {
		super(message + " Property: " + bp.toString());
	}
}
