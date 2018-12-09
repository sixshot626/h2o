package h2o.common.schedule;

public class TaskResult {

    public static final TaskResult Ok   = new TaskResult( TaskState.Ok );
    public static final TaskResult Free = new TaskResult( TaskState.Free );
    public static final TaskResult Continue = new TaskResult( TaskState.Continue );
    public static final TaskResult Break = new TaskResult( TaskState.Break );
    public static final TaskResult Wait = new TaskResult( TaskState.Wait );

    public static TaskResult sleep( long time ) {
        TaskResult tr = new TaskResult( TaskState.Sleep );
        tr.sleepTime = time;
        return tr;
    }

    public final TaskState taskState;

    private long sleepTime;

    private TaskResult(TaskState taskState) {
        this.taskState = taskState;
    }

    public long getSleepTime() {
        return sleepTime;
    }


}
