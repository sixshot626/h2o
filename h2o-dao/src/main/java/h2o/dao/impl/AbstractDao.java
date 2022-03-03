package h2o.dao.impl;

import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageInfo;
import h2o.common.data.domain.PageRequest;
import h2o.common.lang.Val;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.Dao;
import h2o.dao.log.LogWriter;
import h2o.dao.ResultSetCallback;
import h2o.dao.exception.DaoException;
import h2o.dao.impl.sql.TSql;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import h2o.dao.sql.SqlSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao implements Dao {


	private ArgProcessor argProcessor;

	private OrmProcessor ormProcessor;

	private PagingProcessor pagingProcessor;

	private LogWriter logWriter;
	


	@Override
	public void setArgProcessor(ArgProcessor argProcessor) {
		this.argProcessor = argProcessor;
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
		return argProcessor;
	}

	protected OrmProcessor getOrmProcessor() {
		return ormProcessor;
	}

	protected PagingProcessor getPagingProcessor() {
		return pagingProcessor;
	}

	protected LogWriter getLogWriter() {
		return logWriter;
	}

	protected Map<String, Object> argProc(Object... args) {
		ArgProcessor _argProcessor = this.getArgProcessor();
		return _argProcessor.proc(args);
	}

	protected <T> T ormProc(Map<String, Object> row, Class<T> clazz) {
		OrmProcessor _ormProcessor = this.getOrmProcessor();
		return _ormProcessor.proc(row, clazz);
	}



	@Override
	public <T> Val<T> get(Class<T> clazz, SqlSource sqlSource, Object... args) throws DaoException {
		try {

			Val<Map<String, Object>> row = this.get(sqlSource, args);

			return row.isPresent() ? new Val<>( this.ormProc(row.getValue() , clazz) ) : Val.empty();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}



	@Override
	public <T> List<T> load(Class<T> clazz, SqlSource sqlSource, Object... args) throws DaoException {
		try {
			List<Map<String, Object>> rows = this.load(sqlSource, args);

			List<T> objs = new ArrayList<T>(rows.size());

			for (Map<String, Object> row : rows) {
				objs.add(this.ormProc(row, clazz));
			}

			return objs;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}



	@Override
	public Page<Map<String, Object>> pagingLoad(SqlSource sqlSource, PageRequest pageRequest, Object... args) {

		PagingProcessor _pagingProcessor = this.getPagingProcessor();

		Map<String, Object> paramMap = this.argProc(args);
		String sql = sqlSource.getSql( paramMap );

		Tuple2<String, String> t = _pagingProcessor.totalSql(sql);
		Val<Number> count = this.getField(  t.e0 , t.e1 , args  );

		PageInfo pageInfo = new PageInfo( pageRequest , count.get().longValue() );
		if ( pageInfo.getTotalElements() == 0L ) {
			return new Page<Map<String, Object>>( pageInfo , ListBuilder.<Map<String, Object>>newEmptyList() );
		}


		Tuple2<String, Map<String, Object>> p = _pagingProcessor.pagingSql(sql, pageRequest);
		paramMap.putAll( p.e1 );
		List<Map<String, Object>> records = this.load(p.e0, paramMap);

		return new Page<Map<String, Object>>(pageInfo , records);
	}

	@Override
	public <T> Page<T> pagingLoad(Class<T> clazz, SqlSource sqlSource, PageRequest pageRequest, Object... args) throws DaoException {

		Page<Map<String, Object>> pageMap = this.pagingLoad(sqlSource, pageRequest, args);

		List<Map<String, Object>> rows = pageMap.getContent();

		List<T> objs = new ArrayList<T>(rows.size());

		for (Map<String, Object> row : rows) {
			objs.add(this.ormProc(row, clazz));
		}

		return new Page<T>( pageMap , objs );

	}



	@Override
	public <T> Val<T> getField(String sql, String fieldName, Object... args) throws DaoException {
		return this.getField( new TSql(sql) , fieldName, args );
	}

	@Override
	public <T> List<T> loadFields(String sql, String fieldName, Object... args) throws DaoException {
		return this.loadFields( new TSql(sql) , fieldName, args );
	}

	@Override
	public Val<Map<String, Object>> get(String sql, Object... args) throws DaoException {
		return this.get( new TSql(sql) , args );
	}

	@Override
	public List<Map<String, Object>> load(String sql, Object... args) throws DaoException {
		return this.load( new TSql(sql), args );
	}

	@Override
	public <T> Val<T> get(Class<T> clazz, String sql, Object... args) throws DaoException {
		return this.get( clazz,  new TSql(sql), args );
	}

	@Override
	public <T> List<T> load(Class<T> clazz, String sql, Object... args) throws DaoException {
		return this.load( clazz, new TSql(sql), args );
	}

	@Override
	public <T> Val<T> load(ResultSetCallback<T> rsCallback, String sql, Object... args) throws DaoException {
		return this.load( rsCallback, new TSql(sql), args );
	}



	@Override
	public Page<Map<String, Object>> pagingLoad(String sql, PageRequest pageRequest, Object... args) throws DaoException {
		return this.pagingLoad( new TSql(sql), pageRequest , args );
	}

	@Override
	public <T> Page<T> pagingLoad(Class<T> clazz, String sql, PageRequest pageRequest, Object... args) throws DaoException {
		return this.pagingLoad(clazz, new TSql(sql), pageRequest , args );
	}

	@Override
	public int update(String sql, Object... args) throws DaoException {
		return this.update( new TSql(sql), args );
	}

	@Override
	public int[] batchUpdate(String sql, Collection<?> args) throws DaoException {
		return this.batchUpdate( new TSql(sql), args );
	}



}
