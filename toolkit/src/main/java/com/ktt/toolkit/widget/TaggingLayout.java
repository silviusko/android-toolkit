package com.ktt.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.ktt.toolkit.R;

/**
 * @author luke_kao
 */
public class TaggingLayout extends ViewGroup {
    private static final int NONE_VALUE = -1;

    private int mChildHorizontalSpacing;
    private int mChildVerticalSpacing;
    private int mGravity;
    private int mMaxLine;

    private SparseIntArray mRemainingSpaces = new SparseIntArray();
    private int mRemainingVerticalSpace = 0;

    public TaggingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TaggingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TaggingLayout);
        try {
            mChildHorizontalSpacing = a.getDimensionPixelSize(R.styleable.TaggingLayout_childHorizontalSpacing, 0);
            mChildVerticalSpacing = a.getDimensionPixelSize(R.styleable.TaggingLayout_childVerticalSpacing, 0);
            mGravity = a.getInt(R.styleable.TaggingLayout_gravity, Gravity.CENTER_HORIZONTAL);
            mMaxLine = a.getInt(R.styleable.TaggingLayout_maxLine, NONE_VALUE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        boolean willHeightGrow = widthMode != MeasureSpec.UNSPECIFIED;
        boolean isHeightSpecified = heightMode != MeasureSpec.AT_MOST;

        int width = 0;
        int height = 0;

        int lineIndex = 0;
        int currentPos = 0;
        int nextXPos = 0;
        int nextYPos = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int currentHorizontalSpacing = currentPos > 0 ? mChildHorizontalSpacing : 0;

            // move cursor with new horizontal spacing for new child
            boolean willNewChildFit = nextXPos + currentHorizontalSpacing + child.getMeasuredWidth() <= widthSize;
            if (willNewChildFit) {
                nextXPos += currentHorizontalSpacing;
            }

            // check if children width has exceed boundary
            if (willHeightGrow && !willNewChildFit) {
                // step a. record remaining horizontal space for current line
                mRemainingSpaces.put(lineIndex, widthSize - nextXPos);

                // step b. move cursor to next line
                nextYPos = height;

                // step c. check if maxLine has exceed
                lineIndex++;
                if (lineIndex >= mMaxLine && mMaxLine > 0) break;

                // step d. move cursor with new vertical spacing for new line
                nextYPos += mChildVerticalSpacing;

                // step e. reset the x-pos information
                currentPos = 0;
                nextXPos = 0;
            }


            // set current child information
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.x = nextXPos;
            lp.y = nextYPos;
            lp.line = lineIndex;
            lp.pos = currentPos;

            // move cursor to next child
            currentPos++;
            nextXPos += child.getMeasuredWidth();

            // recalculate the size for TaggingLayout
            width = Math.max(width, nextXPos);
            height = Math.max(height, nextYPos + child.getMeasuredHeight());
        }

        // cache remaining size
        mRemainingSpaces.put(lineIndex, widthSize - nextXPos);
        if (isHeightSpecified && nextYPos < heightSize) {
            mRemainingVerticalSpace = heightSize - nextYPos;
        }

        // final size with padding size
        width += getPaddingRight() + getPaddingLeft();
        height += getPaddingBottom() + getPaddingTop();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            TaggingLayout.LayoutParams lp = (TaggingLayout.LayoutParams) child.getLayoutParams();

            if (lp.line == NONE_VALUE) break;

            int remainingHorizontalSpace = mRemainingSpaces.get(lp.line);
            int leftRemainingSpace = getLeftRemainingSpace(remainingHorizontalSpace);
            int topRemainingSpace = getTopRemainingSpace(mRemainingVerticalSpace);

            final int x = lp.x + getPaddingLeft() + leftRemainingSpace;
            final int y = lp.y + getPaddingTop() + topRemainingSpace;

            child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
        }
    }

    private int getLeftRemainingSpace(int remainingSpace) {
        switch (mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                return 0;

            case Gravity.CENTER_HORIZONTAL:
                return remainingSpace / 2;

            case Gravity.RIGHT:
                return remainingSpace;
        }

        return 0;
    }

    private int getTopRemainingSpace(int remainingSpace) {
        switch (mGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                return 0;

            case Gravity.CENTER_VERTICAL:
                return remainingSpace / 2;

            case Gravity.BOTTOM:
                return remainingSpace;
        }

        return 0;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }


    public static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;
        int line = NONE_VALUE;
        int pos;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}
