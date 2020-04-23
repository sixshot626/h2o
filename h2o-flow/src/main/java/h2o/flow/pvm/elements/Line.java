/**
 * @author 张建伟
 */
package h2o.flow.pvm.elements;

import h2o.flow.pvm.FlowException;
import h2o.flow.pvm.runtime.RunContext;


public interface Line extends FlowElement {

	  Node pass(RunContext runContext) throws FlowException;

}
