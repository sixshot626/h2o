package h2o.jenkov.container.script;

import h2o.jenkov.container.Container;
import h2o.jenkov.container.IContainer;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;

/**

 */
public class Bugs extends TestCase {

    public void testExtraParanthesesErrorMessage(){
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        String script = "test1 = * h2o.jenkov.container.TestProduct()); \n" +
                        "test2 = * h2o.jenkov.container.TestProduct();  "  ;

        ByteArrayInputStream input = new ByteArrayInputStream(script.getBytes());

        try{
            builder.addFactories(input);
            fail("should throw exception because of extra parantheses in script");
        } catch(ParserException e){
            assertTrue(e.getMessage().indexOf("Expected token ; but found )") > -1);
        }


    }

    public void testFactoryTailErrorMessage(){
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        String script = "test1 = * h2o.jenkov.container.TestProduct().setValue1('A value')); ";

        ByteArrayInputStream input = new ByteArrayInputStream(script.getBytes());

        try{
            builder.addFactories(input);
            fail("should throw exception because of extra parantheses in script");
        } catch (ParserException e){
            assertTrue(e.getMessage().indexOf("Expected token ; but found )") > -1);
        }


    }
}
