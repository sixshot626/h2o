/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

import h2o.flow.pvm.FlowData;
import h2o.flow.pvm.FlowInstance;


public class RunContext {
	
	
	private final FlowInstance flowInstance;

	private FlowData runData;

	private FlowTransactionManager txManager;

	
	
	public RunContext( FlowInstance flowInstance ) {
		this.flowInstance = flowInstance;
	}


	public FlowTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(FlowTransactionManager txManager) {
		this.txManager = txManager;
	}



	public FlowData getRunData() {
		return runData;
	}

	public void setRunData(FlowData runData) {
		this.runData = runData;
	}


	

	public FlowInstance getFlowInstance() {
		return flowInstance;
	}


	public RunContext copy() {

		RunContext nRunContext = new RunContext( this.flowInstance );

		nRunContext.runData = this.runData == null ? null : this.runData.copy();
		nRunContext.txManager = this.txManager;
		

		return nRunContext;
	}

}
