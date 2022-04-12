package h2o.dao.transaction;


import javax.sql.DataSource;
import java.sql.Connection;

public interface ScopeManager {

	String getDataSourceName();
	DataSource getDataSource();

	void setTransactionIsolationLevel(Integer transactionIsolationLevel);

	Connection openConnection();

	Object beginScope();
	
	void endScope(Object scopeObj);

}
