package h2o.common.util.id;


public class TimestampSequence {

    public static class Sequence {

        public final long workerId;

        public final long timeId;

        public final long timestamp;

        public final long sequence;

        public Sequence(long workerId , long timeId, long timestamp, long sequence) {
            this.workerId = workerId;
            this.timeId = timeId;
            this.timestamp = timestamp;
            this.sequence = sequence;
        }
    }


    private final long maxTimerId;

    private final long maxSequence;

    private final long workerId;

    private long timerId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    public TimestampSequence(long workerId, long timerId , long maxTimerId, long maxSequence ) {
        this.workerId = workerId;
        this.timerId = timerId;
        this.maxTimerId = maxTimerId;
        this.maxSequence = maxSequence;
    }

    public TimestampSequence(long workerId, long timerId) {
        this.workerId = workerId;
        this.timerId = timerId;
        this.maxTimerId = 128;
        this.maxSequence = 4096;
    }

    public TimestampSequence(long workerId) {
        this(workerId,0);
    }

    public TimestampSequence() {
        this(0,0);
    }

    // ==============================Methods==========================================



    public synchronized Sequence next() {

        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过
        if (timestamp < lastTimestamp) {
            timerId++;
            if ( timerId >= maxTimerId ) {
                timerId = 0;
            }
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence++;
            if ( sequence >= this.maxSequence ) {
                sequence = 0;
            }
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return new Sequence( workerId , timerId , timestamp , sequence );

    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }


    public long getMaxTimerId() {
        return maxTimerId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getTimerId() {
        return timerId;
    }


}