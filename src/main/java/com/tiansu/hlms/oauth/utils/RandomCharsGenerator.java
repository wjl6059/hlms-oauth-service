package com.tiansu.hlms.oauth.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author shufq
 * @description
 * @date 2018/2/2 9:33
 */
public class RandomCharsGenerator {
    private static SecureRandom random = new SecureRandom();

    /**
     * 生成随机字符
     * @param size
     * @return
     */
    public static String uuid(int size) {
        final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
                'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
                'Z'};

        char[] cs = new char[size];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = digits[random.nextInt(digits.length)];
        }
        return new String(cs);
    }


    public static long idGen(){
        return Long.parseLong(randomTimeStampNumer(3));
    }
    /* 没有被调用
    public static long randomNumber(){
        long id = random.nextLong();
        if(id < 0){
            return Math.abs(id);
        }else{
            return id;
        }
    }
    */

    /**
     * 时间戳+n位随机数（适用于并发量不大的情况下，保证随机值唯一）
     * * @return
     */
    public static String randomTimeStampNumer(int size){
        Random random = new Random();
        int end = random.nextInt((int)(Math.pow(10,size))-1);
        //不足位则补0
        String str = System.currentTimeMillis() + String.format("%0"+size+"d", end);
        return str;
    }




    public static void main(String[] args){
        for(int i = 0;i<100;i++){
            System.out.println(idGen());
        }

    }
}
