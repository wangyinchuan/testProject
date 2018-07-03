package com.example.xapiservice.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class MD5 {

    private static final Logger log= LoggerFactory.getLogger(MD5.class);

    private MD5() {}

    public static String md5(String str){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("utf-8"));
            byte[] bytes = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte b : bytes) {
                i = b;
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch(Exception e){
            log.error("md5加密出错", e);
            return "false";
        }
    }

}