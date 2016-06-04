package tt.kao.toolkit.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import tt.kao.toolkit.BuildConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author luke_kao
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BitmapImageCacheTest {
    private MockBitmapImageCache mCache;

    @Before
    public void setUp() throws Exception {
        mCache = new MockBitmapImageCache(RuntimeEnvironment.application);
    }

    @After
    public void tearDown() throws Exception {
        mCache.clear();
    }

    @Test(expected = NullPointerException.class)
    public void getBitmap_nullValue() {
        mCache.getBitmap(null);
    }

    @Test
    public void getBitmap_normalValue() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mCache.putBitmap("key 1", bitmap);

        assertNotNull(mCache.getBitmap("key 1"));
    }

    @Test
    public void putBitmap_nullValue() {
        mCache.putBitmap(null, null);

        assertEquals(0, mCache.getCacheSize());
    }

    @Test
    public void putBitmap_normalValue() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mCache.putBitmap("key 1", bitmap);

        assertNotNull(mCache.getBitmap("key 1"));
    }

    @Test
    public void putBitmap_duplicatedValue() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mCache.putBitmap("key 1", bitmap);
        mCache.putBitmap("key 1", bitmap);

        assertNotNull(mCache.getEntryFromMem("key 1"));
    }

    @Test
    public void putBitmap_overCache() {
        int cacheSize = mCache.getMaxCacheSize();

        String firstKey = "";
        String key = "";
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        for (int i = 0, size = 0; size < cacheSize; i++) {
            key = "key " + i;
            if (TextUtils.isEmpty(firstKey)) {
                firstKey = key;
            }
            mCache.putBitmap(key, bitmap);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                size += bitmap.getAllocationByteCount();
            } else {
                size += bitmap.getByteCount();
            }
        }

        assertNull(mCache.getEntryFromMem(firstKey));
        assertNotNull(mCache.getEntryFromMem(key));
    }

    @Test
    public void clear_normal() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mCache.putBitmap("key 1", bitmap);

        mCache.clear();

        assertNull(mCache.getBitmap("key 1"));
    }

    private static class MockBitmapImageCache extends BitmapImageCache {

        public MockBitmapImageCache(Context context) {
            super(context);
        }

        public int getMaxCacheSize() {
            return bitmapLruCache.maxSize();
        }

        public int getCacheSize() {
            return bitmapLruCache.size();
        }

        public Bitmap getEntryFromMem(String key) {
            return bitmapLruCache.get(key);
        }
    }
}