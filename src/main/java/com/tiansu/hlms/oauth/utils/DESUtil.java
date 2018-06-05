package com.tiansu.hlms.oauth.utils;



import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;


/**
 * @author chenxu
 * @description DES加密工具类
 * @date 2018-05-22 10:41
 **/

public class DESUtil {

    /**
     *
     * @param encryptionStr    待加密的字符串
     * @param password 加密密钥
     * @return 返回16进制的字符串
     *
     * @creator 陈旭
     * @createtime 2018/5/22 10:06
     * @description: 进行解密操作
     */
    public static String encrypt(String encryptionStr, String password) {
        try{
            byte[] data = encryptionStr.getBytes();
            byte[] key = password.getBytes();
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作

            return byte2hex(cipher.doFinal(data));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param decryptionBase64Str    待解密的字符串
     * @param password 加密密钥
     * @return 解密后的字符串
     *
     * @creator 陈旭
     * @createtime 2018/5/22 10:06
     * @description: 进行解密操作
     */

    public static String decrypt(String decryptionBase64Str, String password) {
        try {
            byte[] data = hex2byte(decryptionBase64Str.getBytes());
            byte[] key = password.getBytes();

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return new String (cipher.doFinal(data));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }
}
