package h2o.dao.exception;

import java.sql.SQLException;

public class DaoException extends RuntimeException {

    private static final long serialVersionUID = -8658199600771967115L;

    private final SQLException sqlException;


    public DaoException() {
        super();
        this.sqlException = null;
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
        this.sqlException = getSQLException( cause );
    }

    public DaoException(String message) {
        super(message);
        this.sqlException = null;
    }

    public DaoException(Throwable cause) {
        super(cause);
        this.sqlException = getSQLException( cause );
    }


    public SQLException getSqlException() {
        return sqlException;
    }

    private static SQLException getSQLException(Throwable throwable ) {

        while (throwable != null) {
            if (throwable instanceof SQLException ) {
                return (SQLException) throwable;
            }
            if ( throwable == throwable.getCause() ) {
                break;
            }
            throwable = throwable.getCause();
        }
        return null;
    }


}
