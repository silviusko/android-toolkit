package tt.kao.toolkit.utils;

import android.text.TextUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import tt.kao.toolkit.BuildConfig;

/**
 * @author luke_kao
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HashUtilsTest {
    public static final String TEST_STRING = "abcd一二三四あか。";
    public static final String PASSWORD_FOR_ENCRYPTION = "PASSWORD";
    public static final String MD5_FOR_TEST_STRING = "b6c028a41dfa5807fd698d27e1a34d39";

    @Test
    public void encrypt_NormalValue() {
        String encrypt;
        encrypt = HashUtils.encrypt(TEST_STRING);
        Assert.assertFalse(TextUtils.isEmpty(encrypt));

        encrypt = HashUtils.encrypt(TEST_STRING, PASSWORD_FOR_ENCRYPTION);
        Assert.assertFalse(TextUtils.isEmpty(encrypt));
    }

    @Test
    public void encrypt_EmptyValue() {
        String encrypt;
        encrypt = HashUtils.encrypt("");
        Assert.assertTrue(TextUtils.isEmpty(encrypt));

        encrypt = HashUtils.encrypt("", PASSWORD_FOR_ENCRYPTION);
        Assert.assertTrue(TextUtils.isEmpty(encrypt));
    }

    @Test
    public void encrypt_NullValue() {
        String encrypt;
        encrypt = HashUtils.encrypt(null);
        Assert.assertTrue(TextUtils.isEmpty(encrypt));

        encrypt = HashUtils.encrypt(null, PASSWORD_FOR_ENCRYPTION);
        Assert.assertTrue(TextUtils.isEmpty(encrypt));
    }


    @Test
    public void decrypt_NormalValue() {
        String encrypt, decrypt;
        encrypt = HashUtils.encrypt(TEST_STRING);
        decrypt = HashUtils.decrypt(encrypt);
        Assert.assertEquals(TEST_STRING, decrypt);

        encrypt = HashUtils.encrypt(TEST_STRING, PASSWORD_FOR_ENCRYPTION);
        decrypt = HashUtils.decrypt(encrypt, PASSWORD_FOR_ENCRYPTION);
        Assert.assertEquals(TEST_STRING, decrypt);
    }

    @Test
    public void decrypt_WrongPassword() {
        String encrypt, decrypt;
        String lowercasePw = PASSWORD_FOR_ENCRYPTION.toLowerCase();

        encrypt = HashUtils.encrypt(TEST_STRING, PASSWORD_FOR_ENCRYPTION);
        decrypt = HashUtils.decrypt(encrypt, lowercasePw);
        Assert.assertNotEquals(TEST_STRING, decrypt);
    }

    @Test
    public void decrypt_EmptyValue() {
        String encrypt, decrypt;
        encrypt = HashUtils.encrypt("");
        decrypt = HashUtils.decrypt(encrypt);
        Assert.assertEquals("", decrypt);

        encrypt = HashUtils.encrypt("", PASSWORD_FOR_ENCRYPTION);
        decrypt = HashUtils.decrypt(encrypt, PASSWORD_FOR_ENCRYPTION);
        Assert.assertEquals("", decrypt);
    }

    @Test
    public void decrypt_NullValue() {
        String decrypt;
        decrypt = HashUtils.decrypt(null);
        Assert.assertEquals("", decrypt);

        decrypt = HashUtils.decrypt(null, PASSWORD_FOR_ENCRYPTION);
        Assert.assertEquals("", decrypt);
    }

    @Test
    public void md5_NormalValue() {
        String md5 = HashUtils.md5(TEST_STRING);
        Assert.assertEquals(MD5_FOR_TEST_STRING, md5);
    }

    @Test
    public void md5_EmptyValue() {
        String md5 = HashUtils.md5("");
        Assert.assertEquals("", md5);
    }

    @Test
    public void md5_NullValue() {
        String md5 = HashUtils.md5(null);
        Assert.assertEquals("", md5);
    }

    @Test
    public void toHexString_NormalValue() {
        byte[] byteContent = {0x11, 0x22, 0x33, 0x44, (byte) 0xff};
        String hexContent = HashUtils.toHexString(byteContent);

        Assert.assertEquals("11223344ff", hexContent);
    }

    @Test
    public void toHexString_EmptyValue() {
        byte[] byteContent = {};
        String hexContent = HashUtils.toHexString(byteContent);

        Assert.assertEquals("", hexContent);
    }

    @Test(expected = NullPointerException.class)
    public void toHexString_NullValue() {
        HashUtils.toHexString(null);
    }
}