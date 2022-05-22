package h2o.dao.proc;

import h2o.dao.exception.DaoException;

import java.util.Map;

public interface OrmProcessor {

    <T> T proc(Map<String, Object> row, Class<T> clazz) throws DaoException;

}
