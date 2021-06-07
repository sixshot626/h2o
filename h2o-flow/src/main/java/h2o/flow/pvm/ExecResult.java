package h2o.flow.pvm;

import h2o.common.lang.Val;
import h2o.common.util.collection.ListBuilder;
import h2o.flow.pvm.elements.Line;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExecResult {

    private final RunStatus status;

    private final List<Line> lines;

    private final Val<Object> result;

    public ExecResult( Object result , RunStatus status ) {
        this.status = status;
        this.lines = Collections.emptyList();
        this.result = new Val<>(result);
    }

    public ExecResult( Object result , RunStatus status, Line... lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newList(lines));
        this.result = new Val<>(result);
    }

    public ExecResult( Object result  , RunStatus status, Collection<Line> lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newListAndAddAll( lines ) );
        this.result = new Val<>(result);
    }


    public ExecResult(RunStatus status) {
        this.status = status;
        this.lines = Collections.emptyList();
        this.result = Val.empty();
    }

    public ExecResult(RunStatus status, Line... lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newList(lines));
        this.result = Val.empty();
    }

    public ExecResult(RunStatus status, Collection<Line> lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newListAndAddAll( lines ) );
        this.result = Val.empty();
    }


    public static ExecResult pause() {
        return new ExecResult( RunStatus.PAUSE );
    }

    public static ExecResult end() {
        return new ExecResult( RunStatus.END );
    }

    public static ExecResult exception() {
        return new ExecResult( RunStatus.EXCEPTION );
    }

    public static ExecResult goOn( Line... lines ) {
        return new ExecResult( RunStatus.RUNNING , lines );
    }

    public static ExecResult goOn( Collection<Line> lines ) {
        return new ExecResult( RunStatus.RUNNING , lines );
    }


    public RunStatus getStatus() {
        return status;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Val<Object> getResult() {
        return result;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExecResult{");
        sb.append("status=").append(status);
        sb.append(", lines=").append(lines);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

}
