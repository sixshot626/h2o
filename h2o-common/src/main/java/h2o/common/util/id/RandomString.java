package h2o.common.util.id;

import java.util.Random;

public class RandomString {

    private final RandInt rand;

    public RandomString() {
        this.rand = new DefaultRandIntImpl(new Random());
    }

    public RandomString( long seed ) {
        this.rand = new DefaultRandIntImpl( new Random( seed ) );
    }

    public RandomString( RandInt rand ) {
        this.rand = rand;
    }

    private final static char[] c = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    private final static char[] d = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    public String makeCode(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int r = rand.nextInt(c.length);
            sb.append(c[r]);
        }

        return sb.toString();
    }


    public String makeNumberCode(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int r = rand.nextInt(d.length);
            sb.append(d[r]);
        }

        return sb.toString();
    }




    public interface RandInt {
        int nextInt(int n);
    }

    private static class DefaultRandIntImpl implements RandInt {

        private final Random random;

        public DefaultRandIntImpl(Random random) {
            this.random = random;
        }

        @Override
        public int nextInt(int n) {
            return random.nextInt();
        }

    }



}
