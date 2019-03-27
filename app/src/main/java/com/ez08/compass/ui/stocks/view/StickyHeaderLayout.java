package com.ez08.compass.ui.stocks.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;

import com.ez08.compass.R;

public class StickyHeaderLayout extends LinearLayout implements NestedScrollingParent2 {

    LinearLayout stickyHeader;
    Scroller mScroller;

    int maxScrollY; // 最大滚动距离

    public StickyHeaderLayout(Context context) {
        super(context);
    }

    public StickyHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        mScroller = new Scroller(getContext());
    }

    int mTopViewHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = stickyHeader.getMeasuredHeight();
        maxScrollY = mTopViewHeight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        stickyHeader = findViewById(R.id.sticky_header);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View view, @NonNull View view1, int axes, int i1) {
        Log.e("StickyHeaderLayout", "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view1, int i, int i1) {
        Log.e("StickyHeaderLayout", "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(@NonNull View view, int i) {
        Log.e("StickyHeaderLayout", "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(@NonNull View view, int i, int i1, int i2, int i3, int i4) {
        Log.e("StickyHeaderLayout", "onNestedScroll");
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e("StickyHeaderLayout", "onNestedPreFling");
//        if (velocityY > 0 && getScrollY() < maxScrollY) // 向上滑动, 且当前View还没滑到最顶部
//        {
//            fling((int) velocityY, maxScrollY);
//            return true;
//        } else if (velocityY < 0 && getScrollY() > 0) // 向下滑动, 且当前View部分在屏幕外
//        {
//            fling((int) velocityY, 0);
//            return true;
//        }
        return true;
    }


    @Override
    public void onNestedPreScroll(@NonNull View view, int dx, int dy, @NonNull int[] consumed, int i2) {
        Log.e("StickyHeaderLayout", "onNestedPreScroll");
        // dy > 0表示子View向上滑动;

        // 子View向上滑动且父View的偏移量<ImageView高度
        boolean hiddenTop = dy > 0 && getScrollY() < maxScrollY;

        // 子View向下滑动(说明此时父View已经往上偏移了)且父View还在屏幕外面, 另外内部View不能在垂直方向往下移动了
        /**
         * ViewCompat.canScrollVertically(view, int)
         * 负数: 顶部是否可以滚动(官方描述: 能否往上滚动, 不太准确吧~)
         * 正数: 底部是否可以滚动
         */
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(view, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    public void fling(int velocityY, int maxY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, maxY);
        invalidate();
    }

//    @Override
//    public void scrollTo(int x, int y) {
//        if (y < 0) // 不允许向下滑动
//        {
//            y = 0;
//        }
//        if (y > maxScrollY) // 防止向上滑动距离大于最大滑动距离
//        {
//            y = maxScrollY;
//        }
//        if (y != getScrollY()) {
//            super.scrollTo(x, y);
//        }
//    }


    //整个的ScrollView的高度
    private int scrollHeight = 0;
    private float lastY = 0;
    /**
     * 在自定义view中经常使用的是重新绘制
     * 在viewGroup中经常使用scrollBy等进行移动
     */

    private int windowHeight;
    //这是进行记录横向或者竖向是否进行滑动了
    private float moveY = 0;

    private VelocityTracker mVelocityTracker;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        scrollHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //添加上所有view的margin，获取整个子view的高度
            scrollHeight += child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
        }
        //获取ScrollView的高度
        windowHeight = getMeasuredHeight();
        Log.e("TAG", "MyScrollView---scrollHeight---" + scrollHeight + "   windowHeight----" + windowHeight);

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                lastY = moveY = ev.getY();
//                mScroller.forceFinished(true);
//                if (mVelocityTracker == null) {
//                    mVelocityTracker = VelocityTracker.obtain();
//                } else {
//                    mVelocityTracker.clear();
//                }
//                mVelocityTracker.addMovement(ev);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("TAG", "MyScrollView---getScrollY====" + getScrollY());
//                mVelocityTracker.addMovement(ev);
//                float curY = ev.getY();
//                scrollBy(0, -(int) (curY - lastY));
//                invalidate();
//                lastY = curY;
//                break;
//            case MotionEvent.ACTION_UP:
//
//                //这里做了一个回弹的效果，在第二个参数中设置了滚动了多少距离，然后dy为它的负值，立马就回弹回去
//                //这里肯定不能传递ev.getX()，因为getX()是获取的手指点击的位置,因此一定要使用getScrollY()，这是获取的滚动后的距离。
//                //这里getScrollY()是在scrollTo()或scrollBy()中进行赋值。因此要调用这个方法，一定要先调用这两个方法。
//                //startScroll()方法不适合在action_move中调用，因为这个方法默认的时间就是250毫秒，频繁的使用postInvalidate()进行刷新，就会导致移动动作的
//                //覆盖，反而出现很难移动的效果。因为action_move的回调很快，每个十几像素就回调了。如果将startScroll()的第五个参数设置为0，也就是间隔时间设置为0
//                //mScroller.startScroll(0,(int)getScrollY(),0,-(int)(curY-lastY),0);  那么就出现了和scrollBy()相似的效果
//                mScroller.abortAnimation();
//                if (getScrollY() < 0) {//证明达到上边界了，这时候要进行回弹处理
//                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
//                    postInvalidate();// 重绘执行computeScroll()
//                } else if (windowHeight + getScrollY() > scrollHeight) {//达到最底部
//                    mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - (scrollHeight - windowHeight)));
//                    postInvalidate();// 重绘执行computeScroll()
//                } else {
//                    mVelocityTracker.computeCurrentVelocity(1000);
//                    float yVelocity = mVelocityTracker.getYVelocity();
////                    Log.e("TAG", "MyScrollView---yVelocity====" + yVelocity);
//                    /**
//                     * fling 方法参数注解
//                     * startX 滚动起始点X坐标
//                     * startY 滚动起始点Y坐标
//                     * velocityX   当滑动屏幕时X方向初速度，以每秒像素数计算
//                     * velocityY   当滑动屏幕时Y方向初速度，以每秒像素数计算
//                     * minX    X方向的最小值，scroller不会滚过此点。
//                     *　maxX    X方向的最大值，scroller不会滚过此点。
//                     *　minY    Y方向的最小值，scroller不会滚过此点。
//                     *　maxY    Y方向的最大值，scroller不会滚过此点。
//                     */
//                    if (Math.abs(yVelocity) > 50) {
////                        mScroller.extendDuration(2000);
//                        mScroller.fling(0, getScrollY(), 0, -(int) yVelocity,
//                                0, 0,
//                                0, (scrollHeight - windowHeight));
//                        postInvalidate();
//                    }
//                }
//                //进行计算移动的距离
//                moveY = Math.abs(ev.getY() - moveY);
//                //如果横向或者竖向已经移动了一段距离，那么就不能响应子控件的点击事件
//                if (moveY > 10) {
//                    return true;
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

//    @Override
//    public void computeScroll() {
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            invalidate();
//        }
//    }
}
