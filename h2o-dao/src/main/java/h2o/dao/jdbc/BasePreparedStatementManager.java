package h2o.dao.jdbc;

import h2o.common.lang.SNumber;
import h2o.jenkov.db.impl.PreparedStatementManagerBase;
import h2o.jenkov.db.itf.IPreparedStatementManager;
import h2o.jenkov.db.itf.PersistenceException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BasePreparedStatementManager extends PreparedStatementManagerBase implements IPreparedStatementManager {

    private final SNumber fetchSize;

    private final SNumber timeout;


    public BasePreparedStatementManager(SNumber fetchSize , SNumber timeout) {
        this.fetchSize = fetchSize;
        this.timeout = timeout;
    }

    public BasePreparedStatementManager(SNumber fetchSize , SNumber timeout , Object[] parameters) {
        super(parameters);
        this.fetchSize = fetchSize;
        this.timeout = timeout;
    }

    @Override
    public void init(PreparedStatement statement) throws SQLException, PersistenceException {
        super.init(statement);
        if ( this.isQuery && fetchSize.isPresent() ) {
            statement.setFetchSize( fetchSize.intValue() );
        }
        if ( timeout.isPresent() ) {
            statement.setQueryTimeout( timeout.intValue() );
        }

    }


}
