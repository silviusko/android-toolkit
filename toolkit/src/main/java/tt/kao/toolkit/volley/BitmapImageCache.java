package tt.kao.toolkit.volley;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

/**
 * @author luke_kao
 */
public class BitmapImageCache implements ImageLoader.ImageCache {
    protected final LruCache<String, Bitmap> bitmapLruCache;

    public BitmapImageCache(Context context) {
        int memSize = getRecommendMemSizeInBytes(context);

        bitmapLruCache = new LruCache<String, Bitmap>(memSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return value.getAllocationByteCount();
                } else {
                    return value.getByteCount();
                }
            }
        };
    }

    protected int getRecommendMemSizeInBytes(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int availableMemInBytes = am.getMemoryClass() * 1024 * 1024;

        // 8 is recommend value by Google
        return availableMemInBytes / 8;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return bitmapLruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if (url == null || url.length() == 0) return;

        if (bitmap != null) {
            bitmapLruCache.put(url, bitmap);
        }
    }

    public void clear() {
        bitmapLruCache.evictAll();
    }
}
