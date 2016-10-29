package com.qican.ygj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import android.util.Log;

public class CryptoUtils {
	
	private static final String TAG = "CryptoUtils";
	
	public final static String MASTER_PASSWORD = "masterP@ssword";
	private final static String HEX = "0123456789ABCDEF"; 
	
	public static String encrypt(String cleartext) {
		try {
			return encrypt(MASTER_PASSWORD, cleartext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return cleartext;
	}
	
	public static String decrypt(String encrypted) {
		try {
			return decrypt(MASTER_PASSWORD, encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return encrypted;
	}
	
    public static String encrypt(String seed, String cleartext) throws Exception {    
        byte[] rawKey = getRawKey(seed.getBytes());    
        byte[] result = encrypt(rawKey, cleartext.getBytes());    
        return toHex(result);
    }    
        
    public static String decrypt(String seed, String encrypted) throws Exception {    
        byte[] rawKey = getRawKey(seed.getBytes());    
        byte[] enc = toByte(encrypted);    
        byte[] result = decrypt(rawKey, enc);    
        return new String(result);    
    }
    
    private static byte[] getRawKey(byte[] seed) throws Exception {    
        KeyGenerator kgen = KeyGenerator.getInstance("AES");    
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");    
        sr.setSeed(seed);    
        kgen.init(128, sr); // 192 and 256 bits may not be available    
        SecretKey skey = kgen.generateKey();    
        byte[] raw = skey.getEncoded();    
        return raw;    
    }
    
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {    
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");    
        Cipher cipher = Cipher.getInstance("AES");    
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);    
        byte[] encrypted = cipher.doFinal(clear);    
        return encrypted;    
    }
    
    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {    
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");    
        Cipher cipher = Cipher.getInstance("AES");    
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);    
        byte[] decrypted = cipher.doFinal(encrypted);    
        return decrypted;    
    }    
   
    public static String toHex(String txt) {    
        return toHex(txt.getBytes());    
    }
    
    public static String fromHex(String hex) {    
        return new String(toByte(hex));    
    } 
    
    public static byte[] toByte(String hexString) {    
        int len = hexString.length()/2;    
        byte[] result = new byte[len];    
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(
            		hexString.substring(2 * i, 2 * i+2), 16).byteValue();
        }
        return result;    
    }    
   
    public static String toHex(byte[] buf) {
        if (buf == null)    
            return "";    
        StringBuffer result = new StringBuffer(2*buf.length);    
        for (int i = 0; i < buf.length; i++) {    
            appendHex(result, buf[i]);    
        }
        
        return result.toString();  
    }
    
    private static void appendHex(StringBuffer sb, byte b) {    
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));    
    }
    
    public static byte[]  getMd5(String key) {
    	byte [] buffer;
    	byte [] ret = new byte[16];
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			buffer = key.getBytes();
			md5.update(buffer);
		} catch (NoSuchAlgorithmException e) {
			Log.e(
					TAG,
					String.format("readFile Error!\te.getMessage:%s",
							e.getMessage()));
			return ret;
		}
		
		ret = md5.digest();
		return ret;
    }
    
    public static String CryptoRc4(String data) {
    	return PackageUtils.jni_CryptoRc4(data, "iflytek_pass_edp");
    }
}
