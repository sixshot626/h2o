package h2o.dao.exception;

public class DaoException extends RuntimeException {


    private static final long serialVersionUID = -8658199600771967115L;

    public DaoException() {
        super();
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }


}
