package h2o.jenkov.container.impl.factory.convert;

import h2o.jenkov.container.impl.factory.LocalFactoryBase;
import h2o.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class BooleanFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public BooleanFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return boolean.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return Boolean.parseBoolean(this.sourceFactory.instance(parameters, localProducts).toString());
    }
}
