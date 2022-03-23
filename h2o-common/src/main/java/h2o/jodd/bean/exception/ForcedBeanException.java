package h2o.jodd.bean.exception;

import h2o.jodd.bean.BeanException;
import h2o.jodd.bean.BeanProperty;

public class ForcedBeanException extends BeanException {
	public ForcedBeanException(String message, BeanProperty bp, Throwable cause) {
		super(message + " Property: " + bp.toString(), cause);
	}
}
