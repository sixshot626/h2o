package h2o.dao.impl;

import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageInfo;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.ResultInfo;
import h2o.common.lang.Val;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.Assert;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.Dao;
import h2o.dao.ResultSetCallback;
import h2o.dao.exception.DaoException;
import h2o.dao.impl.sql.TSql;
import h2o.dao.log.LogWriter;
import h2o.dao.page.PagingProcessor;
import h2o.dao.proc.ArgProcessor;
import h2o.dao.proc.OrmProcessor;
import h2o.dao.proc.RowProcessor;
import h2o.dao.sql.SqlSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao implements Dao {

    private final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    private ArgProcessor argProcessor;

    private RowProcessor rowProcessor;

    private OrmProcessor ormProcessor;

    private PagingProcessor pagingProcessor;

    private LogWriter logWriter;

    private boolean autoClose = true;


    @Override
    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    @Override
    public boolean isAutoClose() {
        return autoClose;
    }

    @Override
    public void setArgProcessor(ArgProcessor argProcessor) {
        this.argProcessor = argProcessor;
    }

    @Override
    public void setRowProcessor(RowProcessor rowProcessor) {
        this.rowProcessor = rowProcessor;
    }

    @Override
    public void setOrmProcessor(OrmProcessor ormProcessor) {
        this.ormProcessor = ormProcessor;
    }

    @Override
    public void setPagingProcessor(PagingProcessor pagingProcessor) {
        this.pagingProcessor = pagingProcessor;
    }

    @Override
    public void setLogWriter(LogWriter logWriter) {
        this.logWriter = logWriter;
    }


    protected ArgProcessor getArgProcessor() {
        Assert.notNull(argProcessor , "ArgProcessor must not be null");
        return argProcessor;
    }

    protected RowProcessor getRowProcessor() {
        Assert.notNull(rowProcessor , "RowProcessor must not be null");
        return rowProcessor;
    }

    protected OrmProcessor getOrmProcessor() {
        Assert.notNull(ormProcessor , "OrmProcessor must not be null");
        return ormProcessor;
    }

    protected PagingProcessor getPagingProcessor() {
        Assert.notNull(pagingProcessor , "PagingProcessor must not be null");
        return pagingProcessor;
    }

    protected LogWriter getLogWriter() {
        return logWriter;
    }

    protected Map<String, Object> argProc(Object... args) {
        ArgProcessor _argProcessor = this.getArgProcessor();
        return _argProcessor.proc(args);
    }

    protected Map<String, Object> rowProc(Map<String, Object> row) {
        RowProcessor _rowProcessor = this.getRowProcessor();
        return _rowProcessor.proc(row);
    }

    protected <T> T ormProc(Map<String, Object> row, Class<T> clazz) {
        OrmProcessor _ormProcessor = this.getOrmProcessor();
        return _ormProcessor.proc(row, clazz);
    }


    @Override
    public <T> Val<T> getField(SqlSource sqlSource, String fieldName, Object... args) throws DaoException {

        Val<Map<String, Object>> row = this.get( sqlSource, args );
        if ( row.isPresent() ) {

            if ( fieldName == null ) {
                @SuppressWarnings("unchecked")
                Val<T> r = (Val<T>)new Val<>(row.get().values().iterator().next());
                return r;
            } else {
                @SuppressWarnings("unchecked")
                Val<T> r = (Val<T>)new Val<>(row.get().get(fieldName));
                return r;
            }

        }

        return Val.empty();
    }

    @Override
    public <T> List<T> loadFields(SqlSource sqlSource, String fieldName, Object... args) throws DaoException {

        List<T> fields = new ArrayList<>();

        List<Map<String, Object>> rows = this.load(sqlSource, args);
        for ( Map<String,Object> row : rows ) {

            if ( fieldName == null ) {
                @SuppressWarnings("unchecked")
                T f = (T)row.values().iterator().next();
                fields.add( f );
            } else {
                @SuppressWarnings("unchecked")
                T f = (T)row.get(fieldName);
                fields.add( f );
            }

        }
        return fields;
    }

    @Override
    public <T> Val<T> get( Class<T> clazz, SqlSource sqlSource, Object... args ) throws DaoException {

        try {

            Val<Map<String, Object>> row = this.get( sqlSource, args );

            return row.isPresent() ? new Val<>( this.ormProc(row.getValue(), clazz) ) : Val.empty();

        } catch ( DaoException e ) {
            throw e;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }


    @Override
    public <T> List<T> load( Class<T> clazz, SqlSource sqlSource, Object... args ) throws DaoException {

        try {
            List<Map<String, Object>> rows = this.load( sqlSource, args );

            List<T> objs = new ArrayList<T>(rows.size());

            for (Map<String, Object> row : rows) {
                objs.add(this.ormProc(row, clazz));
            }

            return objs;

        } catch ( DaoException e ) {
            throw e;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }




    @Override
    public List<Map<String, Object>> fetch( SqlSource sqlSource, ResultInfo fetchRequest, Object... args ) throws DaoException {

        try {

            PagingProcessor _pagingProcessor = this.getPagingProcessor();

            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);


            Tuple2<String, Map<String, Object>> p = _pagingProcessor.pagingSql(sql, fetchRequest);
            paramMap.putAll(p.e1);

            return this.load(p.e0, paramMap);

        } catch ( DaoException e ) {
            throw e;
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

    @Override
    public <T> List<T> fetch( Class<T> clazz, SqlSource sqlSource, ResultInfo fetchRequest, Object... args ) throws DaoException {

        try {

            List<Map<String, Object>> rows = this.fetch(sqlSource, fetchRequest, args);

            List<T> objs = new ArrayList<T>(rows.size());

            for (Map<String, Object> row : rows) {
                objs.add(this.ormProc(row, clazz));
            }

            return objs;

        } catch ( DaoException e ) {
            throw e;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }





    @Override
    public Page<Map<String, Object>> pagingLoad(SqlSource sqlSource, PageRequest pageRequest, Object... args) {

        boolean _autoClose = this.autoClose;
        if ( _autoClose ) {
            this.autoClose = false;
        }

        try {

            PagingProcessor _pagingProcessor = this.getPagingProcessor();

            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);

            Tuple2<String, String> t = _pagingProcessor.totalSql(sql);
            Val<Number> count = this.getField(t.e0, t.e1, args);

            PageInfo pageInfo = new PageInfo(pageRequest, count.get().longValue());
            if (pageInfo.getTotalElements() == 0L) {
                return new Page<Map<String, Object>>(pageInfo, ListBuilder.<Map<String, Object>>newEmptyList());
            }

            Tuple2<String, Map<String, Object>> p = _pagingProcessor.pagingSql( sql, new ResultInfo(pageRequest) );
            paramMap.putAll(p.e1);
            List<Map<String, Object>> records = this.load(p.e0, paramMap);

            return new Page<Map<String, Object>>(pageInfo, records);

        } catch ( DaoException e ) {
            throw e;
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            if ( _autoClose ) {
                this.autoClose = true;
                this.close();
            }
        }
    }

    @Override
    public <T> Page<T> pagingLoad(Class<T> clazz, SqlSource sqlSource, PageRequest pageRequest, Object... args) throws DaoException {

        try {

            Page<Map<String, Object>> pageMap = this.pagingLoad(sqlSource, pageRequest, args);

            List<Map<String, Object>> rows = pageMap.getContent();

            List<T> objs = new ArrayList<T>(rows.size());

            for (Map<String, Object> row : rows) {
                objs.add(this.ormProc(row, clazz));
            }

            return new Page<T>(pageMap, objs);

        } catch ( DaoException e ) {
            throw e;
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }



    @Override
    public List<String> columnLabels(String sql, Object... args) throws DaoException {
        return this.columnLabels(new TSql(sql), args);
    }


    @Override
    public <T> Val<T> getField(String sql, String fieldName, Object... args) throws DaoException {
        return this.getField(new TSql(sql), fieldName, args);
    }

    @Override
    public <T> List<T> loadFields(String sql, String fieldName, Object... args) throws DaoException {
        return this.loadFields(new TSql(sql), fieldName, args);
    }

    @Override
    public Val<Map<String, Object>> get(String sql, Object... args) throws DaoException {
        return this.get(new TSql(sql), args);
    }

    @Override
    public List<Map<String, Object>> load(String sql, Object... args) throws DaoException {
        return this.load(new TSql(sql), args);
    }

    @Override
    public <T> Val<T> get(Class<T> clazz, String sql, Object... args) throws DaoException {
        return this.get(clazz, new TSql(sql), args);
    }

    @Override
    public <T> List<T> load(Class<T> clazz, String sql, Object... args) throws DaoException {
        return this.load(clazz, new TSql(sql), args);
    }

    @Override
    public <T> Val<T> load(ResultSetCallback<T> rsCallback, String sql, Object... args) throws DaoException {
        return this.load(rsCallback, new TSql(sql), args);
    }



    @Override
    public List<Map<String, Object>> fetch(String sql, ResultInfo fetchRequest, Object... args) throws DaoException {
        return this.fetch( new TSql(sql) , fetchRequest , args );
    }

    @Override
    public <T> List<T> fetch(Class<T> clazz, String sql, ResultInfo fetchRequest, Object... args) throws DaoException {
        return this.fetch( clazz , new TSql(sql) , fetchRequest , args );
    }



    @Override
    public Page<Map<String, Object>> pagingLoad(String sql, PageRequest pageRequest, Object... args) throws DaoException {
        return this.pagingLoad(new TSql(sql), pageRequest, args);
    }

    @Override
    public <T> Page<T> pagingLoad(Class<T> clazz, String sql, PageRequest pageRequest, Object... args) throws DaoException {
        return this.pagingLoad(clazz, new TSql(sql), pageRequest, args);
    }

    @Override
    public int update(String sql, Object... args) throws DaoException {
        return this.update(new TSql(sql), args);
    }

    @Override
    public int[] batchUpdate(String sql, Collection<?> args) throws DaoException {
        return this.batchUpdate(new TSql(sql), args);
    }


    @Override
    public Connection getConnection() {
        return connection;
    }
}
