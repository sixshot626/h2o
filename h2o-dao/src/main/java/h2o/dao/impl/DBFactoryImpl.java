package h2o.dao.impl;

import h2o.common.dao.SqlTable;
import h2o.common.ioc.Factory;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.dao.DBFactory;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import h2o.dao.sql.SqlBuilder;
import h2o.dao.transaction.ScopeManager;
import h2o.dao.transaction.TransactionManager;

import java.util.Optional;

public class DBFactoryImpl implements DBFactory {

    protected static final String DBUTIL_BEANID = "dbUtil";
    protected static final String DB_BEANID = "db";

    protected static final String SQLTABLE_BEANID = "sqlTable";
    protected static final String SQLBUILDER_BEANID = "sqlBuilder";
    protected static final String SQLTEMPLATEUTIL_BEANID = "sqlTemplateUtil";


    protected static final String ARGPROCESSOR_BEANID = "argProcessor";
    protected static final String ORMPROCESSOR_BEANID = "ormProcessor";
    protected static final String PAGINGPROCESSOR_BEANID = "pagingProcessor";


    protected static final String SCOPEMANAGER_BEANID = "scopeManager";
    protected static final String TRANSACTIONMANAGER_BEANID = "transactionManager";

    protected final Factory factory;

    public DBFactoryImpl(Factory factory) {
        this.factory = factory;
    }

    @Override
    public SqlTable getSqlTable() {
        return factory.get(SQLTABLE_BEANID);
    }

    @Override
    public TemplateUtil getSqlTemplateUtil() {
        return factory.get(SQLTEMPLATEUTIL_BEANID);
    }

    @Override
    public SqlBuilder getSqlBuilder() {
        return factory.get(SQLBUILDER_BEANID);
    }

    @Override
    public ArgProcessor getArgProcessor() {
        return factory.get(ARGPROCESSOR_BEANID);
    }

    @Override
    public OrmProcessor getOrmProcessor() {
        return factory.get(ORMPROCESSOR_BEANID);
    }

    @Override
    public Optional<PagingProcessor> getPagingProcessor() {
        return Optional.ofNullable(factory.silentlyGet(PAGINGPROCESSOR_BEANID));
    }

    @Override
    public Optional<ScopeManager> getScopeManager() {
        return Optional.ofNullable(factory.silentlyGet(SCOPEMANAGER_BEANID));
    }

    @Override
    public Optional<TransactionManager> getTransactionManager() {
        return Optional.ofNullable(factory.silentlyGet(TRANSACTIONMANAGER_BEANID));
    }

    @Override
    public Db getDb(String dateSourceName) {
        return factory.get(DB_BEANID, dateSourceName);
    }

    @Override
    public DbUtil getDbUtil() {
        return factory.get(DBUTIL_BEANID);
    }
}
