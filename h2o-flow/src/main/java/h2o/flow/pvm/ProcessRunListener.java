/**
 * @author 张建伟
 */
package h2o.flow.pvm;

import h2o.flow.pvm.elements.Line;
import h2o.flow.pvm.elements.Node;
import h2o.flow.pvm.runtime.ExecResult;
import h2o.flow.pvm.runtime.NodeRunScoeObject;
import h2o.flow.pvm.runtime.RunContext;

public interface ProcessRunListener {
	
	void onStart(RunContext runContext, Node node , boolean signal , Object[] args);
	
	void enterNode( NodeRunScoeObject nodeRunScoeObject , RunContext runContext, Node node , boolean signal , Object[] args );
	
	void leaveNode( NodeRunScoeObject nodeRunScoeObject , RunContext runContext, Node node , ExecResult result );
	
	void passLine( RunContext runContext, Line line );
	
	void onEnd( RunContext runContext, ExecResult result );
	
	void onException( RunContext runContext, Throwable e );

}
