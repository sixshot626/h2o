package h2o.utils.key;


import h2o.common.collections.tuple.Tuple2;
import h2o.common.collections.tuple.TupleUtil;
import h2o.common.concurrent.LockMap;
import h2o.common.math.IntArith;
import h2o.dao.Dao;
import h2o.dao.DaoCallback;
import h2o.dao.DbUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class KeyGen {

    private static final Logger log = LoggerFactory.getLogger( KeyGen.class.getName() );

    public static final String DEFAULT_CYCLICSPACE = "_null_";

    static final int RETRYTIMES = 10;

    private static final LockMap lockMap = new LockMap();


    private static final String SELSEQ;
    private static final String INSSEQ;
    private static final String UPDSEQ;

    static {

        String selSql;
        String insSql;
        String udpSql;

        try {

            String sqlFile = "KeyGen";

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



    static String leftPad(String sn, int length) {
        return StringUtils.leftPad(sn, length, '0');
    }


    public static String getKey(String seq, int length) {
        return leftPad(getKey(seq), length);
    }

    public static String getKey(String seqObj, String cyclicSpace, int length) {
        return leftPad(getKey( seqObj, cyclicSpace ), length);
    }


    public static String getKey(String seqObj, CyclicSpaceSetter cyclicSpaceSetter , int length) {
        return leftPad(getKey( seqObj, cyclicSpaceSetter ), length);
    }


    public static String getKey(String seq) {
        return getKey(seq, (String)null);
    }


    public static String getKey(String seqObj, String cyclicSpace) {
        cyclicSpace = StringUtils.isBlank(cyclicSpace) ? DEFAULT_CYCLICSPACE : cyclicSpace;
        return getKey( seqObj , new DefaultCyclicSpaceSetter(cyclicSpace) );
    }


    public static String getKey( String seqObj , CyclicSpaceSetter cyclicSpaceSetter ) {

        Lock lock = lockMap.getLock(seqObj);
        lock.lock();
        try {

            String k = null;
            int n = 0;
            while (((k = incAndUpdateKey(seqObj, cyclicSpaceSetter)) == null) && (n++ < RETRYTIMES)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(20L);
                } catch (InterruptedException localInterruptedException) {
                }
            }

            if (k == null) {
                throw new RuntimeException("getKey 失败!!!");
            }

            log.info("getKey({})============>{}", seqObj, k);

            return k;

        } finally {
            lock.unlock();
        }
    }



    public static BigInteger getNumberKey(String seq) {
        return new BigInteger(getKey(seq));
    }

    public static BigInteger getNumberKey(String seqObj, String cyclicSpace) {
        return new BigInteger(getKey(seqObj, cyclicSpace));
    }

    public static BigInteger getNumberKey(String seqObj, CyclicSpaceSetter cyclicSpaceSetter) {
        return new BigInteger(getKey(seqObj, cyclicSpaceSetter));
    }


    private static String incAndUpdateKey(final String key, final CyclicSpaceSetter cyclicSpaceSetter) {
        Tuple2<String, String> kk = incAndUpdateKey(key, cyclicSpaceSetter, "1");
        return kk == null ? null : kk.e0;
    }


    /**
     * @return 新值,原值
     */
    static Tuple2<String, String> incAndUpdateKey(final String key, final CyclicSpaceSetter cyclicSpaceSetter , final String incNum) {

        final String[] rr = {"-1", "-1"};

        DbUtil.getDb("common" ).tx( new DaoCallback<Void>() {

            @Override
            public Void doCallback( Dao dao , Object scopeObj ) throws Exception {

                Map<String,Object> m =  dao.get( SELSEQ, "seqobj", key );

                if ( m == null ) {

                    try {

                        dao.update( INSSEQ , "seqobj", key , "cyclicspace" , DEFAULT_CYCLICSPACE );

                    } catch (Exception e) {
                        log.error("",e);
                    }

                    rr[0] = null;

                } else {

                    try {

                        String oldCyc = (String)m.get("cyclicspace");
                        String oldNo  = (String)m.get("seqno");

                        String cyclicSpace = cyclicSpaceSetter.getNewCyclicSpace( oldCyc , oldNo );
                        if ( cyclicSpace == null ) {
                            cyclicSpace = DEFAULT_CYCLICSPACE;
                        }

                        String r0 = ( cyclicSpace.equals( oldCyc) ) ?  oldNo : "0";

                        String r = IntArith.add( r0 , incNum );

                        dao.update( UPDSEQ , "seqobj", key  , "cyclicspace" , cyclicSpace , "seqno" , r );

                        rr[0] = r;
                        rr[1] = r0;

                    } catch ( Exception e ) {
                        log.error("",e);
                        rr[0] = null;
                    }

                }

                return null;
            }

        });


        return rr[0] == null ? null : TupleUtil.t( rr[0], rr[1] );

    }





}