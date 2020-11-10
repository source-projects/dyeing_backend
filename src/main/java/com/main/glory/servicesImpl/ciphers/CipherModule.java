package com.main.glory.servicesImpl.ciphers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import javax.crypto.
import java.security.*;

@Service
public class CipherModule {
	//	private static final String key = "aesEncryptionKey";
	private static final String key = "Aehc#s8723ba[hf7";
	private static final String initVector = "encryptionIntVec";

	public static String encrypt(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

//            Cipher.getInstance()
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			System.out.println(encrypted);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String encryptRSA(String plainText, Key sKey){
		try{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, sKey);
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch(Exception e){
			e.printStackTrace(System.out);
		}
		return null;
	}

	public static String decryptRSA(String encryptedText, Key sKey){
		try{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			return new String(cipher.doFinal(Base64.decodeBase64(encryptedText)));
		} catch(Exception e){
			e.printStackTrace(System.out);
		}
		return null;
	}
}

