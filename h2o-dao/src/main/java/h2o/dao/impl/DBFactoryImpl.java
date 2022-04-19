package h2o.dao.impl;

import h2o.common.concurrent.factory.InstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.ioc.Factory;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.common.util.lang.StringUtil;
import h2o.dao.DBFactory;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import h2o.dao.sql.SqlBuilder;
import h2o.dao.sql.SqlTable;

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
    public Optional<PagingProcessor> getPagingProcessor(String name) {
        return Optional.ofNullable(factory.silentlyGet(StringUtil.build(name, "_", PAGINGPROCESSOR_BEANID)));
    }


    private final InstanceTable<String, Db> dbTable = new InstanceTable<>(new InstanceFactory<Db>() {
        @Override
        public Db create(Object name) {
            return createDb((String) name);
        }

        @Override
        public void free(Object id, Db ins) {
        }

        @Override
        public void destroy(Db ins) {
        }
    });

    @Override
    public Db getDb(String name) {
        return dbTable.getAndCreateIfAbsent(name);
    }

    @Override
    public Db createDb(String name) {
        return factory.get(DB_BEANID, name);
    }

    @Override
    public DbUtil getDbUtil() {
        return factory.get(DBUTIL_BEANID);
    }
}
