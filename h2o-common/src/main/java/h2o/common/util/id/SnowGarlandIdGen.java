package h2o.common.util.id;


import h2o.common.lang.SDate;
import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;


public class SnowGarlandIdGen {

    private SDate cyclicSpace;

    private MiniSnowflakeIdGen idGen;

    private final long workerId;

    public SnowGarlandIdGen( long workerId) {
        this.workerId = workerId;

        if (workerId > 1023 || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than 1023 or less than 0");
        }
    }


    public String nextKey( SDate cyclicSpace ) {
        return nextKey(cyclicSpace , 19);
    }

    public String nextKey( SDate cyclicSpace ,  int n ) {
        return StringUtils.leftPad( Long.toString( nextId(cyclicSpace) ) ,  n , '0' );
    }

    public synchronized long nextId( SDate cyclicSpace ) {

        if ( idGen == null || !cyclicSpace.equals( this.cyclicSpace ) ) {
            this.cyclicSpace = cyclicSpace;

            long twepoch = DateUtil.toDate( cyclicSpace.get() ).getTime() -  ( 1000L * 24 * 60 * 60 * 397  );
            this.idGen = new MiniSnowflakeIdGen( twepoch , this.workerId );
        }

        return this.idGen.nextId();

    }


    public long getWorkerId() {
        return workerId;
    }
}