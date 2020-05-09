/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

import h2o.flow.pvm.FlowData;
import h2o.flow.pvm.FlowInstance;

public interface FlowTransactionManager {

	Object beginTransaction( RunContext runContext );

	void commit( Object transactionObj );

	void rollBack( Object transactionObj );

}
