package com.ez08.compass.ui.market.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ez08.compass.R;
import com.ez08.compass.ui.market.holder.ChartsIndustryHolder;
import com.ez08.compass.ui.market.holder.ChartsPopularHolder;

public class CustomGridItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDividerVer;
    private Drawable mDividerHor;

    private final Rect mBounds = new Rect();

    public CustomGridItemDecoration(Context context) {
        this.mDividerVer = ContextCompat.getDrawable(context, R.drawable.line_light_1px);
        this.mDividerHor = ContextCompat.getDrawable(context, R.drawable.line_light_hor_1px);
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            c.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View view = parent.getChildAt(i);
            if (parent.getChildViewHolder(view) instanceof ChartsIndustryHolder ||
                    parent.getChildViewHolder(view) instanceof ChartsPopularHolder) {
                drawBlock(c, parent, view);
            } else {
                drawHorizontal(c, parent, view, left, right);
            }

        }
    }

    private void drawBlock(Canvas canvas, RecyclerView parent, View child) {
        canvas.save();
        parent.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
        int divideW = this.mDividerHor.getIntrinsicWidth();
        int divideH = this.mDividerVer.getIntrinsicHeight();

        this.mDividerVer.setBounds(this.mBounds.left,
                this.mBounds.bottom - divideH,
                this.mBounds.right,
                this.mBounds.bottom);

        this.mDividerHor.setBounds(this.mBounds.right - divideW,
                this.mBounds.top,
                this.mBounds.right,
                this.mBounds.bottom);

        this.mDividerVer.draw(canvas);
        this.mDividerHor.draw(canvas);
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent, View child, int left, int right) {
        canvas.save();
        parent.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
        int bottom = this.mBounds.bottom;
        int top = bottom - this.mDividerVer.getIntrinsicHeight();
        this.mDividerVer.setBounds(left, top, right, bottom);
        this.mDividerVer.draw(canvas);
        canvas.restore();
    }


    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildViewHolder(view) instanceof ChartsIndustryHolder ||
                parent.getChildViewHolder(view) instanceof ChartsPopularHolder) {
            outRect.set(0, 0, 1, 1);
        } else {
            outRect.set(0, 0, 0, 1);
        }
    }
}