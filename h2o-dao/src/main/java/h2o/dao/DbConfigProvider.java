package h2o.dao;

import h2o.common.Mode;
import h2o.common.ioc.ButterflyFactory;
import h2o.common.ioc.Factory;

/**
 * Created by zhangjianwei on 2016/11/14.
 */
public final class DbConfigProvider {

    public static Factory getDbConfig() {

        ButterflyFactory dbButterflyFactory = new ButterflyFactory( "db" , Mode.prodMode ? "db.bcs" : "db." + Mode.name + ".bcs" );

        Factory factory = null;

        try {
            factory = dbButterflyFactory.get("dbConfig");
        } catch ( Exception e ) {
        }


        if ( factory != null ) {

            try {
                dbButterflyFactory.dispose();
            } catch ( Exception e ) {
            }

            return factory;


        } else {

            return dbButterflyFactory;

        }
    }

}
