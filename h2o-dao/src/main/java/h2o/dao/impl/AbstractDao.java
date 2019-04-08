package h2o.dao.impl;

import h2o.common.bean.page.Page;
import h2o.common.bean.page.PageInfo;
import h2o.common.bean.page.PageRequest;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.collections.tuple.Tuple2;
import h2o.dao.Dao;
import h2o.dao.ResultSetCallback;
import h2o.dao.SqlSource;
import h2o.dao.exception.DaoException;
import h2o.dao.impl.sql.TSql;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao implements Dao {

    private static final Logger log = LoggerFactory.getLogger( AbstractDao.class.getName() );

	private volatile ArgProcessor argProcessor;

	private volatile OrmProcessor ormProcessor;

	private volatile PagingProcessor pagingProcessor;
	


	public void setArgProcessor(ArgProcessor argProcessor) {
		this.argProcessor = argProcessor;
	}

	public void setOrmProcessor(OrmProcessor ormProcessor) {
		this.ormProcessor = ormProcessor;
	}

    public void setPagingProcessor(PagingProcessor pagingProcessor) {
        this.pagingProcessor = pagingProcessor;
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


	protected Map<String, Object> argProc(Object... args) {
		ArgProcessor _argProcessor = this.getArgProcessor();
		return _argProcessor.proc(args);
	}

	protected <T> T ormProc(Map<String, Object> row, Class<T> clazz) {
		OrmProcessor _ormProcessor = this.getOrmProcessor();
		return _ormProcessor.proc(row, clazz);
	}



	@Override
	public <T> T get(Class<T> clazz, SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> row = this.get(sqlSource, args);

			return this.ormProc(row, clazz);
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
		Number count = this.getField(  t.e0 , t.e1 , args  );

		PageInfo pageInfo = new PageInfo( pageRequest , count.longValue() );
		if ( pageInfo.getTotalRecord() == 0L ) {
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

		List<Map<String, Object>> rows = pageMap.getRecords();

		List<T> objs = new ArrayList<T>(rows.size());

		for (Map<String, Object> row : rows) {
			objs.add(this.ormProc(row, clazz));
		}

		return new Page<T>( pageMap.getPageInfo() , objs );

	}



	@Override
	public <T> T getField(String sql, String fieldName, Object... args) throws DaoException {
		return this.getField( new TSql(sql) , fieldName, args );
	}

	@Override
	public <T> List<T> loadFields(String sql, String fieldName, Object... args) throws DaoException {
		return this.loadFields( new TSql(sql) , fieldName, args );
	}

	@Override
	public Map<String, Object> get(String sql, Object... args) throws DaoException {
		return this.get( new TSql(sql) , args );
	}

	@Override
	public List<Map<String, Object>> load(String sql, Object... args) throws DaoException {
		return this.load( new TSql(sql), args );
	}

	@Override
	public <T> T get(Class<T> clazz, String sql, Object... args) throws DaoException {
		return this.get( clazz,  new TSql(sql), args );
	}

	@Override
	public <T> List<T> load(Class<T> clazz, String sql, Object... args) throws DaoException {
		return this.load( clazz, new TSql(sql), args );
	}

	@Override
	public <T> T load(ResultSetCallback<T> rsCallback, String sql, Object... args) throws DaoException {
		return this.load( rsCallback, new TSql(sql), args );
	}



	@Override
	public Page<Map<String, Object>> pagingLoad(String sql, PageRequest pageRequest, Object... args) {
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



	@Override
	public Connection getConnection() throws SQLException {
		return this.getDataSource().getConnection();
	}


}
