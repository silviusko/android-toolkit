package com.ktt.toolkit.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * reference from https://nelenkov.blogspot.tw/2012/04/using-password-based-encryption-on.html
 *
 * @author luke_kao
 *
 */
public class HashUtils {
    private static final String DEFAULT_PASSWORD = "GBAQUFXV4uJJxxkyoIkD";
    private static final String SEPARATOR = "]";
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_LENGTH = 128;
    private static final int SALT_LENGTH = KEY_LENGTH / 8;

    public static String encrypt(String content) {
        return encrypt(content, DEFAULT_PASSWORD);
    }

    public static String encrypt(String content, String password) {
        if (TextUtils.isEmpty(content)) return "";

        try {
            byte[] byteContent = content.getBytes("utf-8");

            byte[] salt = new byte[SALT_LENGTH];

            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            SecretKeySpec key = deriveKeyPbkdf2(password, salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];
            random.nextBytes(iv);

            IvParameterSpec ivParams = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

            byte[] cryptContent = cipher.doFinal(byteContent);

            return toBase64(salt) + SEPARATOR + toBase64(iv) + SEPARATOR + toBase64(cryptContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String content) {
        return decrypt(content, DEFAULT_PASSWORD);
    }

    public static String decrypt(String content, String password) {
        if (TextUtils.isEmpty(content)) return "";

        try {
            String[] fields = content.split(SEPARATOR);
            byte[] salt = fromBase64(fields[0]);
            byte[] iv = fromBase64(fields[1]);
            byte[] cipherBytes = fromBase64(fields[2]);

            SecretKey key = deriveKeyPbkdf2(password, salt);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            byte[] plaintext = cipher.doFinal(cipherBytes);

            return new String(plaintext, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    private static SecretKeySpec deriveKeyPbkdf2(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyByte = keyFactory.generateSecret(keySpec).getEncoded();

        return new SecretKeySpec(keyByte, "AES");
    }

    public static String md5(String content) {
        if (TextUtils.isEmpty(content))
            return "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] byteMd5 = md.digest(content.getBytes());

            return toHexString(byteMd5);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP | Base64.NO_PADDING);
    }

    private static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP | Base64.NO_PADDING);
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int intVal = b & 0xff;
            if (intVal < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(intVal));
        }
        return sb.toString();
    }
}
