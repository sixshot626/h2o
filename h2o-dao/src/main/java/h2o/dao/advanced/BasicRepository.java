package h2o.dao.advanced;

import h2o.common.bean.page.Page;
import h2o.common.bean.page.PageRequest;
import h2o.common.collections.CollectionUtil;
import h2o.common.util.lang.GenericsUtil;

import java.util.List;

public class BasicRepository<E> {



    public void add(E entity) {
        this.createDaoBasicUtil(entity).add(entity);
    }

    public void batAdd(List<E> entities) {
        if (CollectionUtil.isNotBlank(entities)) {
            this.createDaoBasicUtil(entities.get(0)).batAdd(entities);
        }
    }

    public int edit(E entity) {
        return this.createDaoBasicUtil(entity).edit(entity);
    }

    public int editByUnique(E entity, String uniqueName) {
        return this.createDaoBasicUtil(entity).editByUnique(entity, uniqueName);
    }

    public int editByAttr(E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).editByAttr(entity, attrNames);
    }






    public E get(E entity) {
        return this.createDaoBasicUtil(entity).get(entity);
    }

    public E getAndLock(E entity) {
        return this.createDaoBasicUtil(entity).getAndLock(entity);
    }

    protected E get(E entity, boolean lock) {
        return this.createDaoBasicUtil(entity).get(entity, lock);
    }


    public E getByUnique( E entity, String uniqueName ) {
        return this.getByUnique( entity , false ,uniqueName );
    }

    public E getAndLockByUnique( E entity, String uniqueName ) {
        return this.getByUnique( entity , true ,uniqueName );
    }

    protected E getByUnique(E entity, boolean lock, String uniqueName ) {
        return this.createDaoBasicUtil(entity).getByUnique(entity,lock ,uniqueName );
    }


    public E getByAttr(E entity ,  String... attrNames) {
        return this.createDaoBasicUtil(entity).getByAttr(entity, false , attrNames );
    }

    public E getAndLockByAttr(E entity , String... attrNames) {
        return this.createDaoBasicUtil(entity).getByAttr(entity, true  , attrNames );
    }


    protected E getByAttr(E entity , boolean lock , String... attrNames) {
        return this.createDaoBasicUtil(entity).getByAttr(entity, lock, attrNames );
    }

    public List<E> loadByAttr(E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).loadByAttr(entity, attrNames);
    }

    public List<E> loadAll() {
        return this.createDaoBasicUtil(this.getEntityClass()).loadAll();
    }


    public Page<E> pagingLoadByAttr(PageRequest pageRequest, E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).pagingLoadByAttr(pageRequest, entity, attrNames);
    }

    public Page<E> pagingLoad(PageRequest pageRequest ) {
        return this.createDaoBasicUtil(this.getEntityClass()).pagingLoad(pageRequest);
    }




    public E selectOne( String[] fields , E entity ) {
        return this.createDaoBasicUtil(entity).selectOne( fields , entity);
    }

    public E selectOneAndLock( String[] fields , E entity) {
        return this.createDaoBasicUtil(entity).selectOneAndLock( fields , entity);
    }

    protected E selectOne( String[] fields , E entity, boolean lock) {
        return this.createDaoBasicUtil(entity).selectOne( fields , entity , lock );
    }


    public E selectOneByUnique( String[] fields , E entity, String uniqueName ) {
        return this.selectOneByUnique( fields , entity , false ,uniqueName );
    }

    public E selectOneAndLockByUnique(String[] fields ,  E entity, String uniqueName ) {
        return this.selectOneByUnique( fields , entity , true ,uniqueName );
    }

    protected E selectOneByUnique(String[] fields , E entity, boolean lock, String uniqueName ) {
        return this.createDaoBasicUtil(entity).selectOneByUnique( fields , entity,lock ,uniqueName );
    }


    public E selectOneByAttr(String[] fields , E entity ,  String... attrNames) {
        return this.createDaoBasicUtil(entity).selectOneByAttr( fields , entity, false , attrNames );
    }

    public E selectOneAndLockByAttr(String[] fields , E entity , String... attrNames) {
        return this.createDaoBasicUtil(entity).selectOneByAttr( fields , entity, true  , attrNames );
    }


    protected E selectOneByAttr( String[] fields , E entity , boolean lock , String... attrNames) {
        return this.createDaoBasicUtil(entity).selectOneByAttr( fields , entity , lock , attrNames );
    }




    public List<E> selectByAttr( String[] fields , E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).selectByAttr( fields , entity, attrNames);
    }

    public List<E> selectAll( String[] fields) {
        return this.createDaoBasicUtil(this.getEntityClass()).selectAll( fields );
    }



    public Page<E> pagingSelectByAttr(String[] fields, PageRequest pageRequest, E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).pagingSelectByAttr(fields, pageRequest, entity, attrNames);
    }

    public Page<E> pagingSelect(String[] fields, PageRequest pageRequest ) {
        return this.createDaoBasicUtil(this.getEntityClass()).pagingSelect(fields, pageRequest);
    }




    public int del(E entity) {
        return this.createDaoBasicUtil(entity).del(entity);
    }

    public int delByUnique(E entity, String uniqueName) {
        return this.createDaoBasicUtil(entity).delByUnique(entity, uniqueName);
    }

    public int delByAttr(E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).delByAttr(entity, attrNames);
    }




    protected DaoBasicUtil<E> createDaoBasicUtil( E entity ) {
        return new DaoBasicUtil<E>(entity.getClass());
    }

    protected DaoBasicUtil<E> createDaoBasicUtil( Class<?> entityClazz ) {
        return new DaoBasicUtil<E>(entityClazz);
    }

    protected Class<E> getEntityClass() {
        return (Class<E>) GenericsUtil.getSuperClassGenricType(this.getClass());
    }

}
