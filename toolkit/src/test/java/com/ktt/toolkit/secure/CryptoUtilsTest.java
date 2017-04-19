package com.ktt.toolkit.secure;

import android.text.TextUtils;

import com.ktt.toolkit.BuildConfig;
import com.ktt.toolkit.secure.CryptoUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * @author luke_kao
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CryptoUtilsTest {
    public static final String TEST_STRING = "abcd一二三四あか。";
    public static final String PASSWORD_FOR_ENCRYPTION = "PASSWORD";
    public static final String MD5_FOR_TEST_STRING = "b6c028a41dfa5807fd698d27e1a34d39";

    @Test
    public void encrypt_NormalValue() {
        String encrypt;
        encrypt = CryptoUtils.encrypt(TEST_STRING);
        Assert.assertFalse(TextUtils.isEmpty(encrypt));

        encrypt = CryptoUtils.encrypt(TEST_STRING, PASSWORD_FOR_ENCRYPTION);
        Assert.assertFalse(TextUtils.isEmpty(encrypt));
    }

    @Test
    public void encrypt_EmptyValue() {
        String encrypt;
        encrypt = CryptoUtils.encrypt("");
        Assert.assertTrue(TextUtils.isEmpty(encrypt));

        encrypt = CryptoUtils.encrypt("", PASSWORD_FOR_ENCRYPTION);
        Assert.assertTrue(TextUtils.isEmpty(encrypt));
    }

    @Test
    public void encrypt_NullValue() {
        String encrypt;
        encrypt = CryptoUtils.encrypt(null);
        Assert.assertTrue(TextUtils.isEmpty(encrypt));

        encrypt = CryptoUtils.encrypt(null, PASSWORD_FOR_ENCRYPTION);
        Assert.assertTrue(TextUtils.isEmpty(encrypt));
    }


    @Test
    public void decrypt_NormalValue() {
        String encrypt, decrypt;
        encrypt = CryptoUtils.encrypt(TEST_STRING);
        decrypt = CryptoUtils.decrypt(encrypt);
        Assert.assertEquals(TEST_STRING, decrypt);

        encrypt = CryptoUtils.encrypt(TEST_STRING, PASSWORD_FOR_ENCRYPTION);
        decrypt = CryptoUtils.decrypt(encrypt, PASSWORD_FOR_ENCRYPTION);
        Assert.assertEquals(TEST_STRING, decrypt);
    }

    @Test
    public void decrypt_WrongPassword() {
        String encrypt, decrypt;
        String lowercasePw = PASSWORD_FOR_ENCRYPTION.toLowerCase();

        encrypt = CryptoUtils.encrypt(TEST_STRING, PASSWORD_FOR_ENCRYPTION);
        decrypt = CryptoUtils.decrypt(encrypt, lowercasePw);
        Assert.assertNotEquals(TEST_STRING, decrypt);
    }

    @Test
    public void decrypt_EmptyValue() {
        String encrypt, decrypt;
        encrypt = CryptoUtils.encrypt("");
        decrypt = CryptoUtils.decrypt(encrypt);
        Assert.assertEquals("", decrypt);

        encrypt = CryptoUtils.encrypt("", PASSWORD_FOR_ENCRYPTION);
        decrypt = CryptoUtils.decrypt(encrypt, PASSWORD_FOR_ENCRYPTION);
        Assert.assertEquals("", decrypt);
    }

    @Test
    public void decrypt_NullValue() {
        String decrypt;
        decrypt = CryptoUtils.decrypt(null);
        Assert.assertEquals("", decrypt);

        decrypt = CryptoUtils.decrypt(null, PASSWORD_FOR_ENCRYPTION);
        Assert.assertEquals("", decrypt);
    }

    @Test
    public void md5_NormalValue() {
        String md5 = CryptoUtils.md5(TEST_STRING);
        Assert.assertEquals(MD5_FOR_TEST_STRING, md5);
    }

    @Test
    public void md5_EmptyValue() {
        String md5 = CryptoUtils.md5("");
        Assert.assertEquals("", md5);
    }

    @Test
    public void md5_NullValue() {
        String md5 = CryptoUtils.md5(null);
        Assert.assertEquals("", md5);
    }

    @Test
    public void toHexString_NormalValue() {
        byte[] byteContent = {0x11, 0x22, 0x33, 0x44, (byte) 0xff};
        String hexContent = CryptoUtils.toHexString(byteContent);

        Assert.assertEquals("11223344ff", hexContent);
    }

    @Test
    public void toHexString_EmptyValue() {
        byte[] byteContent = {};
        String hexContent = CryptoUtils.toHexString(byteContent);

        Assert.assertEquals("", hexContent);
    }

    @Test(expected = NullPointerException.class)
    public void toHexString_NullValue() {
        CryptoUtils.toHexString(null);
    }
}