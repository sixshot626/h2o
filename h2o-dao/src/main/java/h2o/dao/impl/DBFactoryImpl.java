package h2o.dao.impl;

import h2o.common.dao.SqlTable;
import h2o.common.ioc.Factory;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.dao.*;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;

public class DBFactoryImpl implements DBFactory {

    protected static final String DBUTIL_BEANID                 = "dbUtil";
    protected static final String DB_BEANID                     = "db";

    protected static final String SQLTABLE_BEANID               = "sqlTable";
    protected static final String SQLBUILDER_BEANID             = "sqlBuilder";
    protected static final String SQLTEMPLATEUTIL_BEANID        = "sqlTemplateUtil";


    protected static final String ARGPROCESSOR_BEANID           = "argProcessor";
    protected static final String ORMPROCESSOR_BEANID           = "ormProcessor";
    protected static final String PAGINGPROCESSOR_BEANID        = "pagingProcessor";


    protected static final String SCOPEMANAGER_BEANID           = "scopeManager";
    protected static final String TRANSACTIONMANAGER_BEANID     = "transactionManager";

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
    public ScopeManager getScopeManager() {
        return factory.get(SCOPEMANAGER_BEANID);
    }

    @Override
    public TransactionManager getTransactionManager() {
        return factory.get(TRANSACTIONMANAGER_BEANID);
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
    public PagingProcessor getPagingProcessor() {
        return factory.get(PAGINGPROCESSOR_BEANID);
    }

    @Override
    public DbUtil getDbUtil() {
        return factory.get(DBUTIL_BEANID);
    }

    @Override
    public Db getDb(String dateSourceName) {
        return factory.get(DB_BEANID , dateSourceName );
    }
}
