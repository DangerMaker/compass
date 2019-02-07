package com.ez08.compass.ui.news.tab;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 触摸监视器
 * Created by caizepeng on 16/5/19.
 */
public abstract class RecyclerItemTouchListener implements RecyclerView.OnItemTouchListener{
    private RecyclerView recyclerView;
    private GestureDetectorCompat mGestureDetectorCompat;

    public RecyclerItemTouchListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(
                recyclerView.getContext(),new ItemTouchHelperGestureListener()
        );
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
    //手势识别类
    class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener{
        public ItemTouchHelperGestureListener() {
        }

        //普通的单击事件
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(viewHolder,e,viewHolder.getLayoutPosition());
            }
            return true;
        }
        //长按屏幕,超过一定时长就会触发
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null){
                RecyclerView.ViewHolder viewHolder =
                        recyclerView.findContainingViewHolder(child);
                onLongClick(viewHolder,e,viewHolder.getLayoutPosition());
            }
        }
    }

    public abstract  void  onItemClick(RecyclerView.ViewHolder viewHolder, MotionEvent e, int position);
    public abstract  void  onLongClick(RecyclerView.ViewHolder viewHolder, MotionEvent e, int position);
}
