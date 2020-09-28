package h2o.dao.transaction;

import h2o.dao.Dao;

public interface ScopeManager {

	Object beginScope(Dao dao);
	
	void endScope(Object scopeObj);
	
}
