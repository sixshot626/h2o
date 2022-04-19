package h2o.common.cluster;


import h2o.common.Tools;
import h2o.common.lang.SDate;
import h2o.common.util.id.SnowGarlandIdGen;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;

public abstract class ClusterUtil {

    private ClusterUtil() {
    }

    public static long getWorkerId() {
        long workerId = Long.parseLong(h2o.jodd.util.SystemUtil.get("H2OWorkerId", "0"));
        Tools.log.info("SYS PARA - workerId : {}", workerId);
        return workerId;
    }


    public static class IdGenerator {

        private final SnowGarlandIdGen idGen = new SnowGarlandIdGen(ClusterUtil.getWorkerId());

        public synchronized String makeId() {
            LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
            String yyyy = Integer.toString(localDate.getYear());
            return StringUtils.substring(yyyy, -2) +
                    idGen.nextKey(new SDate(yyyy + "-01-01", true));
        }

    }


}
