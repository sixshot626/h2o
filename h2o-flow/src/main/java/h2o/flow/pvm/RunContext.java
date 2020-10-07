/**
 * @author 张建伟
 */
package h2o.flow.pvm;

public interface RunContext {

	FlowInstance getFlowInstance();

	RunContext copy();

}
