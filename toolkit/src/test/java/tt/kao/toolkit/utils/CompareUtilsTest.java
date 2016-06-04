package tt.kao.toolkit.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import tt.kao.toolkit.BuildConfig;

/**
 * @author luke_kao
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CompareUtilsTest {

    @Test
    public void equals_ForString() {
        Assert.assertTrue(CompareUtils.equals("abc", "abc"));
        Assert.assertFalse(CompareUtils.equals("abc", "ABC"));
        Assert.assertFalse(CompareUtils.equals("abc", ""));
        Assert.assertFalse(CompareUtils.equals("", "ABC"));
        Assert.assertTrue(CompareUtils.equals("", ""));
        Assert.assertFalse(CompareUtils.equals("abc", null));
        Assert.assertFalse(CompareUtils.equals(null, "ABC"));
        Assert.assertTrue(CompareUtils.equals(null, null));
    }

    @Test
    public void equals_ForObject() {
        MockObject mo1 = new MockObject();
        MockObject mo2 = new MockObject();
        MockObject mo3 = mo1;
        MockObject mo4 = null;

        Assert.assertTrue(CompareUtils.equals(mo1, mo1));
        Assert.assertTrue(CompareUtils.equals(mo2, mo2));
        Assert.assertTrue(CompareUtils.equals(mo1, mo3));

        Assert.assertFalse(CompareUtils.equals(mo1, mo2));
        Assert.assertFalse(CompareUtils.equals(mo2, mo1));
        Assert.assertFalse(CompareUtils.equals(mo3, mo2));
        Assert.assertFalse(CompareUtils.equals(mo2, mo3));

        Assert.assertFalse(CompareUtils.equals(mo1, null));
        Assert.assertFalse(CompareUtils.equals(null, mo1));
        Assert.assertTrue(CompareUtils.equals(null, null));
        Assert.assertFalse(CompareUtils.equals(mo1, mo4));
        Assert.assertFalse(CompareUtils.equals(mo4, mo1));
        Assert.assertTrue(CompareUtils.equals(mo4, mo4));
    }

    private static class MockObject {
    }
}
