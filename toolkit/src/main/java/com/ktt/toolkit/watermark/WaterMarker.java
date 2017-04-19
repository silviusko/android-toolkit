package com.ktt.toolkit.watermark;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author luke_kao
 */
public class WaterMarker {
    private Canvas mCanvas;
    private Bitmap mResult;
    private ArrayList<Drawer> mDrawers = new ArrayList<>();

    public static void markImageFile(File src, File dest, String watermark) {
        Bitmap bitmap = BitmapFactory.decodeFile(src.getAbsolutePath());

        new WaterMarker(bitmap)
            .add(new MarkDrawer(watermark))
            .draw()
            .toFile(dest)
            .release();

        bitmap.recycle();
    }

    public static Bitmap markImage(final Bitmap src, String watermark) {
        return new WaterMarker(src)
            .add(new MarkDrawer(watermark))
            .draw()
            .getResult();
    }

    private WaterMarker(Bitmap src) {
        mResult = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        mCanvas = new Canvas(mResult);

        mDrawers.add(new BitmapDrawer(src));
    }

    public WaterMarker add(Drawer drawer) {
        mDrawers.add(drawer);
        return this;
    }

    public WaterMarker draw() {
        for (Drawer drawer : mDrawers) {
            drawer.onSave(mCanvas);

            drawer.onDraw(mCanvas);

            drawer.onRestore(mCanvas);
        }
        return this;
    }

    public WaterMarker toFile(File file) {
        Bitmap result = getResult();

        if (result != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);

                result.compress(Bitmap.CompressFormat.PNG, 95, fos);

                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    public Bitmap getResult() {
        if (mResult != null && !mResult.isRecycled()) {
            return mResult;
        } else {
            return null;
        }
    }

    public void release() {
        if (mResult != null && !mResult.isRecycled()) {
            mResult.recycle();
            mResult = null;
        }
    }
}
