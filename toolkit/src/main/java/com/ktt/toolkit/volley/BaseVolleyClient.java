package com.ktt.toolkit.volley;

import android.content.Context;
import android.support.annotation.NonNull;

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
        BitmapImageCache imageCache = createBitmapImageCache(context);

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

    @NonNull
    protected BitmapImageCache createBitmapImageCache(Context context) {
        return new BitmapImageCache(context);
    }
}
