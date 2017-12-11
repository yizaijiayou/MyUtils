package com.example.myutils.utils.systemUtils.encrypting;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project name ElectroCar
 * Created by 哎呀呀！要努力打代码啦！··
 * on 2017/5/2 14:57.
 * 本类描述：MD5加密类
 */
public class MD5 {
    public static String getMD5(String msg){
        byte[] bys;
        try {
            bys = MessageDigest.getInstance("MD5").digest(msg.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 fail to get", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5 fail to get", e);
        }

        StringBuilder sb = new StringBuilder(bys.length * 2);
        for (byte b :bys){
            if ((b & 0xFF) < 0x10) sb.append("0");
            sb.append(Integer.toHexString( b & 0xFF));
        }
        return sb.toString().toUpperCase();
    }
}
