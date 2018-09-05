/**
 * @author 张建伟
 */
package h2o.flow.pvm.elements;

import h2o.common.collections.tuple.Tuple2;
import h2o.flow.pvm.FlowException;
import h2o.flow.pvm.runtime.RunContext;
import h2o.flow.pvm.runtime.RunStatus;

import java.util.List;

public interface SignalNode extends Node {

	Tuple2<RunStatus , List<Line>> signal(RunContext runContext) throws FlowException;
	
}
