package h2o.jenkov.container.script;

import h2o.jenkov.container.Container;
import h2o.jenkov.container.IContainer;
import junit.framework.TestCase;

import java.util.Locale;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalizedResourceFactoryTest extends TestCase {

    public static Locale getLocale(){
        return new Locale("da", "dk");
    }

    public void test(){
        IContainer           container = new Container();
        ScriptFactoryBuilder builder   = new ScriptFactoryBuilder(container);

        builder.addFactory("UK     = 1 java.util.Locale.UK;");
        builder.addFactory("DK     = 1 java.util.Locale('da', 'dk');");
        builder.addFactory("locale = * h2o.jenkov.container.script.LocalizedResourceFactoryTest.getLocale(); ");
        builder.addFactory("aText  = L <UK:'hello', DK:'hej'>;");

        String aText = (String) container.instance("aText");
        assertEquals("hej", aText);
        
    }
}
