package com.ez08.compass.ui.news.tab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ez08.compass.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class InfoTabAdapter extends RecyclerView.Adapter<MyViewHolder> implements DragItemData {
    private Context context;
    private List<String> data;
    private boolean isEdit;//是否处于可编辑模式
    private int currSelectPos = -1;//当前选择的position
    private boolean myAdapter=false;

    public int getCurrSelectPos() {
        return currSelectPos;
    }

    public void setCurrSelectPos(int currSelectPos) {
        this.currSelectPos = currSelectPos;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public InfoTabAdapter(Context context) {
        this.context = context;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setStatus(boolean myAdapter) {
        this.myAdapter=myAdapter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView=null;
        if(myAdapter){
            mView = LayoutInflater.from(context).inflate(R.layout.info_bar_group_layout, null);
        }else{
            mView = LayoutInflater.from(context).inflate(R.layout.info_bar_group_nor, null);
        }
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(data.get(position));
        if (isEdit && (position != 0 && position != 1 && position != 2)&&myAdapter) {
            holder.mImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mImageView.setVisibility(View.GONE);
        }
//        if (currSelectPos == position) {
//            Log.e("DEMO", "当前点击的是 == " + getData().get(position));
//            holder.mTextView.setBackground(context.getResources().getDrawable(R.drawable.item_selectedbg));
//        } else {
//            holder.mTextView.setBackground(context.getResources().getDrawable(R.drawable.item_bg));
//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getDatas() {
        return this.data;
    }

}


class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public ImageView mImageView;

    public MyViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.itemDemo_tv_name);
        mImageView = (ImageView) itemView.findViewById(R.id.itemDemo_im_del);
    }
}
