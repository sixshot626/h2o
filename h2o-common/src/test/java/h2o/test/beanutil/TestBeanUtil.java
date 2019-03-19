package h2o.test.beanutil;

import h2o.common.Tools;

public class TestBeanUtil {

    public static void main( String[] args ) {

        Aaa a = new Aaa();
        a.setAaaBBBccc( "aaaaa");
        a.setCCC( "cccccc");

        System.out.println( Tools.b.beanCopy( a , new Bbb() ) );

        System.out.println( Tools.bic.beanCopy( a , new Bbb() ) );

    }

}
