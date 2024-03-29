package h2o.common.schedule;


enum TaskState {
    Ok, Free, Continue, Break, Wait, Sleep
}


public class TaskResult implements java.io.Serializable {

    private static final long serialVersionUID = 7956881167239663534L;

    public static final TaskResult OK = new TaskResult(TaskState.Ok);
    public static final TaskResult FREE = new TaskResult(TaskState.Free);
    public static final TaskResult CONTINUE = new TaskResult(TaskState.Continue);
    public static final TaskResult BREAK = new TaskResult(TaskState.Break);
    public static final TaskResult WAIT = new TaskResult(TaskState.Wait);


    public static TaskResult SLEEP(long time) {
        TaskResult tr = new TaskResult(TaskState.Sleep);
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
