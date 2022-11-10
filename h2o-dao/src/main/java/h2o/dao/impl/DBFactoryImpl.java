package h2o.dao.impl;

import h2o.common.concurrent.factory.CachedCreator;
import h2o.common.concurrent.factory.InstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.ioc.Factory;
import h2o.common.lang.Val;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.common.util.lang.StringUtil;
import h2o.dao.DBFactory;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.page.PagingProcessor;
import h2o.dao.proc.ArgProcessor;
import h2o.dao.proc.OrmProcessor;
import h2o.dao.proc.RowProcessor;
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
    protected static final String ROWPROCESSOR_BEANID = "rowProcessor";
    protected static final String ORMPROCESSOR_BEANID = "ormProcessor";
    protected static final String PAGINGPROCESSOR_BEANID = "pagingProcessor";


    protected final Factory factory;

    private <T> T factoryGet(String id) {
        return factory.get(id);
    };

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


    private final CachedCreator<ArgProcessor> argProcessorCache
            = new CachedCreator<>(()->factoryGet(ARGPROCESSOR_BEANID));
    @Override
    public ArgProcessor getArgProcessor() {
        return argProcessorCache.get(true);
    }


    private final CachedCreator<RowProcessor> rowProcessorCache
            = new CachedCreator<>(()->factoryGet(ROWPROCESSOR_BEANID));
    @Override
    public RowProcessor getRowProcessor() {
        return rowProcessorCache.get(true);
    }


    private final CachedCreator<OrmProcessor> ormProcessorCache
            = new CachedCreator<>(()->factoryGet(ORMPROCESSOR_BEANID));
    @Override
    public OrmProcessor getOrmProcessor() {
        return ormProcessorCache.get(true);
    }


    private final InstanceTable<String, Val<PagingProcessor>> pagingProcessorTable
            = new InstanceTable<>(new InstanceFactory<Val<PagingProcessor>>() {

        @Override
        public Val<PagingProcessor> create(Object name) {
            return new Val<>( factory.silentlyGet(StringUtil.build(name, "_", PAGINGPROCESSOR_BEANID)) );
        }

        @Override
        public void free(Object id, Val<PagingProcessor> ins) {
        }

        @Override
        public void destroy(Val<PagingProcessor> ins) {
        }
    });

    @Override
    public Optional<PagingProcessor> getPagingProcessor(String name) {
        Val<PagingProcessor> pagingProcessorVal = pagingProcessorTable.getAndCreateIfAbsent(name);
        if (pagingProcessorVal.isPresent()) {
            return Optional.of( pagingProcessorVal.get() );
        } else {
            return Optional.empty();
        }
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
