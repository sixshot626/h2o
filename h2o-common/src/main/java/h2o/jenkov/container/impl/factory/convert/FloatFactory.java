package h2o.jenkov.container.impl.factory.convert;

import h2o.jenkov.container.impl.factory.LocalFactoryBase;
import h2o.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class FloatFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public FloatFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return float.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return Float.parseFloat(this.sourceFactory.instance(parameters, localProducts).toString());
    }
}
