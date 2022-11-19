package h2o.dao.jdbc;

import h2o.jenkov.db.impl.PreparedStatementManagerBase;
import h2o.jenkov.db.itf.IPreparedStatementManager;
import h2o.jenkov.db.itf.PersistenceException;
import h2o.common.lang.SNumber;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BasePreparedStatementManager extends PreparedStatementManagerBase implements IPreparedStatementManager {

    private final SNumber fetchSize;


    public BasePreparedStatementManager(SNumber fetchSize) {
        this.fetchSize = fetchSize;
    }

    public BasePreparedStatementManager(SNumber fetchSize , Object[] parameters) {
        super(parameters);
        this.fetchSize = fetchSize;
    }

    @Override
    public Object execute(PreparedStatement statement) throws SQLException, PersistenceException {

        if ( fetchSize.isPresent() ) {
            statement.setFetchSize( fetchSize.intValue() );
        }
        return super.execute(statement);
    }
}
