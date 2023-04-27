package h2o.common.cluster;


import h2o.apache.commons.lang.StringUtils;
import h2o.common.lang.SDate;
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




        private final static char[] LATIN = new char[] {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'};

        public synchronized String make20Id() {
            LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
            String yyyy = Integer.toString(localDate.getYear());
            return StringUtil.build(LATIN[ Integer.parseInt(StringUtils.substring(yyyy, -2)) % LATIN.length ] ,
                    idGen.nextKey(new SDate(yyyy + "-01-01", true)));
        }

    }


}
