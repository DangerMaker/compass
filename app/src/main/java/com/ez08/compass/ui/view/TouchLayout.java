package com.ez08.compass.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ez08.compass.R;

public class TouchLayout extends RelativeLayout {
	private RelativeLayout mTitleBar;
	private RelativeLayout mEvu;
	private int backgroundColorNormal;
	public TouchLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(null, R.styleable.main_attrs,0, 0);
		backgroundColorNormal = getResources().getColor(a.getResourceId(R.styleable.main_attrs_shadow_button,0));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("","");
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		Log.e("","");
//		if(mEvu.getVisibility()==View.VISIBLE){
//			mEvu.setVisibility(View.GONE);
//			mTitleBar.setBackgroundColor(backgroundColorNormal);
//			return true;
//		}
		return super.onInterceptTouchEvent(ev);
	}
	
	public void setView(RelativeLayout mTitleBar, RelativeLayout mEvu){
		this.mEvu=mEvu;
		this.mTitleBar=mTitleBar;
	}
}
