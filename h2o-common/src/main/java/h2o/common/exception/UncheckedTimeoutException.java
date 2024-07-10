package h2o.common.exception;

public class UncheckedTimeoutException extends RuntimeException  {

    private static final long serialVersionUID = -1696176887469400139L;

    public UncheckedTimeoutException() {
    }

    public UncheckedTimeoutException(String message) {
        super(message);
    }

    public UncheckedTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncheckedTimeoutException(Throwable cause) {
        super(cause);
    }


}