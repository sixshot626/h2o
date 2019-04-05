/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

public interface FlowTransactionManager {
	
	void beginTransaction();
	
	void rollBack();

	void commit();	

}
