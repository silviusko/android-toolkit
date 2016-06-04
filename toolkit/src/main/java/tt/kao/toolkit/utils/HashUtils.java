package tt.kao.toolkit.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author luke_kao
 */
public class HashUtils {
    private static final String IV_KEY = "68091a4e07156d6aa17b7ac88a92102d";
    private static final String PASSWORD = "GBAQUFXV4uJJxxkyoIkD";
    private static final int KEY_SIZE = 16;

    public static String encrypt(String content) {
        return encrypt(content, PASSWORD);
    }

    public static String encrypt(String content, String password) {
        if (TextUtils.isEmpty(content)) return "";

        try {
            byte[] byteContent = content.getBytes("utf-8");
            byte[] cryptContent = crypto(byteContent, password, Cipher.ENCRYPT_MODE);
            return toBase64(cryptContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String content) {
        return decrypt(content, PASSWORD);
    }

    public static String decrypt(String content, String password) {
        if (TextUtils.isEmpty(content)) return "";

        try {
            byte[] decryptFrom = fromBase64(content);
            byte[] decryptContent = crypto(decryptFrom, password, Cipher.DECRYPT_MODE);
            return new String(decryptContent, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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

    private static synchronized byte[] crypto(byte[] content, String password, int mode)
        throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {

        byte[] key = generateKey(password, KEY_SIZE);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        byte[] ivKey = generateKey(IV_KEY, KEY_SIZE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivKey);


        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, keySpec, ivParameterSpec);

        return cipher.doFinal(content);
    }

    private static byte[] generateKey(String value, int size) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        byte[] sha1 = digest.digest(value.getBytes("UTF-8"));

        return Arrays.copyOf(sha1, size);
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
