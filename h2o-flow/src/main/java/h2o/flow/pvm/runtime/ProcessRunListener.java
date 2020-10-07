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
	
	void enterNode(NodeRunScopeObject nodeRunScoeObject , RunContext runContext, Node node , boolean signal , Object[] args );
	
	void leaveNode(NodeRunScopeObject nodeRunScoeObject , RunContext runContext, Node node , ExecResult result );
	
	void passLine( RunContext runContext, Line line );
	
	void onEnd( RunContext runContext, ExecResult result );
	
	void onException( RunContext runContext, Throwable e );

}
