//package com.ez08.compass.ui.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import com.ez08.compass.R;
//import com.ez08.compass.tools.AppUtils;
//
//public class EasyMsgEditTextView extends FrameLayout implements View.OnClickListener {
//
//    LinearLayout normalLayout;
//    LinearLayout upLayout;
//    EditText editText;
//
//    public EasyMsgEditTextView(Context context) {
//        super(context);
//    }
//
//    public EasyMsgEditTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        normalLayout = findViewById(R.id.normal_layout);
//        upLayout = findViewById(R.id.up_layout);
//        normalLayout.setOnClickListener(this);
//
//        editText = findViewById(R.id.cs_input);
//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if(!hasFocus){
//                   AppUtils.hideSoftInput(editText);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.normal_layout:
//                normalLayout.setVisibility(GONE);
//                upLayout.setVisibility(VISIBLE);
//                AppUtils.showSoftInput(editText);
//                break;
//        }
//
//    }
//}
