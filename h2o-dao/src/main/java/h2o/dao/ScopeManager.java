package h2o.dao;

public interface ScopeManager {

	Object beginScope(Dao dao);
	
	void endScope(Object scopeObj);
	
}
