/**
 * @author 张建伟
 */
package h2o.flow.pvm.elements;

import h2o.flow.pvm.FlowException;
import h2o.flow.pvm.runtime.ExecResult;
import h2o.flow.pvm.runtime.RunContext;

public interface Node extends FlowElement {

	ExecResult exec(RunContext runContext , Object... args ) throws FlowException;

}
