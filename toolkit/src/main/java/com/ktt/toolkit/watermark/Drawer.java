package com.ktt.toolkit.watermark;

import android.graphics.Canvas;

/**
 * @author luke_kao
 */
public abstract class Drawer {
    public void onSave(Canvas canvas) {
        canvas.save();
    }

    public abstract void onDraw(Canvas canvas);

    public void onRestore(Canvas canvas) {
        canvas.restore();
    }
}
