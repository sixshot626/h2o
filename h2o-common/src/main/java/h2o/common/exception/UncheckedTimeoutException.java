package h2o.common.exception;

import java.util.concurrent.TimeoutException;

public class UncheckedTimeoutException extends RuntimeException implements ExceptionWrapper  {

    private static final long serialVersionUID = -1696176887469400139L;

    protected final Throwable cause;

    public UncheckedTimeoutException() {
        this.cause = new TimeoutException();
    }

    public UncheckedTimeoutException(String message) {
        super(message);
        this.cause = new TimeoutException();
    }

    public UncheckedTimeoutException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }

    public UncheckedTimeoutException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}