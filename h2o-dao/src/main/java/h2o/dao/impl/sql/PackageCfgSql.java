package h2o.dao.impl.sql;

import h2o.common.util.lang.RuntimeUtil;
import h2o.dao.sql.SqlSource;
import org.apache.commons.lang.StringUtils;

public class PackageCfgSql extends PathCfgSql implements SqlSource {


    private static String getSqlPath(Class<?> runClazz) {

        String runClassName = runClazz == null ? RuntimeUtil.getCallClassName(PackageCfgSql.class.getName()) : runClazz.getName();
        if (runClassName == null) {

            throw new RuntimeException("Get class path Exception ");

        } else {

            return StringUtils.replace(StringUtils.substringBeforeLast(runClassName, "."), ".", "/") + "/cfg";

        }
    }


    public PackageCfgSql(String key) {
        super(getSqlPath(null), key);
    }


    public PackageCfgSql(Class<?> runClazz, String key) {
        super(getSqlPath(runClazz), key);
    }


    public PackageCfgSql(PackageCfgSqlHelper helper, String key) {
        super(helper.path, key);
    }


    public static PackageCfgSqlHelper helper() {
        return new PackageCfgSqlHelper(getSqlPath(null));
    }

    public static PackageCfgSqlHelper helper(Class<?> runClazz) {
        return new PackageCfgSqlHelper(getSqlPath(runClazz));
    }


    public static class PackageCfgSqlHelper {

        private final String path;

        private PackageCfgSqlHelper(String path) {
            this.path = path;
        }

    }


}
