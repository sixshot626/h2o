package h2o.dao.advanced;

import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.ResultInfo;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.Val;
import h2o.common.util.lang.GenericsUtil;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.structure.ColumnMeta;
import h2o.dao.structure.TableStruct;
import h2o.dao.structure.TableStructParser;

import java.util.List;

public abstract class BasicRepository<E> {

    private final String dataSourceName;

    private final Dao dao;

    private final Class<E> entityClazz;

    private final TableStruct tableStruct;


    protected BasicRepository() {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
        @SuppressWarnings("unchecked")
        Class<E> clazz = (Class<E>) GenericsUtil.getSuperClassGenricType(this.getClass());
        this.entityClazz = clazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz).get();
    }

    protected BasicRepository(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        @SuppressWarnings("unchecked")
        Class<E> clazz = (Class<E>) GenericsUtil.getSuperClassGenricType(this.getClass());
        this.entityClazz = clazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz).get();
    }

    protected BasicRepository(Dao dao) {
        this.dataSourceName = null;
        this.dao = dao;
        @SuppressWarnings("unchecked")
        Class<E> clazz = (Class<E>) GenericsUtil.getSuperClassGenricType(this.getClass());
        this.entityClazz = clazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz).get();
    }

    protected BasicRepository(String dataSourceName , Class<E> entityClazz) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.entityClazz = entityClazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz).get();
    }

    protected BasicRepository(Dao dao , Class<E> entityClazz) {
        this.dataSourceName = null;
        this.dao = dao;
        this.entityClazz = entityClazz;
        this.tableStruct = TableStructParser.parse(this.entityClazz).get();
    }

    protected BasicRepository(String dataSourceName , Class<E> entityClazz , TableStruct tableStruct) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
        this.entityClazz = entityClazz;
        this.tableStruct = tableStruct;
    }

    protected BasicRepository(Dao dao , Class<E> entityClazz , TableStruct tableStruct) {
        this.dataSourceName = null;
        this.dao = dao;
        this.entityClazz = entityClazz;
        this.tableStruct = tableStruct;
    }




    public void add(E entity) {
        createDaoBasicUtil().add(entity);
    }

    public void batAdd(List<E> entities) {
        createDaoBasicUtil().batAdd(entities);
    }


    public int edit(E entity) {
        return createDaoBasicUtil().edit(entity);
    }

    public int editByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().editByUnique(entity, uniqueName);
    }

    public int editByAttr(E entity, String attrName, String... more) {
        return createDaoBasicUtil().editByAttr(entity, attrName, more);
    }

    public int editByAttr(E entity, List<String> attrNames) {
        return createDaoBasicUtil().editByAttr(entity, attrNames);
    }

    public int editWhere(E entity, String where, Object... args) {
        return createDaoBasicUtil().editWhere(entity, where, args);
    }


    public int edit(String[] fields, E entity) {
        return createDaoBasicUtil().edit(fields, entity);
    }

    public int editByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().editByUnique(fields, entity, uniqueName);
    }

    public int editByAttr(String[] fields, E entity, String attrName, String... more) {
        return createDaoBasicUtil().editByAttr(fields, entity, attrName, more);
    }

    public int editByAttr(String[] fields, E entity, List<String> attrNames) {
        return createDaoBasicUtil().editByAttr(fields, entity, attrNames);
    }

    public int editWhere(String[] fields, E entity, String where, Object... args) {
        return createDaoBasicUtil().editWhere(fields, entity, where, args);
    }


    public int update(E entity) {
        return createDaoBasicUtil().update(entity);
    }

    public int updateByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().updateByUnique(entity, uniqueName);
    }

    public int updateByAttr(E entity, String attrName, String... more) {
        return createDaoBasicUtil().updateByAttr(entity, attrName, more);
    }

    public int updateByAttr(E entity, List<String> attrNames) {
        return createDaoBasicUtil().updateByAttr(entity, attrNames);
    }

    public int updateWhere(E entity, String where, Object... args) {
        return createDaoBasicUtil().updateWhere(entity, where, args);
    }


    public int update(String[] fields, E entity) {
        return createDaoBasicUtil().update(fields, entity);
    }

    public int updateByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().updateByUnique(fields, entity, uniqueName);
    }

    public int updateByAttr(String[] fields, E entity, String attrName, String... more) {
        return createDaoBasicUtil().updateByAttr(fields, entity, attrName, more);
    }

    public int updateByAttr(String[] fields, E entity, List<String> attrNames) {
        return createDaoBasicUtil().updateByAttr(fields, entity, attrNames);
    }

    public int updateWhere(String[] fields, E entity, String where, Object... args) {
        return createDaoBasicUtil().updateWhere(fields, entity, where, args);
    }


    public Val<E> get(E entity) {
        return createDaoBasicUtil().get(entity);
    }

    public Val<E> getAndLock(E entity) {
        return createDaoBasicUtil().getAndLock(entity);
    }

    public Val<E> getByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().getByUnique(entity, uniqueName);
    }

    public Val<E> getAndLockByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().getAndLockByUnique(entity, uniqueName);
    }

    public Val<E> getByAttr(E entity, String attrName, String... more) {
        return createDaoBasicUtil().getByAttr(entity, attrName, more);
    }

    public Val<E> getByAttr(E entity, List<String> attrNames) {
        return createDaoBasicUtil().getByAttr(entity, attrNames);
    }

    public Val<E> getAndLockByAttr(E entity, String attrName, String... more) {
        return createDaoBasicUtil().getAndLockByAttr(entity, attrName, more);
    }

    public Val<E> getAndLockByAttr(E entity, List<String> attrNames) {
        return createDaoBasicUtil().getAndLockByAttr(entity, attrNames);
    }

    public Val<E> selectOne(String[] fields, E entity) {
        return createDaoBasicUtil().selectOne(fields, entity);
    }

    public Val<E> selectOneAndLock(String[] fields, E entity) {
        return createDaoBasicUtil().selectOneAndLock(fields, entity);
    }

    public Val<E> selectOneByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().selectOneByUnique(fields, entity, uniqueName);
    }

    public Val<E> selectOneAndLockByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().selectOneAndLockByUnique(fields, entity, uniqueName);
    }

    public Val<E> selectOneByAttr(String[] fields, E entity, String attrName, String... more) {
        return createDaoBasicUtil().selectOneByAttr(fields, entity, attrName, more);
    }

    public Val<E> selectOneByAttr(String[] fields, E entity, List<String> attrNames) {
        return createDaoBasicUtil().selectOneByAttr(fields, entity, attrNames);
    }

    public Val<E> selectOneAndLockByAttr(String[] fields, E entity, String attrName, String... more) {
        return createDaoBasicUtil().selectOneAndLockByAttr(fields, entity, attrName, more);
    }

    public Val<E> selectOneAndLockByAttr(String[] fields, E entity, List<String> attrNames) {
        return createDaoBasicUtil().selectOneAndLockByAttr(fields, entity, attrNames);
    }

    public List<E> loadByAttr(E entity, String attrName, String... more) {
        return createDaoBasicUtil().loadByAttr(entity, attrName, more);
    }

    public List<E> loadByAttr(E entity, List<String> attrNames) {
        return createDaoBasicUtil().loadByAttr(entity, attrNames);
    }

    public List<E> selectByAttr(String[] fields, E entity, String attrName, String... more) {
        return createDaoBasicUtil().selectByAttr(fields, entity, attrName, more);
    }

    public List<E> selectByAttr(String[] fields, E entity, List<String> attrNames) {
        return createDaoBasicUtil().selectByAttr(fields, entity, attrNames);
    }

    public List<E> loadByAttr(SortInfo[] sortInfos, E entity, String attrName, String... more) {
        return createDaoBasicUtil().loadByAttr(sortInfos, entity, attrName, more);
    }

    public List<E> loadByAttr(SortInfo[] sortInfos, E entity, List<String> attrNames) {
        return createDaoBasicUtil().loadByAttr(sortInfos, entity, attrNames);
    }

    public List<E> selectByAttr(String[] fields, SortInfo[] sortInfos, E entity, String attrName, String... more) {
        return createDaoBasicUtil().selectByAttr(fields, sortInfos, entity, attrName, more);
    }

    public List<E> selectByAttr(String[] fields, SortInfo[] sortInfos, E entity, List<String> attrNames) {
        return createDaoBasicUtil().selectByAttr(fields, sortInfos, entity, attrNames);
    }

    public List<E> loadAll(SortInfo... sortInfos) {
        return createDaoBasicUtil().loadAll(sortInfos);
    }

    public List<E> selectAll(String[] fields, SortInfo... sortInfos) {
        return createDaoBasicUtil().selectAll(fields, sortInfos);
    }





    public List<E> fetchByAttr( ResultInfo fetchRequest, E entity, String attrName, String... more) {
        return createDaoBasicUtil().fetchByAttr( fetchRequest, entity, attrName, more);
    }

    public List<E> fetchByAttr( ResultInfo fetchRequest, E entity, List<String> attrNames) {
        return createDaoBasicUtil().fetchByAttr( fetchRequest, entity, attrNames);
    }

    public List<E> fetchSelectByAttr(String[] fields, ResultInfo fetchRequest , E entity, String attrName, String... more) {
        return createDaoBasicUtil().fetchSelectByAttr(fields, fetchRequest, entity, attrName, more);
    }

    public List<E> fetchSelectByAttr(String[] fields,  ResultInfo fetchRequest , E entity, List<String> attrNames) {
        return createDaoBasicUtil().fetchSelectByAttr(fields, fetchRequest, entity, attrNames);
    }

    public List<E> fetch( ResultInfo fetchRequest ) {
        return createDaoBasicUtil().fetch( fetchRequest );
    }

    public List<E> fetchSelect(String[] fields, ResultInfo fetchRequest) {
        return createDaoBasicUtil().fetchSelect(fields, fetchRequest);
    }





    public Page<E> pagingLoadByAttr(PageRequest pageRequest, E entity, String attrName, String... more) {
        return createDaoBasicUtil().pagingLoadByAttr(pageRequest, entity, attrName, more);
    }

    public Page<E> pagingLoadByAttr(PageRequest pageRequest, E entity, List<String> attrNames) {
        return createDaoBasicUtil().pagingLoadByAttr(pageRequest, entity, attrNames);
    }

    public Page<E> pagingSelectByAttr(String[] fields, PageRequest pageRequest, E entity, String attrName, String... more) {
        return createDaoBasicUtil().pagingSelectByAttr(fields, pageRequest, entity, attrName, more);
    }

    public Page<E> pagingSelectByAttr(String[] fields, PageRequest pageRequest, E entity, List<String> attrNames) {
        return createDaoBasicUtil().pagingSelectByAttr(fields, pageRequest, entity, attrNames);
    }

    public Page<E> pagingLoad(PageRequest pageRequest) {
        return createDaoBasicUtil().pagingLoad(pageRequest);
    }

    public Page<E> pagingSelect(String[] fields, PageRequest pageRequest) {
        return createDaoBasicUtil().pagingSelect(fields, pageRequest);
    }

    public int del(E entity) {
        return createDaoBasicUtil().del(entity);
    }

    public int delByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().delByUnique(entity, uniqueName);
    }

    public int delByAttr(E entity, String attrName, String... more) {
        return createDaoBasicUtil().delByAttr(entity, attrName, more);
    }

    public int delByAttr(E entity, List<String> attrNames) {
        return createDaoBasicUtil().delByAttr(entity, attrNames);
    }


    public int delWhere(E entity, String where, Object... args) {
        return createDaoBasicUtil().delWhere(entity, where, args);
    }




    protected Class<E> getEntityClass() {
        return this.entityClazz;
    }

    protected TableStruct getTableStruct() {
        return this.tableStruct;
    }

    protected Dao getDao() {
        return dao == null ? DbUtil.getDao(this.dataSourceName) : dao;
    }


    protected BasicUtil<E> createDaoBasicUtil() {
        return new BasicUtil<>(this.getDao(), this.getEntityClass(), this.getTableStruct());
    }


    public String tableName() {
        return this.getTableStruct().tableName();
    }

    public String column(String attrName) {
        ColumnMeta columnMeta = this.getTableStruct().getColumn(attrName);
        return columnMeta.colName;
    }


}
