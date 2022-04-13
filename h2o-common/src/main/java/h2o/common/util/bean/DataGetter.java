package h2o.common.util.bean;

import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.jodd.bean.BeanUtil;

import java.util.Collection;
import java.util.List;

/**
 * Created by zhangjianwei on 2017/5/15.
 */
public class DataGetter {

    private final BeanUtil beanUtil ;

    private final Object data;

    private final boolean silent;

    public DataGetter(Object data) {
        this( data , false );
    }

    public DataGetter(Object data , boolean silent ) {
        this.data = data;
        this.silent = silent;
        beanUtil = silent ? BeanUtil.forcedSilent : BeanUtil.forced;
    }


    public <T> T get(String pname ) {
        return (T)beanUtil.getProperty(data,pname);
    }

    public DataGetter get2DG( String pname ) {
        return this.get2DG( pname , this.silent );
    }

    public DataGetter get2DG( String pname , boolean silent ) {
        return new DataGetter( this.get(pname) , silent );
    }

    public List<DataGetter> get2DGList( String pname ) {
        return this.get2DGList( pname , this.silent );
    }

    public List<DataGetter> get2DGList( String pname , boolean silent ) {

        Collection<Object> c = this.get( pname );

        if (CollectionUtil.isEmpty( c )) {

            return ListBuilder.newEmptyList();

        } else {

            List<DataGetter> dgList = ListBuilder.newList();

            for ( Object o : c ) {
                dgList.add( new DataGetter(o,silent) );
            }

            return dgList;
        }
    }


    public Object getData() {
        return data;
    }

    public boolean isSilent() {
        return silent;
    }

}
