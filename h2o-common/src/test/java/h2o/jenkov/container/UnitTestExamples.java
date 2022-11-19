package h2o.jenkov.container;

import h2o.jenkov.container.script.ScriptFactoryBuilder;
import junit.framework.TestCase;
//import h2o.jenkov.testing.mock.impl.MockFactory;
//import h2o.jenkov.testing.mock.itf.IMock;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class UnitTestExamples extends TestCase {
    public void test(){
        IContainer           container     = new Container();
        ScriptFactoryBuilder scriptBuilder = new ScriptFactoryBuilder(container);

        scriptBuilder.addFactory("test = * h2o.jenkov.container.TestProduct().setValue1('value1');");
        TestProduct product = (TestProduct) container.instance("test");
        assertEquals("value1", product.getValue1());
    }

    public void test2(){

        /*
        IContainer           container     = new Container();
        ScriptFactoryBuilder scriptBuilder = new ScriptFactoryBuilder(container);

        scriptBuilder.addFactory("itfProxy = * h2o.jenkov.testing.mock.impl.MockFactory.createProxy((java.lang.Class)  $0);");
        scriptBuilder.addFactory("colProxy = * h2o.jenkov.testing.mock.impl.MockFactory.createProxy((java.lang.Object) $0);");
        scriptBuilder.addFactory("test     = * itfProxy(h2o.jenkov.container.ISomeInterface.class); ");

        Object test = container.instance("test");
        ISomeInterface testInterface = (ISomeInterface) test;
        assertTrue(test instanceof ISomeInterface);
        */
    }
}
