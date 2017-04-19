package com.ktt.toolkit.watermark;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * @author luke_kao
 */
public class MarkDrawer extends Drawer {
    private static final int LAYER_ALPHA = 0x50;
    private static final float DEFAULT_TEXT_SIZE = 144f;
    private static final float MIN_TEXT_SIZE = 1f;

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private int mInnerWidth;
    private int mInnerHeight;

    private String[] mLines;
    private Rect mMeasureBounds = new Rect();
    private Paint mTextPaint;

    public MarkDrawer(String text) {
        mLines = text.split("\n");
    }

    @Override
    public void onSave(Canvas canvas) {
        RectF bounds = new RectF(canvas.getClipBounds());
        canvas.saveLayerAlpha(bounds, LAYER_ALPHA, Canvas.ALL_SAVE_FLAG);
    }

    @Override
    public void onDraw(Canvas canvas) {
        init(canvas);

        float angle = getAngle();
        canvas.rotate(angle, mCenterX, mCenterY);

        Paint textPaint = getPaintWithFitTextSize();
        Paint shadowPaint = getShadowTextPaint(textPaint);

        float multiLineHeight = getMultiLineHeight(textPaint);

        int y = (int) (mHeight - multiLineHeight) / 2;
        for (String line : mLines) {
            float measureLength = textPaint.measureText(line);
            float x = (mWidth - measureLength) / 2;

            canvas.drawText(line, x + 1, y + 1, shadowPaint);
            canvas.drawText(line, x, y, textPaint);

            y += textPaint.descent() - textPaint.ascent();
        }

    }

    private void init(Canvas canvas) {
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        mInnerWidth = mWidth / 2;
        mInnerHeight = mHeight / 2;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    private float getAngle() {
        double radians = Math.atan2(mHeight, mWidth);
        return (float) (radians * (180 / Math.PI));
    }

    @NonNull
    private Paint getPaintWithFitTextSize() {
        findInnerRectangle();

        Paint textPaint = getTextPaint();

        for (String line : mLines) {
            adjustTextSize(textPaint, line);
        }

        return textPaint;
    }

    private void findInnerRectangle() {
        int w0 = Math.min(mWidth, mHeight);
        int h0 = Math.max(mWidth, mHeight);

        double radians = Math.atan2(mHeight, mWidth);
        double r0 = radians - Math.floor((radians + Math.PI) / (2 + Math.PI)) * 2 * Math.PI;
        r0 = Math.abs(r0);
        if (r0 > Math.PI / 2) {
            r0 = Math.PI - r0;
        }

        double rate = w0 / (h0 * Math.sin(r0) + w0 * Math.cos(r0));

        int w1 = (int) (mWidth <= mHeight ? w0 * rate : h0 * rate);
        int h1 = (int) (mWidth <= mHeight ? h0 * rate : w0 * rate);

        mInnerWidth = Math.max(w1, h1);
        mInnerHeight = Math.min(w1, h1);
    }

    private void adjustTextSize(Paint textPaint, String line) {
        textPaint.getTextBounds(line, 0, line.length(), mMeasureBounds);

        if (mMeasureBounds.width() > mInnerWidth && textPaint.getTextSize() > MIN_TEXT_SIZE) {
            float size = textPaint.getTextSize() * mInnerWidth / mMeasureBounds.width();
            textPaint.setTextSize(size);

            textPaint.getTextBounds(line, 0, line.length(), mMeasureBounds);
        }

        if (mMeasureBounds.height() > mInnerHeight && textPaint.getTextSize() > MIN_TEXT_SIZE) {
            float size = textPaint.getTextSize() * mInnerHeight / mMeasureBounds.height();
            textPaint.setTextSize(size);

            textPaint.getTextBounds(line, 0, line.length(), mMeasureBounds);
        }
    }

    @NonNull
    private Paint getTextPaint() {
        if (mTextPaint == null) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(DEFAULT_TEXT_SIZE);
            textPaint.setFilterBitmap(true);
            textPaint.setAntiAlias(true);

            mTextPaint = textPaint;
        }

        return mTextPaint;
    }

    @NonNull
    private Paint getShadowTextPaint(Paint paint) {
        Paint shadowPaint = new Paint(paint);
        shadowPaint.setColor(Color.BLACK);

        return shadowPaint;
    }

    private float getMultiLineHeight(Paint textPaint) {
        float multiLineHeight = -textPaint.getTextSize();
        for (String line : mLines) {
            textPaint.getTextBounds(line, 0, line.length(), mMeasureBounds);
            multiLineHeight += mMeasureBounds.height();
        }
        return multiLineHeight;
    }
}
