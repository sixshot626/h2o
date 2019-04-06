/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

import h2o.flow.pvm.FlowData;
import h2o.flow.pvm.FlowInstance;


public class RunContext {
	
	
	private final FlowInstance flowInstance;

	private final FlowData runData;

	private final Object transactionStatus;


	public RunContext( FlowInstance flowInstance, FlowData runData, Object transactionStatus ) {
		this.flowInstance = flowInstance;
		this.runData = runData;
		this.transactionStatus = transactionStatus;
	}

	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public FlowData getRunData() {
		return runData;
	}

	public Object getTransactionStatus() {
		return transactionStatus;
	}

	public RunContext copy() {

		return new RunContext( this.flowInstance ,
				this.runData == null ? null : this.runData.copy(),
				this.transactionStatus );

	}

}
