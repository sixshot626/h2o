package h2o.utils.key;


import h2o.dao.Dao;
import h2o.dao.DaoCallback;
import h2o.dao.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeyVersion {

    private static final Logger log = LoggerFactory.getLogger( KeyVersion.class.getName() );

    private static final String SELSEQ = DbUtil.sqlTable.getSql("selseq");
    private static final String INSSEQ = DbUtil.sqlTable.getSql("insseq");
    private static final String UPDSEQ = DbUtil.sqlTable.getSql("updseq");

    public static boolean incVersion( final String key ) {

        return DbUtil.getDb("common").tx( new DaoCallback<Boolean>() {

            @Override
            public Boolean doCallBack(Dao dao) throws Exception {

                if ( dao.update( UPDSEQ , "seqobj", key ) == 0 ) {

                    if ( dao.get(SELSEQ, "seqobj", key) == null ) try {
                        dao.update(INSSEQ, "seqobj", key);
                    } catch (Exception e) {
                        log.error("",e);
                    }

                    return dao.update( UPDSEQ , "seqobj", key ) > 0;

                }

                return true;
            }
        });

    }

    public static long getVersion( String key ) {

        return ( (Number)DbUtil.getDao("common").
                getField( SELSEQ, "seqno" ,"seqobj", key ) ).longValue();

    }

}