package h2o.common.schedule;

public class TaskResult {

    public final TaskState taskState;

    private long sleepTime;

    public TaskResult(TaskState taskState) {
        this.taskState = taskState;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public TaskResult setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }
}
