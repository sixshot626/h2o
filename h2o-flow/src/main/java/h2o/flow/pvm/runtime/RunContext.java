/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

import h2o.flow.pvm.FlowData;
import h2o.flow.pvm.FlowInstance;


public class RunContext {
	
	
	private final FlowInstance flowInstance;

	private final FlowData runData;

	private Object transactionStatus;


	public RunContext( FlowInstance flowInstance, FlowData runData ) {
		this.flowInstance = flowInstance;
		this.runData = runData;
	}

	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public FlowData getRunData() {
		return runData;
	}

	public void setTransactionStatus(Object transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Object getTransactionStatus() {
		return transactionStatus;
	}

	public RunContext copy() {

		RunContext runContextCopy = new RunContext( this.flowInstance ,
				this.runData == null ? null : this.runData.copy());

		runContextCopy.setTransactionStatus( transactionStatus );

		return runContextCopy;

	}

}
