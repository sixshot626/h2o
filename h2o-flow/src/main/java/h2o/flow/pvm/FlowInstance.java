/**
 * @author 张建伟
 */
package h2o.flow.pvm;

import h2o.flow.pvm.elements.Node;


public interface FlowInstance {	
	
	  Node findStartNode();
	
	  Node findNode(Object id);

}
