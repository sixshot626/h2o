/**
 * @author 张建伟
 */
package h2o.flow.pvm.runtime;

import h2o.flow.pvm.ExecResult;
import h2o.flow.pvm.RunContext;
import h2o.flow.pvm.elements.Line;
import h2o.flow.pvm.elements.Node;

public interface ProcessRunListener {
	
	void onStart(RunContext runContext, Node node , boolean signal , Object[] args);
	
	void enterNode(RuntimeScopeObject nodeRunScoeObject , RunContext runContext, Node node , boolean signal , Object[] args );
	
	void leaveNode(RuntimeScopeObject nodeRunScoeObject , RunContext runContext, Node node , ExecResult result );
	
	void enterLine(RuntimeScopeObject lineRunScoeObject , RunContext runContext, Line line , Object[] args );

	void leaveLine(RuntimeScopeObject lineRunScoeObject , RunContext runContext, Line line , Node target );

	void onEnd( RunContext runContext, ExecResult result );
	
	void onException( RunContext runContext, Throwable e );

}
