package tt.kao.toolkit.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by justin_chen on 2016/2/4.
 */
public class BaseVolleyClient {
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    protected BaseVolleyClient(Context context) {
        BitmapImageCache imageCache = new BitmapImageCache(context);

        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, imageCache);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        mRequestQueue.add(req);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
