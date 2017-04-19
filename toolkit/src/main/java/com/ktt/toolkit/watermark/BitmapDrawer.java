package com.ktt.toolkit.watermark;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author luke_kao
 */
public class BitmapDrawer extends Drawer {
    private Bitmap mBitmap;

    public BitmapDrawer(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }
}
