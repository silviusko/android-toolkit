package tt.kao.toolkit.volley;

import android.content.Context;
import android.graphics.Bitmap;

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
    private MockSmallBitmapImageCache mSmallCache;
    private MockDifferentBitmapImageCache mDiffCache;

    @Before
    public void setUp() throws Exception {
        mCache = new MockBitmapImageCache(RuntimeEnvironment.application);
        mSmallCache = new MockSmallBitmapImageCache(RuntimeEnvironment.application);
        mDiffCache = new MockDifferentBitmapImageCache(RuntimeEnvironment.application);
    }

    @After
    public void tearDown() throws Exception {
        mCache.clear();
        mSmallCache.clear();
        mDiffCache.clear();
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
    public void getBitmap_overMemoryCapability() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mDiffCache.putBitmap("key 1", bitmap);
        mDiffCache.putBitmap("key 2", bitmap);
        mDiffCache.putBitmap("key 3", bitmap);

        // load from memory cache
        assertNotNull(mDiffCache.getBitmap("key 2"));
        assertNotNull(mDiffCache.getBitmap("key 3"));

        // load from disk cache, and put it into memory cache
        assertNotNull(mDiffCache.getBitmap("key 1"));
    }

    @Test
    public void getBitmap_overDiskCapability() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mDiffCache.putBitmap("key 1", bitmap);
        mDiffCache.putBitmap("key 2", bitmap);
        mDiffCache.putBitmap("key 3", bitmap);
        mDiffCache.putBitmap("key 4", bitmap);

        // load from memory cache
        assertNotNull(mDiffCache.getBitmap("key 3"));
        assertNotNull(mDiffCache.getBitmap("key 4"));

        // load from disk cache, and put it into memory cache
        assertNotNull(mDiffCache.getBitmap("key 2"));

        assertNull(mDiffCache.getBitmap("key 1"));
    }

    @Test
    public void putBitmap_nullValue() {
        mCache.putBitmap(null, null);

        assertEquals(0, mCache.getMemoryCacheSize());

        assertNull(mCache.getEntryFromDisk("key 1"));
    }

    @Test
    public void putBitmap_normalValue() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mCache.putBitmap("key 1", bitmap);

        assertEquals(100, mCache.getMemoryCacheSize());

        assertNotNull(mCache.getEntryFromDisk("key 1"));
    }

    @Test
    public void putBitmap_duplicatedValue() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mCache.putBitmap("key 1", bitmap);
        mCache.putBitmap("key 1", bitmap);

        assertEquals(100, mCache.getMemoryCacheSize());

        assertNotNull(mCache.getEntryFromMem("key 1"));
        assertNotNull(mCache.getEntryFromDisk("key 1"));
    }

    @Test
    public void putBitmap_smallCache() {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
        mSmallCache.putBitmap("key 1", bitmap);
        mSmallCache.putBitmap("key 2", bitmap);

        assertEquals(100, mSmallCache.getMemoryCacheSize());

        assertNull(mSmallCache.getEntryFromMem("key 1"));
        assertNotNull(mSmallCache.getEntryFromMem("key 2"));

        assertNull(mSmallCache.getEntryFromDisk("key 1"));
        assertNotNull(mSmallCache.getEntryFromDisk("key 2"));
    }

    @Test
    public void putBitmap_diffCache() {
        // The size is 100 in memory cache and the prune condition is the size equals and smalls in memory cache
        // The size is 50 in disk cache and the prune condition is the size smalls in disk cache
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);

        mDiffCache.putBitmap("key 1", bitmap);
        assertNotNull(mDiffCache.getEntryFromMem("key 1"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 1"));

        mDiffCache.putBitmap("key 2", bitmap);
        assertNotNull(mDiffCache.getEntryFromMem("key 1"));
        assertNotNull(mDiffCache.getEntryFromMem("key 2"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 1"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 2"));

        mDiffCache.putBitmap("key 3", bitmap);
        assertNull(mDiffCache.getEntryFromMem("key 1"));
        assertNotNull(mDiffCache.getEntryFromMem("key 2"));
        assertNotNull(mDiffCache.getEntryFromMem("key 3"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 1"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 2"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 3"));

        mDiffCache.putBitmap("key 4", bitmap);
        assertNull(mDiffCache.getEntryFromMem("key 1"));
        assertNull(mDiffCache.getEntryFromMem("key 2"));
        assertNotNull(mDiffCache.getEntryFromMem("key 3"));
        assertNotNull(mDiffCache.getEntryFromMem("key 4"));
        assertNull(mDiffCache.getEntryFromDisk("key 1"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 2"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 3"));
        assertNotNull(mDiffCache.getEntryFromDisk("key 4"));
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

        public int getMemoryCacheSize() {
            return mMemCache.size();
        }

        public Bitmap getEntryFromMem(String key) {
            return mMemCache.get(key);
        }

        public Cache.Entry getEntryFromDisk(String key) {
            return mDiskLruCache.get(key);
        }
    }

    private static class MockSmallBitmapImageCache extends BitmapImageCache {

        public MockSmallBitmapImageCache(Context context) {
            super(context, "small", 100, 100);
        }

        public int getMemoryCacheSize() {
            return mMemCache.size();
        }

        public Bitmap getEntryFromMem(String key) {
            return mMemCache.get(key);
        }

        public Cache.Entry getEntryFromDisk(String key) {
            return mDiskLruCache.get(key);
        }
    }

    private static class MockDifferentBitmapImageCache extends BitmapImageCache {

        public MockDifferentBitmapImageCache(Context context) {
            super(context, "different", 200, 200);
        }

        public int getMemoryCacheSize() {
            return mMemCache.size();
        }

        public Bitmap getEntryFromMem(String key) {
            return mMemCache.get(key);
        }

        public Cache.Entry getEntryFromDisk(String key) {
            return mDiskLruCache.get(key);
        }
    }
}