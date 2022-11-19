package h2o.jenkov.container.script;

import h2o.jenkov.container.Container;
import h2o.jenkov.container.IContainer;
import h2o.jenkov.container.TestProductCasting;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

/**

 */
public class ParameterCastingTest extends TestCase {

    public void test() throws MalformedURLException {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * h2o.jenkov.container.TestProductCasting().setValue((int) 2);");
        TestProductCasting product = (TestProductCasting) container.instance("bean");

        assertNull(product.getValue1());
        assertNull(product.getValue2());
        assertEquals(2, product.getValue3());

        
        builder.addFactory("bean2 = * h2o.jenkov.container.TestProductCasting().setValue((String) 2);");
        product = (TestProductCasting) container.instance("bean2");

        assertEquals("2", product.getValue1());
        assertNull(product.getValue2());
        assertEquals(-1, product.getValue3());


        builder.addFactory("bean3 = * h2o.jenkov.container.TestProductCasting().setValue((URL) 'http://jenkov.com');");
        product = (TestProductCasting) container.instance("bean3");

        assertNull(product.getValue1());
        assertEquals(new URL("http://jenkov.com"), product.getValue2());
        assertEquals(-1, product.getValue3());



    }
}
