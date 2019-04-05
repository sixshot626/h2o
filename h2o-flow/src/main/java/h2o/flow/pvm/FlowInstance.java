/**
 * @author 张建伟
 */
package h2o.flow.pvm;

import h2o.flow.pvm.elements.Node;


public interface FlowInstance {	
	
	  Node getStartNode();
	
	  Node findNode(Object id);

}
