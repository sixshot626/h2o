/**
 * @author 张建伟
 */
package h2o.flow.pvm.exception;

public abstract class FlowException extends RuntimeException {


	private static final long serialVersionUID = 9096081634629771374L;

	public FlowException() {
		super();		
	}

	public FlowException(String message, Throwable cause) {
		super(message, cause);		
	}

	public FlowException(String message) {
		super(message);		
	}

	public FlowException(Throwable cause) {
		super(cause);		
	}
	
	

}
