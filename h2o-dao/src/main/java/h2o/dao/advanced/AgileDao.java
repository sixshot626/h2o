package h2o.dao.advanced;

import h2o.common.collection.KeyMap;
import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.ResultInfo;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.Key;
import h2o.common.lang.Special;
import h2o.common.lang.Val;
import h2o.common.util.collection.ListBuilder;
import h2o.common.util.collection.MapBuilder;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.advanced.support.WhereBuilder;
import h2o.dao.advanced.support.WhereConditions;
import h2o.dao.advanced.support.WhereOptions;
import h2o.dao.exception.DaoException;
import h2o.dao.structure.ColumnMeta;
import h2o.dao.structure.TableStruct;
import h2o.dao.structure.TableStructParser;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AgileDao {

    private final String dataSourceName;
    private final Dao dao;
    private final TableStruct tableStruct;

    private final Consumer<Dao> afterDaoSetter;


    public AgileDao(Class<?> entityClazz) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
        this.tableStruct = TableStructParser.parse(entityClazz).get();
        this.afterDaoSetter = null;
    }

    public AgileDao(String dataSourceName, Class<?> entityClazz) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.tableStruct = TableStructParser.parse(entityClazz).get();
        this.afterDaoSetter = null;
    }

    public AgileDao(Dao dao, Class<?> entityClazz) {
        this.dataSourceName = null;
        this.dao = dao;
        this.tableStruct = TableStructParser.parse(entityClazz).get();
        this.afterDaoSetter = null;
    }

    public AgileDao(TableStruct tableStruct) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
        this.tableStruct = tableStruct;
        this.afterDaoSetter = null;
    }

    public AgileDao(String dataSourceName, TableStruct tableStruct) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.tableStruct = tableStruct;
        this.afterDaoSetter = null;
    }

    public AgileDao(Dao dao, TableStruct tableStruct) {
        this.dataSourceName = null;
        this.dao = dao;
        this.tableStruct = tableStruct;
        this.afterDaoSetter = null;
    }

    private AgileDao(String dataSourceName, Dao dao, TableStruct tableStruct, Consumer<Dao> afterDaoSetter) {
        this.dataSourceName = dataSourceName;
        this.dao = dao;
        this.tableStruct = tableStruct;
        this.afterDaoSetter = afterDaoSetter;
    }


    public AgileDao daoSet( Consumer<Dao> daoSetter ) {
        return new AgileDao( this.dataSourceName , this.dao , this.tableStruct , daoSetter );
    }



    public static final String ASC  = "asc";
    public static final String DESC = "desc";









    public WhereBuilder whereBuilder() {
        return new WhereBuilder( tableStruct ).unconditional(false);
    }

    public WhereBuilder whereBuilder( String prefix ) {
        return new WhereBuilder( tableStruct , prefix ).unconditional(false);
    }



    ///////////////////////////////////////////////////////////////////////

    private final class Query {


        private final List<Object> attrs;
        private WhereConditions whereConditions;
        private List<Object> orders = Collections.emptyList();

        private Query(Object[] attrs) {
            this.attrs = args2List(attrs);
        }


        public Query unconditional() {

            WhereOptions whereOptions = new WhereOptions( tableStruct , true );
            whereOptions.setUnconditional(true);

            this.whereConditions = whereOptions;
            return this;
        }

        public Query where(String where) {

            WhereOptions whereOptions = new WhereOptions( tableStruct , true );
            whereOptions.setWhere(where);

            this.whereConditions = whereOptions;
            return this;
        }

        public Query whereAttrs(Object... wattrs) {
            WhereOptions whereOptions = new WhereOptions( tableStruct , true );
            whereOptions.setWattr(wattrs);

            this.whereConditions = whereOptions;
            return this;
        }

        public Query whereArgs(Object... wargs) {
            WhereOptions whereOptions = new WhereOptions( tableStruct , true );
            whereOptions.setWargs(wargs);

            this.whereConditions = whereOptions;
            return this;
        }


        public Query buildWhere( Consumer<WhereBuilder> consumer ) {
            WhereBuilder whereBuilder = new WhereBuilder( tableStruct );
            consumer.accept( whereBuilder );

            this.whereConditions = whereBuilder;
            return this;
        }

        public Query where( WhereBuilder whereBuilder ) {
            this.whereConditions = whereBuilder;
            return this;
        }


        public Query orderBy(Object... orders) {
            this.orders = args2List(orders);
            return this;
        }


        public Val<Map<String, Object>> lock(Object lock, Object... para) {

            String sql = str(selectSql(attrs), this.whereConditions.whereSql() , lockSql(lock));

            Map<Object,Object> wherePara = this.whereConditions.params();

            Val<Map<String, Object>> res = getDao().get(sql, merge( para , wherePara ) );

            if (res.isPresent()) {
                return new Val<>(orm(res.getValue()));
            } else {
                return res;
            }

        }


        public Val<Map<String, Object>> selectOne(Object... para) {

            String sql = str(selectSql(attrs), this.whereConditions.whereSql());

            Map<Object,Object> wherePara = this.whereConditions.params();

            Val<Map<String, Object>> res = getDao().get(sql, merge( para , wherePara ) );

            if (res.isPresent()) {
                return new Val<>(orm(res.getValue()));
            } else {
                return res;
            }

        }

        public List<Map<String, Object>> select(Object... para) {

            String sql = str(selectSql(attrs), this.whereConditions.whereSql(), orderSql(orders));

            Map<Object,Object> wherePara = this.whereConditions.params();

            List<Map<String, Object>> res = getDao().load(sql, merge( para , wherePara ) );

            if (res.isEmpty()) {
                return res;
            } else {
                return res.stream().map(this::orm).collect(Collectors.toList());
            }
        }


        public List<Map<String, Object>> fetch( ResultInfo fetchRequest , Object... para) {

            List fetchOrderBy = new ArrayList<>( orders );
            if ( fetchRequest.getSorts() != null) {
                List<Object> orderBy = new ArrayList<>();
                for (SortInfo sortInfo : fetchRequest.getSorts()) {
                    orderBy.add(sortInfo.getName());
                    orderBy.add(sortInfo.getDirection());
                }
                if (!orderBy.isEmpty()) {
                    fetchOrderBy.addAll(orderBy);
                }
            }

            String sql = str(selectSql(attrs), this.whereConditions.whereSql(), orderSql(fetchOrderBy));

            Map<Object,Object> wherePara = this.whereConditions.params();

            List<Map<String, Object>> res = getDao()
                    .fetch( sql,
                            new ResultInfo(fetchRequest.getStart(), fetchRequest.getSize()),
                            merge( para , wherePara ) );

            if (res.isEmpty()) {
                return res;
            } else {
                List<Map<String, Object>> data = new ArrayList<>(res.size());
                for ( Map<String, Object> row : res ) {
                    data.add(orm(row));
                }
                return data;
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

            String sql = str(selectSql(attrs), this.whereConditions.whereSql(), orderSql(pageOrderBy));

            Map<Object,Object> wherePara = this.whereConditions.params();

            Page<Map<String, Object>> res = getDao()
                    .pagingLoad(sql,
                            new PageRequest(pageRequest.getPageNo(), pageRequest.getPageSize()),
                            merge( para , wherePara ) );

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






        ///////////////////////////////////////////////////////////////////////


        private String buildSelectSql1AllAttrs() {

            StringBuilder sqlSelect = new StringBuilder("select  ");

            List<ColumnMeta> cols = tableStruct.columns();

            int i = 0;
            for (ColumnMeta col : cols) {
                if (i++ > 0) {
                    sqlSelect.append(" ,\n        ");
                }
                sqlSelect.append(col.colName);
            }

            return sqlSelect.append("\nfrom ").append(tableName()).toString();

        }


        private String buildSelectSql1(Collection<?> ks) {

            StringBuilder sqlSelect = new StringBuilder("select  ");

            int i = 0;
            for (Object k : ks) {
                if (i++ > 0) {
                    sqlSelect.append(" ,\n        ");
                }
                if (k instanceof Special) {
                    sqlSelect.append(((Special) k).getValue());
                } else {
                    sqlSelect.append(column(k));
                }

            }

            return sqlSelect.append("\nfrom ").append(tableName()).toString();

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

        private String orderSql(List<?> orderby) {
            if (orderby == null || orderby.isEmpty()) {
                return null;
            }
            return str(" \n", buildOrderSql(orderby));
        }



        private Map<String, Object> orm(Map<String, Object> row) {

            Map<String, Object> nRow = new HashMap<>();

            for (Map.Entry<String, Object> re : row.entrySet()) {
                ColumnMeta column = tableStruct.findColumn(re.getKey());
                if (column != null) {
                    nRow.put(column.attrName, re.getValue());
                } else {
                    nRow.put(re.getKey(), re.getValue());
                }
            }

            return new KeyMap(nRow);

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

        public SelectOneExecutor whereAttrs(Object... wattrs) {
            return new SelectOneExecutor( new Query(this.attrs).whereAttrs(wattrs) );
        }

        public SelectOneExecutor whereArgs(Object... args) {
            return new SelectOneExecutor( new Query(this.attrs).whereArgs(args) );
        }



        public SelectOneExecutor buildWhere( Consumer<WhereBuilder> consumer ) {
            return new SelectOneExecutor( new Query(this.attrs).buildWhere(  consumer ) );
        }

        public SelectOneExecutor where( WhereBuilder whereBuilder ) {
            return new SelectOneExecutor( new Query(this.attrs).where(  whereBuilder ) );
        }


    }


    public SelectOne selectOne(Object... attrs) {
        return new SelectOne(attrs);
    }



    public final class SelectExecutor {

        private final Query query;

        public SelectExecutor( Query query ) {
            this.query = query;
        }

        public SelectExecutor orderBy( Object... orders ) {
            this.query.orderBy( orders );
            return this;
        }


        public List<Map<String, Object>> query( Object... para) {
            return query.select( para );
        }


        public List<Map<String, Object>> fetch( ResultInfo fetchRequest , Object... para ) {
            return query.fetch( fetchRequest , para );
        }



        public Page<Map<String, Object>> pagingQuery( PageRequest pageRequest , Object... para ) {
            return query.pageSelect( pageRequest, para );
        }

    }


    public final class Select {

        private final Object[] attrs;

        private Select(Object[] attrs) {
            this.attrs = attrs;
        }

        public SelectExecutor unconditional() {
           return new SelectExecutor( new Query(this.attrs).unconditional() );
        }

        public SelectExecutor where(String where) {
            return new SelectExecutor( new Query(this.attrs).where(where) );
        }

        public SelectExecutor whereAttrs(Object... wattrs) {
            return new SelectExecutor( new Query(this.attrs).whereAttrs(wattrs) );
        }

        public SelectExecutor whereArgs(Object... args) {
            return new SelectExecutor( new Query(this.attrs).whereArgs(args) );
        }

        public SelectExecutor buildWhere( Consumer<WhereBuilder> consumer ) {
            return new SelectExecutor( new Query(this.attrs).buildWhere(  consumer ) );
        }

        public SelectExecutor where( WhereBuilder whereBuilder ) {
            return new SelectExecutor( new Query(this.attrs).where(  whereBuilder ) );
        }


    }


    public Select select(Object... attr) {
        return new Select(attr);
    }





    ///////////////////////////////////////////////////////////////////////

    private final class Update {

        private final List<Object> attrs;


        private Update(Object[] attrs) {
            this.attrs = args2List(attrs);
        }

        private WhereConditions whereConditions;


        public Update unconditional() {
            WhereOptions whereOptions = new WhereOptions( tableStruct , false );
            whereOptions.setUnconditional(true);

            this.whereConditions = whereOptions;
            return this;
        }

        public Update where(String where) {
            WhereOptions whereOptions = new WhereOptions( tableStruct , false );
            whereOptions.setWhere(where);

            this.whereConditions = whereOptions;
            return this;
        }

        public Update whereAttrs(Object... wattrs) {
            WhereOptions whereOptions = new WhereOptions( tableStruct , false );
            whereOptions.setWattr(wattrs);

            this.whereConditions = whereOptions;
            return this;
        }

        public Update whereArgs(Object... wargs) {
            WhereOptions whereOptions = new WhereOptions( tableStruct , false );
            whereOptions.setWargs(wargs);

            this.whereConditions = whereOptions;
            return this;
        }

        public Update buildWhere( Consumer<WhereBuilder> consumer ) {
            WhereBuilder whereBuilder = new WhereBuilder(tableStruct).unconditional(false);
            consumer.accept(whereBuilder);

            this.whereConditions = whereBuilder;
            return this;
        }

        public Update buildWhere( WhereBuilder whereBuilder ) {
            this.whereConditions = whereBuilder;
            return this;
        }


        private String additionalStr;

        public void setAdditionalStr(String str ) {
            this.additionalStr = str;
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

            if ( additionalStr != null ) {
                sql = str(sql , "\n        " , additionalStr);
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

                Object item = cell.iterator().next();
                Set<Object> ks = MapBuilder.start().putAll( args2Map( parseArgs( item ) ) ).putAll(paraMap).get().keySet();
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
                    data.add( ListBuilder.start().add(parseArgs(row)).add(paraMap).get() );
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



            String sql = str(updSql, this.whereConditions.whereSql() );

            if ( additionalStr != null ) {
                sql = str(sql , "\n        " , additionalStr);
            }

            Map<Object,Object> wherePara = this.whereConditions.params();

            return getDao().update( sql,  merge( para , wherePara ) );

        }

        @SuppressWarnings("rawtypes")
        public int[] batchEdit(Collection<?> cell, Object... para) {

            if (cell == null || cell.isEmpty()) {
                throw new IllegalArgumentException("data is empty");
            }

            String sql = str(buildUpdateSql1(attrs), this.whereConditions.whereSql());

            Map paraMap = new HashMap<>();
            if (para != null && para.length > 0 && para[0] != null) {
                paraMap = DbUtil.DBFACTORY.getArgProcessor().proc(para);
            }

            paraMap.putAll( this.whereConditions.params() );

            Collection data;
            if (paraMap.isEmpty()) {
                data = cell;
            } else {
                data = new ArrayList<>(cell.size());
                for (Object row : cell) {
                    data.add( ListBuilder.start().add(parseArgs(row)).add(paraMap).get() );
                }
            }

            return getDao().batchUpdate(sql, data);

        }


        public int del(Object... para) {

            String sql = str(buildDelSql1(), this.whereConditions.whereSql());

            if ( additionalStr != null ) {
                sql = str(sql , "\n        " , additionalStr);
            }

            Map<Object,Object> wherePara = this.whereConditions.params();

            return getDao().update(sql, merge( para , wherePara ) );
        }





        ///////////////////////////////////////////////////////////////////////


        private String buildInsertSql(Collection<?> ks) {

            StringBuilder sqlInsert = new StringBuilder("insert into ").append(tableName()).append(" ( ");
            StringBuilder sqlValues = new StringBuilder(" ) \n        values ( ");

            int i = 0;
            for (ColumnMeta col : tableStruct.sort( tableStruct.filterColumns(ks)) ) {
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
            return str("delete from ", tableName());
        }


        private String buildUpdateSql1(Collection<?> ks) {

            StringBuilder sqlUpdate = new StringBuilder("update ").append(tableName()).append(" set ");

            int i = 0;


            for ( Object o : ks ) {

                if (i++ > 0) {
                    sqlUpdate.append(" , ");
                }
                sqlUpdate.append("\n        ");

                if ( o instanceof Special) {

                    sqlUpdate.append( ((Special) o).getValue() );

                } else {

                    ColumnMeta col = tableStruct.getColumn(o);

                    sqlUpdate.append(col.colName);
                    sqlUpdate.append(" = ");
                    sqlUpdate.append(col.value());
                }
            }

            return sqlUpdate.toString();

        }



    }


    public final class Add {

        private final Update update;

        private Add(Object[] attrs) {
            this.update = new Update(attrs);
        }

        public Add appendStr(String str) {
            this.update.setAdditionalStr(str);
            return this;
        }

        public Add appendStr(boolean c , String str) {
            if (c) {
                this.update.setAdditionalStr(str);
            } else {
                this.update.setAdditionalStr(null);
            }
            return this;
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

        public EditExecutor appendStr(String str) {
            this.update.setAdditionalStr(str);
            return this;
        }

        public EditExecutor appendStr(boolean c , String str) {
            if (c) {
                this.update.setAdditionalStr(str);
            } else {
                this.update.setAdditionalStr(null);
            }
            return this;
        }

        public int exec(Object... para) {
            return this.update.edit(para);
        }

    }

    public final class Edit {

        private final Object[] attrs;

        private Edit(Object[] attrs) {
            this.attrs = attrs;
        }

        public EditExecutor unconditional() {
            return new EditExecutor( new Update(this.attrs).unconditional() );
        }

        public EditExecutor where(String where) {
            return new EditExecutor( new Update(this.attrs).where(where) );
        }

        public EditExecutor whereAttrs(Object... wattrs) {
            return new EditExecutor( new Update(this.attrs).whereAttrs(wattrs) );
        }

        public EditExecutor whereArgs(Object... args) {
            return new EditExecutor( new Update(this.attrs).whereArgs(args));
        }


        public EditExecutor buildWhere( Consumer<WhereBuilder> consumer ) {
            return new EditExecutor( new Update(this.attrs).buildWhere(  consumer ) );
        }

        public EditExecutor buildWhere( WhereBuilder whereBuilder ) {
            return new EditExecutor( new Update(this.attrs).buildWhere(  whereBuilder ) );
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

        public int[] exec( Collection<?> cell, Object... para) {
            return this.update.batchEdit(cell, para);
        }
    }



    public final class BatchEdit {

        private final Object[] attrs;

        private BatchEdit(Object[] attrs) {
            this.attrs = attrs;
        }

        public BatchEditExecutor where(String where) {
            return new BatchEditExecutor( new Update(this.attrs).where(where) );
        }

        public BatchEditExecutor whereAttrs(Object... wattrs) {
            return new BatchEditExecutor( new Update(this.attrs).whereAttrs(wattrs) );
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

        public DelExecutor appendStr(String str) {
            this.update.setAdditionalStr(str);
            return this;
        }

        public DelExecutor appendStr(boolean c , String str) {
            if (c) {
                this.update.setAdditionalStr(str);
            } else {
                this.update.setAdditionalStr(null);
            }
            return this;
        }


        public int exec(Object... para) {
            return this.update.del(para);
        }


    }


    public final class Del {

        private Del() {}

        public DelExecutor unconditional() {
            return new DelExecutor( new Update(null).unconditional() );
        }

        public DelExecutor where(String where) {
            return new DelExecutor( new Update(null).where(where) );
        }

        public DelExecutor whereAttrs(Object... attrs) {
            return new DelExecutor( new Update(null).whereAttrs(attrs));
        }

        public DelExecutor whereArgs(Object... args) {
            return new DelExecutor( new Update(null).whereArgs(args) );
        }

        public DelExecutor buildWhere( Consumer<WhereBuilder> consumer ) {
            return new DelExecutor( new Update(null).buildWhere(  consumer ) );
        }

        public DelExecutor buildWhere( WhereBuilder whereBuilder ) {
            return new DelExecutor( new Update(null).buildWhere(  whereBuilder ) );
        }


    }

    public Del del() {
        return new Del();
    }


    //////////////////////////////////////////////////
    // util

    private static Object[] parseArgs( Object args ) {
        if ( args instanceof Map ) {
            return new Object[] {args};
        } else if ( args instanceof Collection ) {
            return ((Collection<?>) args).toArray();
        } else if ( args instanceof Object[] ) {
            return (Object[])args;
        } else {
            throw new IllegalArgumentException();
        }
    }


    private static Object[] merge(Object[] a, Object... b) {
        Object[] c = new Object[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    private static Map args2Map( Object[] args ) {
        if (args == null) {
            return Collections.emptyMap();
        }
       return Collections.unmodifiableMap(DbUtil.DBFACTORY.getArgProcessor().proc(args));
    }


    private static List args2List(Object[] args) {
        if (args == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(args));
    }



    private static String str(Object... strs) {
        StringBuilder sb = new StringBuilder();
        for (Object s : strs) {
            if (s != null) {
                sb.append(name(s));
            }
        }
        return sb.toString();
    }

    private static String name(Object obj) {
        String key;
        if (obj instanceof String) {
            key = (String) obj;
        } else if (obj instanceof Key) {
            key = ((Key) obj).name();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
        return key;
    }



    public Dao getDao() {

        Dao _dao = this.dao == null ? DbUtil.getDao(this.dataSourceName) : this.dao;

        if ( this.afterDaoSetter != null ) {
            this.afterDaoSetter.accept( _dao );
        }

        return _dao;

    }

    public String tableName() {
        return this.tableStruct.tableName();
    }

    public String column(Object attr) {
        return this.tableStruct.getColumn(attr).colName;
    }


}
