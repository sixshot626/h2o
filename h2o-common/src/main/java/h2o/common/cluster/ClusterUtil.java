package h2o.common.cluster;


import h2o.common.Tools;
import h2o.common.lang.SDate;
import h2o.common.util.date.DateUtil;
import h2o.common.util.id.SnowGarlandIdGen;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public abstract class ClusterUtil {

    private ClusterUtil() {}

    public static long getWorkerId() {
        long workerId = Long.parseLong(h2o.jodd.util.SystemUtil.get("H2OWorkerId" , "0"));
        Tools.log.info("SYS PARA - workerId : {}" , workerId );
        return workerId;
    }


    public static class IdGenerator {

        public static final IdGenerator GLOBAL = new IdGenerator();

        private final SnowGarlandIdGen idGen = new SnowGarlandIdGen( ClusterUtil.getWorkerId() );

        public synchronized String makeId() {
            Date cd = new Date();
            String yyyy = DateUtil.toString(cd , "yyyy");
            return StringUtil.build(
                        StringUtils.substring( yyyy , -2 )  ,
                        idGen.nextKey( new SDate( StringUtil.build( yyyy , "-01-01" ) ,true )  )
                    );
        }

    }



}
