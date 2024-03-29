package h2o.jenkov.container.impl.factory.convert;

import h2o.jenkov.container.impl.factory.LocalFactoryBase;
import h2o.jenkov.container.itf.factory.ILocalFactory;

/**

 */
public class IntegerFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public IntegerFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return Integer.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return new Integer(this.sourceFactory.instance(parameters, localProducts).toString());
    }

}
