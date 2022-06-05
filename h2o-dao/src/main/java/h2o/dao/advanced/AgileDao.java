package h2o.dao.advanced;

import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.K;
import h2o.common.lang.S;
import h2o.common.lang.Val;
import h2o.common.util.collection.MapBuilder;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.result.RowData;
import h2o.dao.structure.ColumnMeta;
import h2o.dao.structure.TableStruct;
import h2o.dao.structure.TableStructParser;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AgileDao {

    private final String dataSourceName;
    private final Dao dao;
    private final TableStruct tableStruct;


    public AgileDao(Class<?> entityClazz) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
        this.tableStruct = TableStructParser.parse(entityClazz);
    }

    public AgileDao(String dataSourceName, Class<?> entityClazz) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.tableStruct = TableStructParser.parse(entityClazz);
    }

    public AgileDao(Dao dao, Class<?> entityClazz) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = dao;
        this.tableStruct = TableStructParser.parse(entityClazz);
    }

    public AgileDao(TableStruct tableStruct) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
        this.tableStruct = tableStruct;
    }

    public AgileDao(String dataSourceName, TableStruct tableStruct) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.tableStruct = tableStruct;
    }

    public AgileDao(Dao dao, TableStruct tableStruct) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = dao;
        this.tableStruct = tableStruct;
    }






    public static final String ASC  = "asc";
    public static final String DESC = "desc";


    private String buildInsertSql(Collection<?> ks) {

        StringBuilder sqlInsert = new StringBuilder("insert into ").append(tableName()).append(" ( ");
        StringBuilder sqlValues = new StringBuilder(" ) \n        values ( ");

        int i = 0;
        for (ColumnMeta col : this.tableStruct.sort( this.tableStruct.filterColumns(ks)) ) {
            if (i++ > 0) {
                sqlInsert.append(" , ");
                sqlValues.append(" , ");
            }
            sqlInsert.append(col.colName);
            sqlValues.append(col.value());
        }

        return sqlInsert.append(sqlValues).append(" )").toString();

    }


    private String buildDelSql1() {
        return str("delete from ", this.tableName());
    }


    private String buildUpdateSql1(Collection<?> ks) {

        StringBuilder sqlUpdate = new StringBuilder("update ").append(tableName()).append(" set ");

        int i = 0;
        for (ColumnMeta col : this.tableStruct.filterColumns(ks)) {
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


    private String buildSelectSql1AllAttrs() {

        StringBuilder sqlSelect = new StringBuilder("select  ");

        List<ColumnMeta> cols = this.tableStruct.columns();

        int i = 0;
        for (ColumnMeta col : cols) {
            if (i++ > 0) {
                sqlSelect.append(" ,\n        ");
            }
            sqlSelect.append(col.colName);
        }

        return sqlSelect.append("\nfrom ").append(this.tableName()).toString();

    }


    private String buildSelectSql1(Collection<?> ks) {

        StringBuilder sqlSelect = new StringBuilder("select  ");

        List<ColumnMeta> cols = this.tableStruct.columns();


        int i = 0;
        for (Object k : ks) {
            if (i++ > 0) {
                sqlSelect.append(" ,\n        ");
            }
            if (k instanceof S) {
                sqlSelect.append(((S) k).getValue());
            } else {
                sqlSelect.append(column(k));
            }

        }

        return sqlSelect.append("\nfrom ").append(this.tableName()).toString();

    }


    private String buildOrderSql(List<?> oks) {

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


    private String buildWhereF(Function<?, String> f, Collection<?> ws) {

        if (ws == null || ws.isEmpty()) {
            throw new DaoException(String.valueOf(ws));
        }

        StringBuilder sqlWhere = new StringBuilder("where   ");
        int i = 0;
        for (Object k : ws) {
            if (i++ > 0) {
                sqlWhere.append("\n    and ");
            }
            if (k instanceof S) {
                sqlWhere.append(((S) k).getValue());
            } else {
                sqlWhere.append(column(k));
                sqlWhere.append(" = ");
                sqlWhere.append(((Function<Object, String>) f).apply(k));
            }
        }

        return sqlWhere.toString();

    }

    private String buildWhere(Collection<?> ws) {
        return buildWhereF(k -> str(":", name(k)), ws);
    }

    private String buildWhereW(Collection<?> ws) {
        return buildWhereF(k -> str(":w__", name(k)), ws);
    }

    private Map<String, Object> convWArgs(Map<?, Object> wargs) {
        Map<String, Object> nmap = new HashMap<>();
        for (Map.Entry<?, Object> entry : wargs.entrySet()) {
            if ( entry.getKey() instanceof S ) {
                continue;
            }
            nmap.put(str("w__", name(entry.getKey())), entry.getValue());
        }
        return nmap;
    }

    private Object[] merge(Object[] a, Object... b) {
        Object[] c = new Object[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }



    private Map<String, Object> orm(Map<String, Object> row) {

        Map<String, Object> nRow = new HashMap<>();

        for (Map.Entry<String, Object> re : row.entrySet()) {
            ColumnMeta column = this.tableStruct.findColumn(re.getKey());
            if (column != null) {
                nRow.put(column.attrName, re.getValue());
            } else {
                nRow.put(re.getKey(), re.getValue());
            }
        }

        return new RowData(nRow);

    }


    //////////////////////////////////////////////////


    private String selectSql(Collection<?> attr) {
        if (attr == null || attr.isEmpty()) {
            return buildSelectSql1AllAttrs();
        }
        return buildSelectSql1(attr);
    }


    private String lockSql(Object lock) {
        if (lock == null) {
            return null;
        } else if (lock instanceof String) {
            return str(" \n", lock);
        } else if (lock instanceof Boolean) {
            if (((Boolean) lock).booleanValue()) {
                return " \nfor update";
            } else {
                return null;
            }
        } else {
            throw new DaoException(String.valueOf(lock));
        }

    }


    private String whereSql(whereOptions whereOptions) {
        if (whereOptions.where != null) {
            return str(" \nwhere   ", whereOptions.where);
        } else if (whereOptions.wattr != null) {
            return str(" \n", buildWhere(whereOptions.wattr));
        } else if (whereOptions.wargs != null) {
            return str(" \n", buildWhereW(whereOptions.wargs.keySet()));
        } else if (whereOptions.unconditional) {
            return null;
        } else {
            throw new DaoException("'where' is not set");
        }
    }


    private String orderSql(List<?> orderby) {
        if (orderby == null || orderby.isEmpty()) {
            return null;
        }
        return str(" \n", buildOrderSql(orderby));
    }


    private Object[] margeWhereArgs(whereOptions whereOptions, Object[] para) {
        if (whereOptions.where != null) {
            return para;
        } else if (whereOptions.wattr != null) {
            return para;
        } else if (whereOptions.wargs != null) {
            if (whereOptions.wargs.isEmpty()) {
                return para;
            } else {
                return merge(para, convWArgs((Map<?, Object>) whereOptions.wargs));
            }
        } else if (whereOptions.unconditional) {
            return para;
        } else {
            throw new DaoException("'where' is not set");
        }
    }


    private static final class whereOptions {

        public boolean unconditional;
        public List wattr;
        public String where;
        public Map wargs;


        public boolean isSetted() {
            return this.unconditional || this.where != null || this.wattr != null || this.wargs != null;
        }


        public void setUnconditional(boolean unconditional) {
            if (isSetted()) {
                throw new DaoException("'where' is setted");
            }
            this.unconditional = unconditional;
        }

        public void setWhere(String where) {
            if (isSetted()) {
                throw new DaoException("'where' is setted");
            }
            this.where = where;
        }

        public void setWattr(Object[] wattrs) {
            if (isSetted()) {
                throw new DaoException("'where' is setted");
            }
            this.wattr = args2List(wattrs);
        }

        public void setWargs(Object[] wargs) {

            if (isSetted()) {
                throw new DaoException("'where' is setted");
            }

            if (wargs == null) {
                this.wargs = Collections.EMPTY_MAP;
            } else {

                Map m = new HashMap<>();
                for (int i = 0, len = wargs.length; i < len; i++) {
                    if (wargs[i] instanceof S && ( i + 1 == len || wargs[i + 1] != null) ) {
                        m.put(wargs[i], null);
                    } else {
                        m.put(wargs[i], wargs[++i]);
                    }
                }
                this.wargs = Collections.unmodifiableMap(m);

            }
        }


    }


    private final class Query {


        private final List<Object> attrs;
        private final whereOptions whereOptions = new whereOptions();
        private List<Object> orders = Collections.EMPTY_LIST;

        private Query(Object[] attrs) {
            this.attrs = args2List(attrs);
        }


        public Query unconditional() {
            this.whereOptions.setUnconditional(true);
            return this;
        }

        public Query where(String where) {
            this.whereOptions.setWhere(where);
            return this;
        }

        public Query whereAttrs(Object... wattrs) {
            this.whereOptions.setWattr(wattrs);
            return this;
        }

        public Query whereArgs(Object... wargs) {
            this.whereOptions.setWargs(wargs);
            return this;
        }


        public Query orderBy(Object... orders) {
            this.orders = args2List(orders);
            return this;
        }


        public Val<Map<String, Object>> lock(Object lock, Object... para) {

            String sql = str(selectSql(attrs), whereSql(whereOptions), lockSql(lock));

            Val<Map<String, Object>> res = getDao().get(sql, margeWhereArgs(whereOptions, para));

            if (res.isPresent()) {
                return new Val<>(orm(res.getValue()));
            } else {
                return res;
            }

        }


        public Val<Map<String, Object>> selectOne(Object... para) {

            String sql = str(selectSql(attrs), whereSql(whereOptions));

            Val<Map<String, Object>> res = getDao().get(sql, margeWhereArgs(whereOptions, para));

            if (res.isPresent()) {
                return new Val<>(orm(res.getValue()));
            } else {
                return res;
            }

        }

        public List<Map<String, Object>> select(Object... para) {

            String sql = str(selectSql(attrs), whereSql(whereOptions), orderSql(orders));

            List<Map<String, Object>> res = getDao().load(sql, margeWhereArgs(whereOptions, para));

            if (res.isEmpty()) {
                return res;
            } else {
                return res.stream().map(row -> orm(row)).collect(Collectors.toList());
            }
        }

        public Page<Map<String, Object>> pageSelect(PageRequest pageRequest, Object... para) {


            List pageOrderBy = new ArrayList<>( orders );
            if (pageRequest.getSorts() != null) {
                List<Object> orderBy = new ArrayList<>();
                for (SortInfo sortInfo : pageRequest.getSorts()) {
                    orderBy.add(sortInfo.getName());
                    orderBy.add(sortInfo.getDirection());
                }
                if (!orderBy.isEmpty()) {
                    pageOrderBy.addAll(orderBy);
                }
            }

            String sql = str(selectSql(attrs), whereSql(whereOptions), orderSql(pageOrderBy));

            Page<Map<String, Object>> res = getDao()
                    .pagingLoad(sql,
                            new PageRequest(pageRequest.getPageNo(), pageRequest.getPageSize()),
                            margeWhereArgs(whereOptions, para));

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



    public final class SelectOneExecutor {

        private final Query query;

        public SelectOneExecutor(Query query) {
            this.query = query;
        }

        public Val<Map<String, Object>> lock(boolean lock, Object... para) {
            return query.lock(lock, para);
        }

        public Val<Map<String, Object>> lock(String lock, Object... para) {
            return query.lock(lock, para);
        }

        public Val<Map<String, Object>> query(Object... para) {
            return query.selectOne(para);
        }

    }


    public final class SelectOne {

        private final Object[] attrs;

        private SelectOne(Object[] attrs) {
            this.attrs = attrs;
        }

        public SelectOneExecutor unconditional() {
            return new SelectOneExecutor( new Query(this.attrs).unconditional() );
        }

        public SelectOneExecutor where(String where) {
            return new SelectOneExecutor( new Query(this.attrs).where(where) );
        }

        public SelectOneExecutor whereAttrs(Object... attrs) {
            return new SelectOneExecutor( new Query(this.attrs).whereAttrs(attrs) );
        }

        public SelectOneExecutor whereArgs(Object... args) {
            return new SelectOneExecutor( new Query(this.attrs).whereArgs(args) );
        }


    }


    public SelectOne selectOne(Object... attrs) {
        return new SelectOne(attrs);
    }





    public final class SelectExecutor {

        private final Query query;

        public SelectExecutor(Query query) {
            this.query = query;
        }

        public SelectExecutor orderBy(Object... orders) {
            this.query.orderBy(orders);
            return this;
        }


        public List<Map<String, Object>> query(Object... para) {
            return query.select(para);
        }


        public Page<Map<String, Object>> pagingQuery(PageRequest pageRequest, Object... para) {
            return query.pageSelect(pageRequest, para);
        }

    }


    public final class Select {

        private final Object[] attrs;

        private Select(Object[] attrs) {
            this.attrs = attrs;
        }

        public SelectExecutor unconditional() {
           return new SelectExecutor( new Query( this.attrs ).unconditional() );
        }

        public SelectExecutor where(String where) {
            return new SelectExecutor( new Query( this.attrs ).where(where) );
        }

        public SelectExecutor whereAttrs(Object... attrs) {
            return new SelectExecutor( new Query( this.attrs ).whereAttrs(attrs) );
        }

        public SelectExecutor whereArgs(Object... args) {
            return new SelectExecutor( new Query( this.attrs ).whereArgs(args) );
        }


    }


    public Select select(Object... attr) {
        return new Select(attr);
    }


    //////////////////////////////////////////////////

    private final class Update {

        private final List<Object> attrs;

        private Update(Object[] attrs) {
            this.attrs = args2List(attrs);
        }

        private final whereOptions whereOptions = new whereOptions();


        public Update unconditional() {
            this.whereOptions.setUnconditional(true);
            return this;
        }

        public Update where(String where) {
            this.whereOptions.setWhere(where);
            return this;
        }

        public Update whereAttrs(Object... wattrs) {
            this.whereOptions.setWattr(wattrs);
            return this;
        }

        public Update whereArgs(Object... wargs) {
            this.whereOptions.setWargs(wargs);
            return this;
        }


        public int add(Object... para) {

            if (para == null || para.length == 0 || para[0] == null) {
                throw new IllegalArgumentException("data is empty");
            }

            String sql;
            if (attrs == null || attrs.isEmpty()) {
                sql = buildInsertSql(args2Map(para).keySet());
            } else {
                sql = buildInsertSql(attrs);
            }

            return getDao().update(sql, para);

        }

        public int[] batchAdd(Collection<?> cell, Object... para) {

            if (cell == null || cell.isEmpty()) {
                throw new IllegalArgumentException("data is empty");
            }

            Map paraMap = new HashMap<>();
            if (para != null && para.length > 0 && para[0] != null) {
                paraMap = args2Map(para);
            }

            String sql;
            if (attrs == null || attrs.isEmpty()) {
                Set<Object> ks = MapBuilder.start().putAll((Map) cell.iterator().next()).putAll(paraMap).get().keySet();
                sql = buildInsertSql(ks);
            } else {
                sql = buildInsertSql(attrs);
            }

            Collection data;
            if (paraMap.isEmpty()) {
                data = cell;
            } else {
                data = new ArrayList<>(cell.size());
                for ( Object row : cell) {
                    data.add(MapBuilder.start().putAll((Map)row).putAll(paraMap).get());
                }
            }

            return getDao().batchUpdate(sql, data);

        }


        public int edit(Object... para) {

            String updSql;
            if (attrs == null || attrs.isEmpty()) {
                if (para == null || para.length == 0 || para[0] == null) {
                    throw new IllegalArgumentException("data is empty");
                }
                updSql = buildUpdateSql1(args2Map(para).keySet());
            } else {
                updSql = buildUpdateSql1(attrs);
            }

            String sql = str(updSql, whereSql(whereOptions));

            return getDao().update(sql, margeWhereArgs(whereOptions, para));

        }

        @SuppressWarnings("rawtypes")
        public int[] batchEdit(Collection<?> cell, Object... para) {

            if (cell == null || cell.isEmpty()) {
                throw new IllegalArgumentException("data is empty");
            }

            if (whereOptions.wargs != null) {
                throw new IllegalArgumentException("batch edit not support argument: wargs");
            }

            String sql = str(buildUpdateSql1(attrs), whereSql(whereOptions));

            Map paraMap = new HashMap<>();
            if (para != null && para.length > 0 && para[0] != null) {
                paraMap = DbUtil.DBFACTORY.getArgProcessor().proc(para);
            }

            Collection data;
            if (paraMap.isEmpty()) {
                data = cell;
            } else {
                data = new ArrayList<>(cell.size());
                for (Object row : cell) {
                    data.add(MapBuilder.start().putAll( (Map) row).putAll(paraMap).get());
                }
            }

            return getDao().batchUpdate(sql, data);

        }


        public int del(Object... para) {
            String sql = str(buildDelSql1(), whereSql(whereOptions));
            return getDao().update(sql, margeWhereArgs(whereOptions, para));
        }

    }


    public final class Add {

        private final Update update;

        private Add(Object[] attrs) {
            this.update = new Update(attrs);
        }

        public int exec(Object... para) {
            return this.update.add(para);
        }

    }

    public Add add(Object... attrs) {
        return new Add(attrs);
    }

    public final class BatchAdd {

        private final Update update;

        private BatchAdd(Object[] attrs) {
            this.update = new Update(attrs);
        }



        public int[] exec(Collection<?> cell, Object... para) {
            return this.update.batchAdd(cell, para);
        }

    }

    public BatchAdd batchAdd(Object... attrs) {
        return new BatchAdd(attrs);
    }


    public final class EditExecutor {

        private final Update update;

        private EditExecutor(Update update) {
            this.update = update;
        }

        public int exec(Object... para) {
            return this.update.edit(para);
        }

    }

    public final class Edit {

        private Object[] attrs;

        private Edit(Object[] attrs) {
            this.attrs = attrs;
        }

        public EditExecutor unconditional() {
            return new EditExecutor( new Update(attrs).unconditional() );
        }

        public EditExecutor where(String where) {
            return new EditExecutor( new Update(attrs).where(where) );
        }

        public EditExecutor whereAttrs(Object... attrs) {
            return new EditExecutor( new Update(attrs).whereAttrs(attrs) );
        }

        public EditExecutor whereArgs(Object... args) {
            return new EditExecutor( new Update(attrs).whereArgs(args));
        }



    }

    public Edit edit(Object... attr) {
        return new Edit(attr);
    }




    public final class BatchEditExecutor {

        private final Update update;

        private BatchEditExecutor(Update update) {
            this.update = update;
        }

        public int[] exec(Collection<?> cell, Object... para) {
            return this.update.batchEdit(cell, para);
        }
    }



    public final class BatchEdit {

        private final Object[] attrs;

        private BatchEdit(Object[] attrs) {
            this.attrs = attrs;
        }

        public BatchEditExecutor where(String where) {
            return new BatchEditExecutor( new Update(attrs).where(where) );
        }

        public BatchEditExecutor whereAttrs(Object... attrs) {
            return new BatchEditExecutor( new Update(attrs).whereAttrs(attrs) );
        }


    }

    public BatchEdit batchEdit(Object... attrs) {
        if (attrs == null || attrs.length == 0) {
            throw new DaoException("'attrs' is not set");
        }
        return new BatchEdit(attrs);
    }







    public final class DelExecutor {

        private final Update update;

        private DelExecutor(Update update) {
            this.update = update;
        }


        public int exec(Object... para) {
            return this.update.del(para);
        }


    }


    public final class Del {

        private Del() {}

        private final Update update = new Update((Object[]) null);

        public DelExecutor unconditional() {
            return new DelExecutor( new Update((Object[]) null).unconditional() );
        }

        public DelExecutor where(String where) {
            return new DelExecutor( new Update((Object[]) null).where(where) );
        }

        public DelExecutor whereAttrs(Object... attrs) {
            return new DelExecutor( new Update((Object[]) null).whereAttrs(attrs));
        }

        public DelExecutor whereArgs(Object... args) {
            return new DelExecutor( new Update((Object[]) null).whereArgs(args) );
        }


    }

    public Del del() {
        return new Del();
    }


    //////////////////////////////////////////////////
    // util


    private static Map args2Map( Object[] args ) {
        if (args == null) {
            return Collections.EMPTY_MAP;
        }
       return Collections.unmodifiableMap(DbUtil.DBFACTORY.getArgProcessor().proc(args));
    }


    private static List args2List(Object[] args) {
        if (args == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(Arrays.asList(args));
    }



    private String str(Object... strs) {
        StringBuilder sb = new StringBuilder();
        for (Object s : strs) {
            if (s != null) {
                sb.append(name(s));
            }
        }
        return sb.toString();
    }

    private String name(Object obj) {
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



    public Dao getDao() {
        return dao == null ? DbUtil.getDao(this.dataSourceName) : dao;
    }

    public String tableName() {
        return this.tableStruct.tableName();
    }

    public String column(Object attr) {
        return this.tableStruct.getColumn(attr).colName;
    }


}
