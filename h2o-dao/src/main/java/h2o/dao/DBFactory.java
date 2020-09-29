package h2o.dao;

import h2o.common.dao.SqlTable;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import h2o.dao.sql.SqlBuilder;
import h2o.dao.transaction.ScopeManager;
import h2o.dao.transaction.TransactionManager;

import java.util.Optional;

public interface DBFactory {

    SqlTable getSqlTable();

    TemplateUtil getSqlTemplateUtil();

    SqlBuilder getSqlBuilder();

    ArgProcessor getArgProcessor();

    OrmProcessor getOrmProcessor();

    Optional<PagingProcessor> getPagingProcessor();

    Optional<ScopeManager> getScopeManager();

    Optional<TransactionManager> getTransactionManager();

    Db getDb(String dateSourceName);

    DbUtil getDbUtil();

}
