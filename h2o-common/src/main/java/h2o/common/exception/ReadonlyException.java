package h2o.common.exception;

public class ReadonlyException extends RuntimeException {

    private static final long serialVersionUID = -1774452240981066001L;

    public ReadonlyException() {
        super();
    }

    public ReadonlyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadonlyException(String message) {
        super(message);
    }

    public ReadonlyException(Throwable cause) {
        super(cause);
    }


}
