package h2o.dao.advanced;

import h2o.common.collection.IgnoreCaseMap;
import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.K;
import h2o.common.lang.Val;
import h2o.common.util.collection.MapBuilder;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.jdbc.row.RowData;
import h2o.dao.structure.ColumnMeta;
import h2o.dao.structure.TableStruct;
import h2o.dao.structure.TableStructParser;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CleverDao {

    private final String dataSourceName;

    private final Dao dao;

    private final Class<?> entityClazz;

    private final TableStruct tableStruct;

    public CleverDao(Class<?> entityClazz) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
        this.entityClazz = entityClazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz);
    }

    public CleverDao(String dataSourceName, Class<?> entityClazz) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.entityClazz = entityClazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz);
    }

    public CleverDao(Dao dao, Class<?> entityClazz) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = dao;
        this.entityClazz = entityClazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz);
    }


    public static final K ALL = new K("all");
    public static final K AUTO = new K("auto");

    public static final String ASC = "asc";
    public static final String DESC = "desc";


    private List<ColumnMeta> filterCols(Object ks) {

        if (ks == null) {
            throw new IllegalArgumentException(String.valueOf(ks));
        }

        if (ALL.equals(ks)) {
            return this.getTableStruct().columns();
        }

        if (ks instanceof Collection) {
            return this.getTableStruct().filterColumns( (Collection<Object>) ks);
        }

        throw new IllegalArgumentException(String.valueOf(ks));

    }


    protected String buildInsertSql(Object ks) {

        StringBuilder sqlInsert = new StringBuilder("insert into ").append(tableName()).append(" ( ");
        StringBuilder sqlValues = new StringBuilder(" ) \n        values ( ");

        int i = 0;
        for (ColumnMeta col : filterCols(ks)) {
            if (i++ > 0) {
                sqlInsert.append(" , ");
                sqlValues.append(" , ");
            }
            sqlInsert.append(col.colName);
            sqlValues.append(col.value());
        }

        return sqlInsert.append(sqlValues).append(" )").toString();

    }


    protected String buildDelSql1() {
        return str("delete from ", this.tableName());
    }


    protected String buildUpdateSql1(Collection<?> ks) {

        StringBuilder sqlUpdate = new StringBuilder("update ").append(tableName()).append(" set ");

        int i = 0;
        for (ColumnMeta col : filterCols(ks)) {
            if (i++ > 0) {
                sqlUpdate.append(" , ");
            }
            sqlUpdate.append("\n        ");
            sqlUpdate.append(col.colName);
            sqlUpdate.append(" = ");
            sqlUpdate.append(col.value());
        }

        return sqlUpdate.toString();

    }


    protected String buildSelectSql1(Object ks) {

        StringBuilder sqlSelect = new StringBuilder("select  ");

        List<ColumnMeta> cols = this.getTableStruct().columns();

        if (ks == null || ALL.equals(ks)) {
            int i = 0;
            for (ColumnMeta col : cols) {
                if (i++ > 0) {
                    sqlSelect.append(" ,\n        ");
                }
                sqlSelect.append(col.colName);
            }

        } else if (ks instanceof Collection) {

            int i = 0;
            for (Object k : (Collection) ks) {
                if (i++ > 0) {
                    sqlSelect.append(" ,\n        ");
                }
                if (k instanceof C) {
                    sqlSelect.append(((C) k).value);
                } else {
                    sqlSelect.append(column(k));
                }

            }

        } else {
            throw new IllegalArgumentException(String.valueOf(ks));
        }

        return sqlSelect.append("\nfrom ").append(this.tableName()).toString();

    }


    protected String buildOrderSql(List<?> oks) {

        if (oks == null || oks.isEmpty()) {
            return "";
        }

        StringBuilder sqlOrder = new StringBuilder("order by ");

        for (int i = 0, len = oks.size(); i < len; i += 2) {
            if (i > 0) {
                sqlOrder.append(" , ");
            }
            Object f = oks.get(i);
            Object d = oks.get(i + 1);

            if (f instanceof String) {
                sqlOrder.append(f);
            } else {
                sqlOrder.append(column(f));
            }

            if (d != null) {
                sqlOrder.append(" ");
                sqlOrder.append(name(d));
            }

        }

        return sqlOrder.toString();

    }


    protected String buildWhereF(Function<?, String> f, Collection<?> ws) {

        if (ws == null || ws.isEmpty()) {
            throw new IllegalArgumentException(String.valueOf(ws));
        }

        StringBuilder sqlWhere = new StringBuilder("where   ");
        int i = 0;
        for (Object k : ws) {
            if (i++ > 0) {
                sqlWhere.append("\n    and ");
            }
            if (k instanceof C) {
                sqlWhere.append(((C) k).value);
            } else {
                sqlWhere.append(column(k));
                sqlWhere.append(" = ");
                sqlWhere.append(((Function<Object, String>) f).apply(k));
            }
        }

        return sqlWhere.toString();

    }

    protected String buildWhere(Collection<?> ws) {
        return buildWhereF(k -> str(":", name(k)), ws);
    }

    protected String buildWhereW(Collection<?> ws) {
        return buildWhereF(k -> str(":w__", name(k)), ws);
    }

    protected Map<String, Object> convWArgs(Map<?, Object> wargs) {
        Map<String, Object> nmap = new HashMap<>();
        for (Map.Entry<?, Object> entry : wargs.entrySet()) {
            nmap.put(str("w__", name(entry.getKey())), entry.getValue());
        }
        return nmap;
    }

    protected Object[] merge(Object[] a, Object... b) {
        Object[] c = new Object[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    private Map<String, String> colAttrMap;

    public Map<String, String> getColAttrMap() {
        if (this.colAttrMap == null) {
            Map<String, String> caMap = new IgnoreCaseMap<>(new HashMap<>(), IgnoreCaseMap.LOWER);
            for (ColumnMeta col : this.tableStruct.columns()) {
                caMap.put(col.colName, col.attrName);
            }
            this.colAttrMap = Collections.unmodifiableMap(caMap);
        }
        return colAttrMap;
    }

    protected Map<String, Object> orm(Map<String, Object> row) {

        Map<String, String> caMap = getColAttrMap();

        Map<String, Object> nRow = new RowData(new HashMap<>());
        for (Map.Entry<String, Object> re : row.entrySet()) {
            String key = caMap.get(re.getKey());
            if (key != null) {
                nRow.put(key, re.getValue());
            } else {
                nRow.put(re.getKey(), re.getValue());
            }
        }

        return nRow;
    }

    //////////////////////////////////////////////////

    protected String selectSql(Options options) {
        return buildSelectSql1(options.attr);
    }


    protected String lockSql(Options options) {
        if (options.lock == null) {
            return null;
        } else if (options.lock instanceof String) {
            return str(" \n", options.lock);
        } else if (options.lock instanceof Boolean) {
            if (((Boolean) options.lock).booleanValue()) {
                return " \nfor update";
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException(String.valueOf(options.lock));
        }

    }


    protected String whereSql(Options options) {
        if (options.where != null) {
            return str(" \nwhere   ", options.where);
        } else if (options.wattr != null) {
            if (ALL.equals(options.wattr)) {
                return null;
            } else {
                return str(" \n", buildWhere((List<Object>) options.wattr));
            }
        } else if (options.wargs != null) {
            return str(" \n", buildWhereW(((Map) options.wargs).keySet()));
        } else {
            throw new IllegalArgumentException("where");
        }
    }


    protected String orderSql(Options options) {
        if (options.orderby == null) {
            return null;
        }
        return str(" \n", buildOrderSql((List<?>) options.orderby));
    }


    protected Object[] margeWhereArgs(Options options, Object[] para) {
        if (options.where != null) {
            return para;
        } else if (options.wattr != null) {
            return para;
        } else if (options.wargs != null) {
            if (options.wargs instanceof Map) {
                return merge(para, convWArgs((Map<?, Object>) options.wargs));
            } else {
                throw new IllegalArgumentException("wargs");
            }
        } else {
            throw new IllegalArgumentException("where");
        }
    }


    protected static class Options {

        public Object attr;
        public Object wattr;
        public String where;
        public Object wargs;

        private Object lock;

        public Object orderby;


        private static Object aArgs(Object[] args) {
            if (args == null) {
                return null;
            }
            if (args.length == 1 && (args[0] == null || args[0] instanceof K)) {
                return args[0];
            } else {
                return Collections.unmodifiableList(Arrays.asList(args));
            }
        }

        private static Object mArgs(Object[] args) {
            if (args == null) {
                return null;
            }
            if (args.length == 1 && (args[0] == null || args[0] instanceof K)) {
                return args[0];
            } else {
                if (args.length % 2 != 0) {
                    throw new IllegalArgumentException("an even number of args");
                }
                Map m = new HashMap<>();
                for (int i = 0, len = args.length; i < len; i += 2) {
                    m.put(args[i], args[i + 1]);
                }
                return Collections.unmodifiableMap(m);
            }
        }


        public void setAttr(Object[] attrs) {
            this.attr = aArgs(attrs);
        }

        public void setWattr(Object[] wattrs) {
            this.wattr = aArgs(wattrs);
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public void setWargs(Object[] wargs) {
            this.wargs = mArgs(wargs);
        }

        public void setLock(Object lock) {
            this.lock = lock;
        }

        public void setOrderby(Object[] orderby) {
            this.orderby = aArgs(orderby);
        }

    }


    public class Query {
        protected Query() {
        }

        private final Options options = new Options();

        public Query attr(Object... attrs) {
            this.options.setAttr(attrs);
            return this;
        }

        public Query lock() {
            this.options.setLock(true);
            return this;
        }

        public Query lock(boolean lock) {
            this.options.setLock(lock);
            return this;
        }

        public Query lock(String lock) {
            this.options.setLock(lock);
            return this;
        }

        public Query wattr(Object... wattrs) {
            this.options.setWattr(wattrs);
            return this;
        }

        public Query wargs(Object... wargs) {
            this.options.setWargs(wargs);
            return this;
        }

        public Query where(String where) {
            this.options.setWhere(where);
            return this;
        }

        public Query orderBy(Object... orders) {
            this.options.setOrderby(orders);
            return this;
        }

        public Val<Map<String, Object>> selectOne(Object... para) {

            String sql = str(selectSql(options),
                    whereSql(options),
                    lockSql(options));

            Val<Map<String, Object>> res = getDao().get(sql, margeWhereArgs(options, para));

            if (res.isPresent()) {
                return new Val<>(orm(res.getValue()));
            } else {
                return res;
            }

        }

        public List<Map<String, Object>> select(Object... para) {

            String sql = str(selectSql(options),
                    whereSql(options),
                    orderSql(options));

            List<Map<String, Object>> res = getDao().load(sql, margeWhereArgs(options, para));

            if (res.isEmpty()) {
                return res;
            } else {
                return res.stream().map(row -> orm(row)).collect(Collectors.toList());
            }
        }

        public Page<Map<String, Object>> pageSelect(PageRequest pageRequest, Object... para) {

            if (pageRequest.getSorts() != null) {
                List<Object> orderBy = new ArrayList<>();
                for (SortInfo sortInfo : pageRequest.getSorts()) {
                    orderBy.add(sortInfo.getName());
                    orderBy.add(sortInfo.getDirection());
                }
                if (!orderBy.isEmpty()) {
                    this.orderBy(orderBy);
                }
            }

            String sql = str(selectSql(options),
                    whereSql(options),
                    orderSql(options));

            Page<Map<String, Object>> res = getDao().pagingLoad(sql, pageRequest, margeWhereArgs(options, para));

            if (res.getContent().isEmpty()) {
                return res;
            } else {
                List<Map<String, Object>> data = new ArrayList<>(res.getContent().size());
                for (Map<String, Object> row : res.getContent()) {
                    data.add(orm(row));
                }
                res.setContent(data);
                return res;
            }
        }
    }


    public Query query() {
        return new Query();
    }

    //////////////////////////////////////////////////

    public class Update {

        protected Update() {
        }

        private final Options options = new Options();

        public Update attr(Object... attrs) {
            this.options.setAttr(attrs);
            return this;
        }


        public Update wattr(Object... wattrs) {
            this.options.setWattr(wattrs);
            return this;
        }

        public Update wargs(Object... wargs) {
            this.options.setWargs(wargs);
            return this;
        }

        public Update where(String where) {
            this.options.setWhere(where);
            return this;
        }


        public int add(Object... para) {

            if (para == null || para.length == 0 || para[0] == null) {
                throw new IllegalArgumentException("data is empty");
            }

            String sql;
            if (options.attr == null || AUTO.equals(options.attr)) {
                sql = buildInsertSql(DbUtil.DBFACTORY.getArgProcessor().proc(para).keySet());
            } else {
                sql = buildInsertSql(options.attr);
            }

            return getDao().update(sql, para);

        }

        public int[] batAdd(List<Map> cell, Object... para) {

            if (cell == null || cell.isEmpty()) {
                throw new IllegalArgumentException("data is empty");
            }

            Map paraMap = new HashMap<>();
            if (para != null && para.length > 0 && para[0] != null) {
                paraMap = DbUtil.DBFACTORY.getArgProcessor().proc(para);
            }

            String sql;
            if (options.attr == null || AUTO.equals(options.attr)) {
                Set<Object> ks = MapBuilder.start().putAll(cell.get(0)).putAll(paraMap).get().keySet();
                sql = buildInsertSql(ks);
            } else {
                sql = buildInsertSql(options.attr);
            }

            List data;
            if (paraMap.isEmpty()) {
                data = cell;
            } else {
                data = new ArrayList<>(cell.size());
                for (Map row : cell) {
                    data.add(MapBuilder.start().putAll(row).putAll(paraMap).get());
                }
            }

            return getDao().batchUpdate(sql, data);

        }


        public int edit(Object... para) {

            String updSql;
            if (options.attr == null || AUTO.equals(options.attr)) {
                if (para == null || para.length == 0 || para[0] == null) {
                    throw new IllegalArgumentException("data is empty");
                }
                updSql = buildUpdateSql1(DbUtil.DBFACTORY.getArgProcessor().proc(para).keySet());
            } else {
                updSql = buildUpdateSql1((Collection<?>) options.attr);
            }

            String sql = str(updSql, whereSql(options));

            return getDao().update(sql, para);

        }

        public int[] batEdit(List<Map> cell, Object... para) {

            if (cell == null || cell.isEmpty()) {
                throw new IllegalArgumentException("data is empty");
            }

            if (options.wargs != null) {
                throw new IllegalArgumentException("batch edit not support argument: wargs");
            }

            String sql = str(buildUpdateSql1((Collection<?>) options.attr), whereSql(options));

            Map paraMap = new HashMap<>();
            if (para != null && para.length > 0 && para[0] != null) {
                paraMap = DbUtil.DBFACTORY.getArgProcessor().proc(para);
            }

            List data;
            if (paraMap.isEmpty()) {
                data = cell;
            } else {
                data = new ArrayList<>(cell.size());
                for (Map row : cell) {
                    data.add(MapBuilder.start().putAll(row).putAll(paraMap).get());
                }
            }

            return getDao().batchUpdate(sql, data);

        }


        public int del(Object... para) {
            String sql = str(buildDelSql1(), whereSql(options));
            return getDao().update(sql, para);
        }

    }


    public Update update() {
        return new Update();
    }


    //////////////////////////////////////////////////
    // util


    private String str(Object... strs) {
        StringBuilder sb = new StringBuilder();
        for (Object s : strs) {
            if (s != null) {
                sb.append(name(s));
            }
        }
        return sb.toString();
    }

    protected String name(Object obj) {
        String key;
        if (obj instanceof String) {
            key = (String) obj;
        } else if (obj instanceof K) {
            key = ((K) obj).name();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
        return key;
    }

    protected ColumnMeta getColumnMeta(Object k) {
        return this.tableStruct.getColumn(k);
    }


    ////


    protected Class<?> getEntityClazz() {
        return entityClazz;
    }

    protected TableStruct getTableStruct() {
        return this.tableStruct;
    }

    protected Dao getDao() {
        return dao == null ? DbUtil.getDao(this.dataSourceName) : dao;
    }


    public String tableName() {
        return this.getTableStruct().tableName();
    }

    public String column(Object attr) {
        return this.getColumnMeta(attr).colName;
    }


    public static final class C {

        public final String value;

        public C(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static C c(String str) {
            return new C(str);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            C c = (C) o;
            return value.equals(c.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
