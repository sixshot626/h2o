package h2o.common.util.security;


import h2o.common.exception.ExceptionUtil;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {

    private static final String KEY_ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHM = "SHA256withRSA";


    private static byte[] decryptBASE64(String key) {
        return new Base64Util().decode(key);
    }


    /**
     * 解密<br>
     * 用私钥解密
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









    public static byte[] signBySHA256WithRSA(  byte[] content,  String privateKey) {

        try {

            byte[] keyBytes = decryptBASE64(privateKey);

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey priKey = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(priKey);
            signature.update(content);

            return signature.sign();

        } catch (Exception e) {
            //签名失败
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    public boolean verifyBySHA256WithRSA( byte[] content, String publicKey , byte[] sign  ) {

        try {

            byte[] keyBytes = decryptBASE64(publicKey);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey pubKey = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(keySpec);

            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(content);

            return signature.verify(sign);

        } catch (Exception e) {
            //验签失败
            throw ExceptionUtil.toRuntimeException(e);
        }
    }









    public static KeyPair makeKey(int keysize) {

        try {

            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(keysize);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // String pubkey =  new Base64Util().encode(  keyPair.getPublic().getEncoded() );// 公钥
            // String prikey = new Base64Util().encode( keyPair.getPrivate().getEncoded() );// 私钥


            return keyPair;

        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }


    }


}