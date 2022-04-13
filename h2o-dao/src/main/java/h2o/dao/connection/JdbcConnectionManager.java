package h2o.dao.connection;

import h2o.dao.DbUtil;
import h2o.dao.exception.DaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class JdbcConnectionManager implements ConnectionManager {

    private final String dataSourceName;

    private final AtomicReference<DataSource> dataSourceRef = new AtomicReference<>();

    public JdbcConnectionManager(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public Connection getConnection() {

        DataSource ds = dataSourceRef.get();
        if ( ds == null ) {
            ds = DbUtil.getDataSource( this.dataSourceName );
            dataSourceRef.compareAndSet(null,ds);
        }

        if ( ds == null ) {
            throw new DaoException("DataSource is null");
        }

        try {
            return ds.getConnection();
        } catch ( SQLException e ) {
            throw new DaoException(e);
        }

    }
}
