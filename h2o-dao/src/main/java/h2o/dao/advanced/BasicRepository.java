package h2o.dao.advanced;

import h2o.common.bean.page.Page;
import h2o.common.bean.page.PageRequest;
import h2o.common.bean.page.SortInfo;
import h2o.common.util.lang.GenericsUtil;
import h2o.dao.Dao;
import h2o.dao.DbUtil;

import java.util.List;

public abstract class BasicRepository<E> {

    private final String dataSourceName;

    private final Dao dao;

    protected BasicRepository() {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
    }

    protected BasicRepository(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
    }

    protected BasicRepository(Dao dao) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = dao;
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

    public int editByAttr(E entity, String... attrNames) {
        return createDaoBasicUtil().editByAttr(entity, attrNames);
    }

    public E get(E entity) {
        return createDaoBasicUtil().get(entity);
    }

    public E getAndLock(E entity) {
        return createDaoBasicUtil().getAndLock(entity);
    }

    public E getByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().getByUnique(entity, uniqueName);
    }

    public E getAndLockByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().getAndLockByUnique(entity, uniqueName);
    }

    public E getByAttr(E entity, String... attrNames) {
        return createDaoBasicUtil().getByAttr(entity, attrNames);
    }

    public E getAndLockByAttr(E entity, String... attrNames) {
        return createDaoBasicUtil().getAndLockByAttr(entity, attrNames);
    }

    public E selectOne(String[] fields, E entity) {
        return createDaoBasicUtil().selectOne(fields, entity);
    }

    public E selectOneAndLock(String[] fields, E entity) {
        return createDaoBasicUtil().selectOneAndLock(fields, entity);
    }

    public E selectOneByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().selectOneByUnique(fields, entity, uniqueName);
    }

    public E selectOneAndLockByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().selectOneAndLockByUnique(fields, entity, uniqueName);
    }

    public E selectOneByAttr(String[] fields, E entity, String... attrNames) {
        return createDaoBasicUtil().selectOneByAttr(fields, entity, attrNames);
    }

    public E selectOneAndLockByAttr(String[] fields, E entity, String... attrNames) {
        return createDaoBasicUtil().selectOneAndLockByAttr(fields, entity, attrNames);
    }

    public List<E> loadByAttr(E entity, String... attrNames) {
        return createDaoBasicUtil().loadByAttr(entity, attrNames);
    }

    public List<E> selectByAttr(String[] fields, E entity, String... attrNames) {
        return createDaoBasicUtil().selectByAttr(fields, entity, attrNames);
    }

    public List<E> loadByAttr(SortInfo[] sortInfos, E entity, String... attrNames) {
        return createDaoBasicUtil().loadByAttr(sortInfos, entity, attrNames);
    }

    public List<E> selectByAttr(String[] fields, SortInfo[] sortInfos, E entity, String... attrNames) {
        return createDaoBasicUtil().selectByAttr(fields, sortInfos, entity, attrNames);
    }

    public List<E> loadAll(SortInfo... sortInfos) {
        return createDaoBasicUtil().loadAll(sortInfos);
    }

    public List<E> selectAll(String[] fields, SortInfo... sortInfos) {
        return createDaoBasicUtil().selectAll(fields, sortInfos);
    }

    public Page<E> pagingLoadByAttr(PageRequest pageRequest, E entity, String... attrNames) {
        return createDaoBasicUtil().pagingLoadByAttr(pageRequest, entity, attrNames);
    }

    public Page<E> pagingSelectByAttr(String[] fields, PageRequest pageRequest, E entity, String... attrNames) {
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

    public int delByAttr(E entity, String... attrNames) {
        return createDaoBasicUtil().delByAttr(entity, attrNames);
    }






    private final Class<E> entityClazz = (Class<E>) GenericsUtil.getSuperClassGenricType(this.getClass());

    protected Class<E> getEntityClass() {
        return this.entityClazz;
    }

    protected Dao getDao() {
        return dao == null ? DbUtil.getDao( this.dataSourceName) : dao;
    }

    protected DaoBasicUtil<E> createDaoBasicUtil() {
        return new DaoBasicUtil<E>(this.getEntityClass(), this.getDao());
    }

}
