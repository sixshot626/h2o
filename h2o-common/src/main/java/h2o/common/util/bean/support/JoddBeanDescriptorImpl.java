package h2o.common.util.bean.support;

import h2o.common.util.bean.BeanDescriptor;
import h2o.jodd.introspector.ClassDescriptor;
import h2o.jodd.introspector.ClassIntrospector;
import h2o.jodd.introspector.MethodDescriptor;
import h2o.jodd.introspector.PropertyDescriptor;

import java.util.ArrayList;

public class JoddBeanDescriptorImpl implements BeanDescriptor {

    @Override
    public String[] getPrepNames(Object bean) {

        ClassDescriptor classDescriptor = ClassIntrospector.lookup(bean.getClass());

        PropertyDescriptor[] propertyDescriptors = classDescriptor.getAllPropertyDescriptors();

        ArrayList<String> names = new ArrayList<String>(propertyDescriptors.length);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            MethodDescriptor getter = propertyDescriptor.getReadMethodDescriptor();
            if (getter != null) {
                if (getter.matchDeclared(false)) {
                    names.add(propertyDescriptor.getName());
                }
            }
        }

        return names.toArray(new String[names.size()]);
    }

}
