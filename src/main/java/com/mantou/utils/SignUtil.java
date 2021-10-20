package com.mantou.utils;

import sun.misc.BASE64Decoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtil {
    /**
     * 加签方法
     * @param plain
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static byte[] sign(String plain,String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        //根据对应算法，获取签名对象实例
        Signature signature = Signature.getInstance("SHA256WithRSA");
        //获取私钥，加签用的是私钥，私钥一般是在配置文件里面读的，这里为了演示方便，根据私钥字符串生成私钥对象
        PrivateKey privateKey = getPriveteKey(privateKeyStr);
        //初始化签名对象
        signature.initSign(privateKey);
        //把原始报文更新到对象
        signature.update(plain.getBytes("UTF-8"));
        //加签
        return signature.sign();
    }

    /**
     * 验签方法
     * @param plain
     * @param signatureByte
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws SignatureException
     * @throws InvalidKeySpecException
     */
    public static boolean verify(String plain, byte[] signatureByte,String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException, InvalidKeySpecException {
        //获取公钥
        PublicKey publicKey = getPublicKey(publicKeyStr);
        //根据对应算法，获取签名对象实例
        Signature signature = Signature.getInstance("SHA256WithRSA");
        //初始化签名对象
        signature.initVerify(publicKey);
        //把原始报文更新到签名对象
        signature.update(plain.getBytes("UTF-8"));
        //进行验签
        return signature.verify(signatureByte);
    }

    private static PublicKey getPublicKey(String publicKeyStr) throws InvalidKeySpecException, IOException {
        PublicKey publicKey = null;
        try {
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(publicKeyStr));
            // RSA对称加密算法
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            // 生成公钥对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private static PrivateKey getPriveteKey(String privateKeyStr) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec priPKCS8;
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(privateKeyStr));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
}
