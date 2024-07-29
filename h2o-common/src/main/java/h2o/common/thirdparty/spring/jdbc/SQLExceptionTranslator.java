package h2o.common.thirdparty.spring.jdbc;

import h2o.common.lang.Val;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodes;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class SQLExceptionTranslator {

    private final MyTranslator translator;

    public SQLExceptionTranslator(DataSource dataSource) {
        this.translator = new MyTranslator(dataSource);
    }

    public SQLExceptionTranslator(String dbName) {
        this.translator = new MyTranslator(dbName);
    }

    public SQLExceptionTranslator(SQLErrorCodes sec) {
        this.translator = new MyTranslator(sec);
    }


    public Val<DataAccessException> translate(Throwable throwable) {

        SQLException sqlEx = getSQLException( throwable );
        if ( sqlEx == null ) {
            return Val.empty();
        }

        return new Val(translator.myTranslate( sqlEx  ));

    }


    private SQLException getSQLException( Throwable throwable ) {
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


    private static class MyTranslator extends SQLErrorCodeSQLExceptionTranslator {

        public MyTranslator() {
        }

        public MyTranslator(DataSource dataSource) {
            super(dataSource);
        }

        public MyTranslator(String dbName) {
            super(dbName);
        }

        public MyTranslator(SQLErrorCodes sec) {
            super(sec);
        }

        @Override
        protected String buildMessage(String task, String sql, SQLException ex) {
            return ex.getMessage();
        }

        public DataAccessException myTranslate(SQLException sqlEx) {
            return super.doTranslate( "" , null , sqlEx);
        }

    }


}
