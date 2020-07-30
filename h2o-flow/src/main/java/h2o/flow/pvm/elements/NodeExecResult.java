package h2o.flow.pvm.elements;

import h2o.flow.pvm.runtime.RunStatus;

import java.util.List;

public class NodeExecResult implements java.io.Serializable {

    private final RunStatus status;

    private final List<Line> lines;

    public NodeExecResult(RunStatus status, List<Line> lines) {
        this.status = status;
        this.lines = lines;
    }

    public RunStatus getStatus() {
        return status;
    }

    public List<Line> getLines() {
        return lines;
    }
}
