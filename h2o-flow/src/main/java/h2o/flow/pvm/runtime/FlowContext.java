/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

import h2o.flow.pvm.FlowInstance;
import h2o.flow.pvm.RunContext;


public class FlowContext implements RunContext {


	private final FlowInstance flowInstance;

	private final FlowData runData;

	private Object transactionStatus;

	public FlowContext(FlowInstance flowInstance, FlowData runData ) {
		this.flowInstance = flowInstance;
		this.runData = runData;
	}

	@Override
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

	@Override
	public FlowContext copy() {

		FlowContext runContextCopy = new FlowContext( this.flowInstance ,
				this.runData == null ? null : this.runData.copy());

		runContextCopy.setTransactionStatus( transactionStatus );

		return runContextCopy;

	}

}
