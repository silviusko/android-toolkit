package tt.kao.toolkit.volley;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.volley.Cache;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

/**
 * @author luke_kao
 */
public class AsyncPutImageCacheTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Cache> mWeakCache;
    private String mKey;
    private Bitmap mBitmap;

    public AsyncPutImageCacheTask(Cache cache, String key, Bitmap bitmap) {
        mWeakCache = new WeakReference<>(cache);
        mKey = key;
        mBitmap = bitmap;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Cache cache = mWeakCache.get();
        if (cache == null) return null;

        Cache.Entry existed = cache.get(mKey);
        if (existed != null) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
        byte[] byteArray = stream.toByteArray();
        Cache.Entry entry = new Cache.Entry();
        entry.data = byteArray;

        cache.put(mKey, entry);

        return null;
    }
}
