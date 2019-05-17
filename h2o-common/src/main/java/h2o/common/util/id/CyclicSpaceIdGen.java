package h2o.common.util.id;

import org.apache.commons.lang.StringUtils;

public class CyclicSpaceIdGen {

    private String cyclicSpace;

    private long sequence;

    public CyclicSpaceIdGen( String cyclicSpace ) {
        this.cyclicSpace = cyclicSpace;
    }

    public CyclicSpaceIdGen( String cyclicSpace , long initVal ) {
        this.cyclicSpace = cyclicSpace;
        this.sequence = initVal;
    }

    public String nextKey( String cyclicSpace , int n ) {
        return cyclicSpace + nextId( cyclicSpace , n );
    }

    public String nextId( String cyclicSpace , int n ) {
        return StringUtils.leftPad( Long.toString( nextNumberId( cyclicSpace ) ) , n , '0');
    }

    public synchronized long nextNumberId( String cyclicSpace ) {

        if ( equalsCyclicSpace( cyclicSpace ) ) {
            if ( ++sequence > 0 ) {
               return sequence;
            }
        } else {
            this.cyclicSpace = cyclicSpace;
        }

        sequence = 1L;

        return sequence;

    }

    protected boolean equalsCyclicSpace( String cyclicSpace ) {

        if ( this.cyclicSpace == null ) {
            return false;
        }

        return this.cyclicSpace.equals( cyclicSpace );

    }


}
