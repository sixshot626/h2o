package h2o.utils.key;


import h2o.dao.DbUtil;
import h2o.dao.transaction.JdbcTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeyVersion {

    private KeyVersion() {}

    private static final Logger log = LoggerFactory.getLogger( KeyVersion.class.getName() );


    private static final String SELSEQ;
    private static final String INSSEQ;
    private static final String UPDSEQ;

    static {

        String selSql;
        String insSql;
        String udpSql;

        try {

            String sqlFile = "KeyVersion";

            selSql = DbUtil.sqlTable.getSql( sqlFile , "selseq");
            insSql = DbUtil.sqlTable.getSql( sqlFile , "insseq");
            udpSql = DbUtil.sqlTable.getSql( sqlFile , "updseq");

        } catch ( Exception e ) {

            selSql = DbUtil.sqlTable.getSql("selseq");
            insSql = DbUtil.sqlTable.getSql("insseq");
            udpSql = DbUtil.sqlTable.getSql("updseq");
        }

        SELSEQ = selSql;
        INSSEQ = insSql;
        UPDSEQ = udpSql;

    }

    public static boolean incVersion( final String key ) {

        return DbUtil.tx( new JdbcTransactionManager( "common") ,

                dao-> {


                    if ( dao.update( UPDSEQ , "seqobj", key ) == 0 ) {

                        if ( dao.get(SELSEQ, "seqobj", key) == null ) try {
                            dao.update(INSSEQ, "seqobj", key);
                        } catch (Exception e) {
                            log.error("",e);
                        }

                        return dao.update( UPDSEQ , "seqobj", key ) > 0;

                    }

                    return true;
                });

    }

    public static long getVersion( String key ) {

        return ( DbUtil.getDao("common")
                .<Number>getField( SELSEQ, "seqno" ,"seqobj", key ) )
                .get().longValue();

    }

}