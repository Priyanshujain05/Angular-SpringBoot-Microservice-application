package com.encrypted.secured.cipher;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

	private static final String SECRET_KEY = "1234567890123456";
	private static final String IV = "abcdefghijklmnop";

	public static String decrypt(String encrypted) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES"),
					new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8)));

			byte[] decoded = Base64.getDecoder().decode(encrypted);
			byte[] original = cipher.doFinal(decoded);
			return new String(original, StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
		}
	}

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception ex) {
            throw new RuntimeException("Error encrypting: " + ex.getMessage(), ex);
        }
    }
}
