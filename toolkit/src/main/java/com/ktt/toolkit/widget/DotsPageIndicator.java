package com.ktt.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.ktt.toolkit.R;


/**
 * @author luke_kao
 */
public class DotsPageIndicator extends View implements ViewPager.OnPageChangeListener {
    public final static int HORIZONTAL = 0x00000000;
    public final static int VERTICAL = 0x00000001;

    private final Paint mPaintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintDotSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintStrokeSelected = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ViewPager mViewPager;

    private int mOrientation;
    private float mRadius;
    private float mStrokeWidth;
    private float mSpacing;
    private boolean mSnap;
    private boolean mShowSingleDot;

    private int mCurrentPosition;
    private int mSnapPosition;
    private float mPositionOffset;
    private int mScrollState;

    public DotsPageIndicator(Context context) {
        this(context, null);
    }

    public DotsPageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.DotsPageIndicatorStyle);
    }

    public DotsPageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotsPageIndicator, defStyleAttr, R.style.DotsPageIndicatorStyle);

        mOrientation = a.getInt(R.styleable.DotsPageIndicator_android_orientation, HORIZONTAL);
        mRadius = a.getDimension(R.styleable.DotsPageIndicator_android_radius, 0);
        mStrokeWidth = a.getDimension(R.styleable.DotsPageIndicator_strokeWidth, 0);
        mSpacing = a.getDimension(R.styleable.DotsPageIndicator_spacing, 0);
        mSnap = a.getBoolean(R.styleable.DotsPageIndicator_snap, true);
        mShowSingleDot = a.getBoolean(R.styleable.DotsPageIndicator_showSingleDot, true);

        mPaintDot.setColor(a.getColor(R.styleable.DotsPageIndicator_dotColor, ContextCompat.getColor(context, android.R.color.white)));
        mPaintDotSelected.setColor(a.getColor(R.styleable.DotsPageIndicator_dotColorSelected, ContextCompat.getColor(context, android.R.color.black)));
        mPaintStroke.setColor(a.getColor(R.styleable.DotsPageIndicator_strokeColor, ContextCompat.getColor(context, android.R.color.black)));
        mPaintStroke.setStrokeWidth(mStrokeWidth);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStrokeSelected.setColor(a.getColor(R.styleable.DotsPageIndicator_strokeColorSelected, ContextCompat.getColor(context, android.R.color.black)));
        mPaintStrokeSelected.setStrokeWidth(mStrokeWidth);
        mPaintStrokeSelected.setStyle(Paint.Style.STROKE);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == VERTICAL) {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        }
    }

    private int measureLong(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY || mViewPager == null) {
            return specSize;
        }

        final int count = mViewPager.getAdapter().getCount();
        final int spacingCount = count > 0 ? count - 1 : 0;
        final float circleSize = 2 * mRadius + mStrokeWidth;

        int result = (int) Math.ceil(circleSize * count + mSpacing * spacingCount);

        // with padding
        result += (mOrientation == HORIZONTAL) ?
            getPaddingLeft() + getPaddingRight() :
            getPaddingTop() + getPaddingBottom();

        if (specMode == MeasureSpec.AT_MOST) {
            return Math.min(result, specSize);
        } else {
            return result;
        }
    }

    private int measureShort(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        }

        int result = (int) Math.ceil(2 * mRadius + mStrokeWidth);

        // with padding
        result += (mOrientation == HORIZONTAL) ?
            getPaddingTop() + getPaddingBottom() :
            getPaddingLeft() + getPaddingRight();

        if (specMode == MeasureSpec.AT_MOST) {
            return Math.min(result, specSize);
        } else {
            return result;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) return;

        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) return;
        if (!mShowSingleDot && count == 1) return;

        int longSize;
        int longPaddingStart;
        int longPaddingEnd;
        int shortPaddingStart;
        if (mOrientation == HORIZONTAL) {
            longSize = getWidth();
            longPaddingStart = getPaddingLeft();
            longPaddingEnd = getPaddingRight();
            shortPaddingStart = getPaddingTop();
        } else {
            longSize = getHeight();
            longPaddingStart = getPaddingTop();
            longPaddingEnd = getPaddingBottom();
            shortPaddingStart = getPaddingLeft();
        }

        final float remainingLongSize = longSize -
            longPaddingStart - longPaddingEnd - // padding
            count * (2 * mRadius + mStrokeWidth) - // dots size
            (count - 1) * mSpacing; // spacings size

        final float shortOffset = shortPaddingStart + mRadius + mStrokeWidth / 2;
        final float longOffset = longPaddingStart + mRadius + mStrokeWidth / 2 + remainingLongSize / 2;

        drawDots(canvas, count, shortOffset, longOffset);
        drawDotSelected(canvas, shortOffset, longOffset);
    }

    private void drawDots(Canvas canvas, int count, float shortOffset, float longOffset) {
        float circleX;
        float circleY;

        for (int i = 0; i < count; i++) {
            float currentRadiusOffset = i * 2 * mRadius;
            float currentStrokeOffset = i * mStrokeWidth;
            float currentAllSpacing = i * mSpacing;
            if (mOrientation == HORIZONTAL) {
                circleX = longOffset + currentRadiusOffset + currentStrokeOffset + currentAllSpacing;
                circleY = shortOffset;
            } else {
                circleX = shortOffset;
                circleY = longOffset + currentRadiusOffset + currentStrokeOffset + currentAllSpacing;
            }

            canvas.drawCircle(circleX, circleY, mRadius, mPaintDot);

            if (mStrokeWidth > 0) {
                canvas.drawCircle(circleX, circleY, mRadius, mPaintStroke);
            }
        }
    }

    private void drawDotSelected(Canvas canvas, float shortOffset, float longOffset) {
        float circleX;
        float circleY;

        final float startPosition = mSnap ? mSnapPosition : mCurrentPosition;
        final float radiusOffset = startPosition * 2 * mRadius;
        final float strokeOffset = startPosition * mStrokeWidth;
        final float spacingOffset = startPosition * mSpacing;
        final float dotGap = 2 * mRadius + mStrokeWidth + mSpacing;

        float dragOffset = 0;
        if (!mSnap) {
            // mPositionOffset is always between 0 ~ 1.0
            float offsetWithDirection = mCurrentPosition >= mSnapPosition ?
                mPositionOffset : mPositionOffset - 1.0f;
            dragOffset = offsetWithDirection * dotGap;
        }

        if (mOrientation == HORIZONTAL) {
            circleX = longOffset + radiusOffset + strokeOffset + spacingOffset + dragOffset;
            circleY = shortOffset;
        } else {
            circleX = shortOffset;
            circleY = longOffset + radiusOffset + strokeOffset + spacingOffset + dragOffset;
        }

        canvas.drawCircle(circleX, circleY, mRadius, mPaintDotSelected);

        if (mStrokeWidth > 0) {
            canvas.drawCircle(circleX, circleY, mRadius, mPaintStrokeSelected);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mCurrentPosition = savedState.currentPosition;
        mSnapPosition = savedState.currentPosition;

        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = mCurrentPosition;

        return savedState;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPosition = position;
        mPositionOffset = positionOffset;
        invalidate();
    }


    @Override
    public void onPageSelected(int position) {
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPosition = position;
            mSnapPosition = position;
            invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
    }

    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) return;

        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }

        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        invalidate();
    }

    private static class SavedState extends BaseSavedState {
        int currentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
