package h2o.common.util.security;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.lang.StringUtil;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EncryptUtil {

    private static final String STD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";


    private EncryptUtil() {}


    public static String encrypt( String data, String key) {

        String chars = shuffle( STD_CHARS );

        String str1 = new Base64Util().encode(  XXTEA.encrypt( chars.getBytes(StandardCharsets.UTF_8) , key  ));

        String str2 = new Base64Util(chars).encode(  XXTEA.encrypt( data.getBytes(StandardCharsets.UTF_8) , key  ));

        return StringUtil.build( str1 , "-" , str2 );

    }

    public static String decrypt( String data, String key) {

        String chars = new String( XXTEA.decrypt(
                new Base64Util().decode( StringUtils.substringBefore( data , "-" )) , key) ,
                StandardCharsets.UTF_8);


        return new String( XXTEA.decrypt(
                new Base64Util(chars).decode( StringUtils.substringAfter( data , "-" ) ) , key) ,
                StandardCharsets.UTF_8);

    }




    private static String shuffle( String str ) {

        int len = str.length();

        List<Character> chars = new ArrayList<>(len);
        for ( char c : str.toCharArray() ) {
            chars.add( c );
        }
        Collections.shuffle(chars);
        char[] charArray = new char[len];
        for ( int i = 0 ; i < len ; i++ ) {
            charArray[i] = chars.get(i);
        }

        return new String( charArray );

    }



}
