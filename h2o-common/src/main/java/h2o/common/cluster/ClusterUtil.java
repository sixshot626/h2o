package h2o.common.cluster;


import h2o.apache.commons.lang.StringUtils;
import h2o.common.lang.SDate;
import h2o.common.lang.tuple.Entry;
import h2o.common.util.id.SnowGarlandIdGen;
import h2o.common.util.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneOffset;

public abstract class ClusterUtil {


    private static final Logger LOG = LoggerFactory.getLogger(ClusterUtil.class);

    private ClusterUtil() {
    }

    public static long getWorkerId() {
        long workerId = Long.parseLong(h2o.jodd.util.SystemUtil.get("H2OWorkerId", "0"));
        LOG.info("SYS PARA - workerId : {}", workerId);
        return workerId;
    }


    public static class IdGenerator {

        private final SnowGarlandIdGen idGen;

        public IdGenerator() {
            this(ClusterUtil.getWorkerId());
        }

        public IdGenerator(long workId) {
            this.idGen = new SnowGarlandIdGen(workId);
        }

        public synchronized String makeId() {
            LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
            String yyyy = Integer.toString(localDate.getYear());
            return StringUtil.build(StringUtils.substring(yyyy, -2) ,
                    idGen.nextKey(new SDate(yyyy + "-01-01", true)));
        }


        public synchronized Entry<LocalDate,String> dateId() {
            LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
            String yyyy = Integer.toString(localDate.getYear());
            return new Entry<>(localDate , idGen.nextKey(new SDate(yyyy + "-01-01", true)));
        }

    }


}
