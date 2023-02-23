package h2o.dao;


import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.common.util.collection.MapBuilder;
import h2o.dao.exception.DaoException;
import h2o.dao.sql.SqlBuilder;
import h2o.dao.sql.SqlTable;
import h2o.dao.transaction.JdbcTransactionManager;
import h2o.dao.transaction.TransactionCallback;
import h2o.dao.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;

public final class DbUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DbUtil.class.getName());

    public static final String DEFAULT_DATASOURCE_NAME = "default";

    public static final DBFactory DBFACTORY = DBFactoryProvider.getDbFactory();

    public static final SqlTable sqlTable = newSqlTable();

    public static SqlTable newSqlTable() {
        return DBFACTORY.getSqlTable();
    }


   public static final SqlBuilder sqlBuilder() {
        return DBFACTORY.getSqlBuilder();
   }
    public static final TemplateUtil sqlTemplateUtil = DBFACTORY.getSqlTemplateUtil();


    private final Map<String, DataSource> dsMap = MapBuilder.newConcurrentHashMap();

    public void setDataSources(Map<String, DataSource> dataSources) {
        dsMap.putAll(dataSources);
    }

    public void addDataSource(String name, DataSource ds) {
        dsMap.put(name, ds);
    }


    public static DataSource getDataSource() {
        return getDataSource(DEFAULT_DATASOURCE_NAME);
    }

    public static DataSource getDataSource(String name) {

        LOG.debug(" getDataSource('{}') ... ", name);

        DataSource ds = DBFACTORY.getDbUtil().dsMap.get(name);
        if (ds == null) {
            throw new DaoException("DataSource [" + name + "] undefined.");
        }

        return ds;
    }


    public static Db getDb() {
        return getDb(DEFAULT_DATASOURCE_NAME);
    }

    public static Db getDb(String name) {
        return DBFACTORY.getDb(name);
    }


    public static Dao getDao() {
        return getDb().getDao();
    }

    public static Dao getDao(String name) {
        return getDb(name).getDao();
    }


    public static <T> T qx(String name, TransactionCallback<T> txCallback) {

        Dao dao = null;

        try {
            dao = getDao(name);
            dao.setAutoClose(false);

            return txCallback.doInTransaction(dao);

        } catch (RuntimeException | Error ex) {
            // Transactional code threw application exception -> rollback
            LOG.debug("doCallback", ex);
            throw ex;
        } catch (Throwable ex) {
            // Transactional code threw unexpected exception -> rollback
            LOG.debug("doCallback", ex);
            throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
        } finally {
            if (dao != null) {
                try {
                    dao.close();
                } catch (Exception ex) {
                    LOG.error("Dao close", ex);
                }
            }
        }
    }


    public static <T> T tx(String name, TransactionCallback<T> txCallback) {
        return tx(new JdbcTransactionManager(name), txCallback);
    }

    public static <T> T tx(TransactionManager txManager, TransactionCallback<T> txCallback) {

        Dao dao = null;

        try {

            dao = txManager.getDao();

            T t = txCallback.doInTransaction(dao);

            txManager.commit();

            return t;

        } catch (RuntimeException | Error ex) {
            // Transactional code threw application exception -> rollback
            LOG.debug("doCallback", ex);
            txManager.rollback();
            throw ex;
        } catch (Throwable ex) {
            // Transactional code threw unexpected exception -> rollback
            LOG.debug("doCallback", ex);
            txManager.rollback();
            throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
        } finally {

            if (dao != null) {
                try {
                    dao.close();
                } catch (Exception ex) {
                    LOG.error("Dao close", ex);
                }
            }

            if (txManager instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) txManager).close();
                } catch (Exception ex) {
                    LOG.error("TransactionManager close", ex);
                }
            }

        }
    }

}
