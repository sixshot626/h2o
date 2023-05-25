package h2o.dao;

import h2o.common.Mode;
import h2o.common.ioc.ButterflyFactory;
import h2o.dao.impl.DBFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangjianwei on 2016/11/14.
 */
public final class DBFactoryProvider {

    private static final Logger log = LoggerFactory.getLogger(DBFactoryProvider.class.getName());

    public static DBFactory getDbFactory() {

        ButterflyFactory dbButterflyFactory = new ButterflyFactory("db", Mode.prodMode ? "db.bcs" : "db-" + Mode.config + ".bcs");

        DBFactory factory = null;

        try {
            factory = dbButterflyFactory.get("dbFactory");
        } catch (Exception e) {
        }


        if (factory != null) {

            try {
                dbButterflyFactory.dispose();
            } catch (Exception e) {
            }

        } else {

            factory = new DBFactoryImpl(dbButterflyFactory);

        }

        log.info("DBFactory : {}", factory.getClass().getName());

        return factory;

    }

}
