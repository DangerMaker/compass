package com.ez08.compass.ui.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ez08.compass.R;

public class BaseFragment extends Fragment {

    protected Context mContext;
    private TypedArray typedArray;
    protected int contentColor;
    protected int titleColor;
    protected int redColor;
    protected int greenColor;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        typedArray = getActivity().obtainStyledAttributes(null, R.styleable.main_attrs, 0, 0);
        contentColor = getResources().getColor(typedArray.getResourceId(R.styleable.main_attrs_lable_item_style, 0));
        titleColor = getResources().getColor(typedArray.getResourceId(R.styleable.main_attrs_lable_list_style, 0));
        redColor = getResources().getColor(typedArray.getResourceId(R.styleable.main_attrs_red_main_color, 0));
        greenColor = getResources().getColor(typedArray.getResourceId(R.styleable.main_attrs_green_main_color, 0));
        mContext = getActivity();
    }

}
