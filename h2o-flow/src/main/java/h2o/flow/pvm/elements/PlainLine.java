package h2o.flow.pvm.elements;

import h2o.flow.pvm.RunContext;
import h2o.flow.pvm.exception.FlowException;

public class PlainLine implements Line , java.io.Serializable {

    private final String targetNodeId;

    public PlainLine(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    @Override
    public Node pass(RunContext runContext, Object... args) throws FlowException {
        return runContext.getFlowInstance().findNode( this.targetNodeId );
    }
}
