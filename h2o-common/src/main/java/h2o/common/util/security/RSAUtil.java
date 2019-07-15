package h2o.common.util.security;


import h2o.common.exception.ExceptionUtil;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";


    private static byte[] decryptBASE64(String key) {
        return new Base64Util().decode(key);
    }


    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) {

        try {
            // 对密钥解密
            byte[] keyBytes = decryptBASE64(key);
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);

        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }

    }


    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) {

        try {
            // 对公钥解密
            byte[] keyBytes = decryptBASE64(key);
            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);

        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }

    }


    public static KeyPair makeKey() {

        try {

            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // String pubkey =  new Base64Util().encode(  keyPair.getPublic().getEncoded() );// 公钥
            // String prikey = new Base64Util().encode( keyPair.getPrivate().getEncoded() );// 私钥


            return keyPair;

        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }


    }


}