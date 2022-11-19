package h2o.jenkov.container;

import h2o.jenkov.container.itf.factory.IGlobalFactory;
import h2o.jenkov.container.java.JavaFactory;

/**

 */
public class MyOtherFactory extends JavaFactory {

    public IGlobalFactory test = null;
    public String instance(Object ... parameters) {
        return "test2" + test.instance();
    }
}
