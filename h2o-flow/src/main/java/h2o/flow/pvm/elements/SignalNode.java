/**
 * @author 张建伟
 */
package h2o.flow.pvm.elements;

import h2o.flow.pvm.ExecResult;
import h2o.flow.pvm.RunContext;
import h2o.flow.pvm.exception.FlowException;

public interface SignalNode extends Node {

	ExecResult onSignal(RunContext runContext , Object... args ) throws FlowException;
	
}
