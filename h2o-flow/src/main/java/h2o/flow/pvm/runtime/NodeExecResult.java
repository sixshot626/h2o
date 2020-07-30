package h2o.flow.pvm.runtime;

import h2o.common.collections.builder.ListBuilder;
import h2o.flow.pvm.elements.Line;

import java.util.Collections;
import java.util.List;

public class NodeExecResult implements java.io.Serializable {

    private final RunStatus status;

    private final List<Line> lines;

    public NodeExecResult( RunStatus status, Line line ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newList(line));
    }

    public NodeExecResult( RunStatus status, List<Line> lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(lines);
    }

    public RunStatus getStatus() {
        return status;
    }

    public List<Line> getLines() {
        return lines;
    }
}
