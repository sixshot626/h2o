/**
 * @author 张建伟
 */
package h2o.flow.pvm.elements;

import h2o.flow.pvm.RunContext;
import h2o.flow.pvm.exception.FlowException;


public interface Line extends FlowElement {

	  Node pass(RunContext runContext) throws FlowException;

}
