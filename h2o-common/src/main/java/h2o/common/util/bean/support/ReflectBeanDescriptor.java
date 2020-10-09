package h2o.common.util.bean.support;

import h2o.common.util.bean.BeanDescriptor;
import h2o.common.util.collection.ListBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ReflectBeanDescriptor implements BeanDescriptor {

    @Override
    public String[] getPrepNames(Object bean) {

        return bean instanceof Class ?
                getClassFieldNames((Class)bean) : getClassFieldNames(bean.getClass());


    }


    public static String[] getClassFieldNames( Class clazz ) {

        Field[] fields = clazz.getDeclaredFields();

        List<String> fieldNameList = ListBuilder.newList();
        for ( Field field : fields ) {
            if ( ! Modifier.isStrict( field.getModifiers() ) ) {
                fieldNameList.add( field.getName() );
            }
        }

        return fieldNameList.toArray( new String[0] );
    }


}
