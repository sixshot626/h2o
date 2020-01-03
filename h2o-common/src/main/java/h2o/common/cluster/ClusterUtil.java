package h2o.common.cluster;


import h2o.common.Tools;
import h2o.common.lang.SDate;
import h2o.common.util.date.DateUtil;
import h2o.common.util.id.SnowGarlandIdGen;

import java.util.Date;

public abstract class ClusterUtil {

    private ClusterUtil() {}

    public static long getWorkerId() {
        long workerId = Long.parseLong(h2o.jodd.util.SystemUtil.get("workerId" , "0"));
        Tools.log.info("SYS PARA - workerId : {}" , workerId );
        return workerId;
    }


    public static class IdGenerator {

        private static final SnowGarlandIdGen idGen = new SnowGarlandIdGen( ClusterUtil.getWorkerId() );

        public static String makeId() {
            Date cd = new Date();
            return DateUtil.toString(cd , "yy") + idGen.nextKey( new SDate( DateUtil.toString(cd , "yyyy") + "-01-01" ) );
        }

    }

}
