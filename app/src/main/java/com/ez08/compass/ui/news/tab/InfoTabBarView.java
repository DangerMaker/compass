package com.ez08.compass.ui.news.tab;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.ez08.compass.R;
import com.ez08.compass.entity.InfoTabEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class InfoTabBarView extends LinearLayout implements View.OnClickListener {
    private RecyclerView myResyclerView;
    private InfoTabAdapter myAdapter;
    private InfoTabAdapter sysAdapter;
    private RecyclerView sysRecyclerView;
    private DragItemHelper dragHelper;
    private Context mContext;
    private ArrayList<InfoTabEntity> mCategoryList;

    private  boolean mIsEdite=false;
    public InfoTabBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = View.inflate(context, R.layout.info_tab_bar_layout, null);
        addView(view);
    }

    private void initView() {
        myResyclerView = (RecyclerView) findViewById(R.id.myTab_recyclerView);
        myResyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        myAdapter = new InfoTabAdapter(mContext);
        myAdapter.setData(initMyDaya());
        myAdapter.setStatus(true);
        myResyclerView.setAdapter(myAdapter);
        myResyclerView.addItemDecoration(new SpaceItemDecoration(20));

        sysRecyclerView = (RecyclerView) findViewById(R.id.system_recyClerView);
        sysRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        sysAdapter = new InfoTabAdapter(mContext);
        sysAdapter.setData(initSysyData());
        if(sysAdapter.getData().size()==0){
            findViewById(R.id.info_bar_bottom_title).setVisibility(INVISIBLE);
        }else{
            findViewById(R.id.info_bar_bottom_title).setVisibility(VISIBLE);
        }
        sysRecyclerView.setAdapter(sysAdapter);
        sysRecyclerView.addItemDecoration(new SpaceItemDecoration(20));


        myAdapter.setEdit(false);
        sysAdapter.setEdit(false);
        myAdapter.notifyDataSetChanged();
        sysAdapter.notifyDataSetChanged();
    }

    public void setEdite(boolean isEdite){
        mIsEdite=isEdite;
        myAdapter.setEdit(mIsEdite);
        myAdapter.notifyDataSetChanged();
        sysAdapter.setEdit(mIsEdite);
        sysAdapter.notifyDataSetChanged();
    }

    public List<String> initSysyData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < mCategoryList.size(); i++) {
            InfoTabEntity entity = mCategoryList.get(i);
            if (entity.getCategory().equals("1")) {
                datas.add(entity.getName());
            }
        }
        return datas;
    }

    public List<String> initMyDaya() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < mCategoryList.size(); i++) {
            InfoTabEntity entity = mCategoryList.get(i);
            if (entity.getCategory().equals("0")) {
                datas.add(entity.getName());
            }
        }
        return datas;
    }

    @Override
    public void onClick(View v) {

    }

    private void refreshData() {
        mCategoryList.clear();
        for (int i = 0; i < myAdapter.getData().size(); i++) {
            mCategoryList.add(new InfoTabEntity(myAdapter.getData().get(i), "0"));
        }
        for (int i = 0; i < sysAdapter.getData().size(); i++) {
            mCategoryList.add(new InfoTabEntity(sysAdapter.getData().get(i), "1"));
        }
    }

    public String getData() {
        refreshData();
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < mCategoryList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", mCategoryList.get(i).getName());
                obj.put("state", mCategoryList.get(i).getCategory());
                array.put(obj);
            }
            return array.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setDataList(final ArrayList<InfoTabEntity> mCategoryList) {
        this.mCategoryList = mCategoryList;
        initView();
        dragHelper = new DragItemHelper(myResyclerView, myAdapter);
        //第0,1个不可拖拽
        dragHelper.addInvalidPos(0);
        dragHelper.addInvalidPos(1);
        /**
         * 当前栏目的点击操作
         */
        myResyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(myResyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, MotionEvent e, int position) {
                if (myAdapter.isEdit()) {
                    if(position==0||position==1){
                        return;
                    }
                    //处于编辑状态
                    List<String> myDatas = myAdapter.getDatas();
                    if (position < myDatas.size()) {
                        sysAdapter.getData().add(0, myDatas.get(position));
                        myAdapter.getData().remove(position);
                        myAdapter.notifyDataSetChanged();
                        sysAdapter.notifyDataSetChanged();
                    }
                    refreshData();
                } else {
//                    myAdapter.setCurrSelectPos(position);
//                    myAdapter.notifyDataSetChanged();
                    mCallBack.getCurrengPosition(mCategoryList.get(position).getName());
//                    Toast.makeText(mContext, "跳转Fragment", Toast.LENGTH_SHORT).show();
                }

                if(sysAdapter.getData().size()==0){
                    findViewById(R.id.info_bar_bottom_title).setVisibility(INVISIBLE);
                }else{
                    findViewById(R.id.info_bar_bottom_title).setVisibility(VISIBLE);
                }
            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder viewHolder, MotionEvent e, int position) {
                if(!mIsEdite){
                    mIsEdite=true;
                    myAdapter.setEdit(mIsEdite);
                    sysAdapter.setEdit(mIsEdite);
                    myAdapter.notifyDataSetChanged();
                    sysAdapter.notifyDataSetChanged();
                    mCallBack.isEdite(true);
                    return;
                }

            }
        });
        /***
         * 系统菜单栏目的点击
         */
        sysRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(sysRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, MotionEvent e, int position) {

                if (sysAdapter.getData().size() > position) {
                    String newData = sysAdapter.getData().get(position);
                    myAdapter.getData().add(newData);
                    sysAdapter.getData().remove(position);
                    myAdapter.notifyDataSetChanged();
                    sysAdapter.notifyDataSetChanged();
                }

                if(sysAdapter.getData().size()==0){
                    findViewById(R.id.info_bar_bottom_title).setVisibility(INVISIBLE);
                }else{
                    findViewById(R.id.info_bar_bottom_title).setVisibility(VISIBLE);
                }

//                if (sysAdapter.isEdit()) {
//                    if (sysAdapter.getData().size() > position) {
//                        String newData = sysAdapter.getData().get(position);
//                        myAdapter.getData().add(newData);
//                        sysAdapter.getData().remove(position);
//                        myAdapter.notifyDataSetChanged();
//                        sysAdapter.notifyDataSetChanged();
//                    }
//                    refreshData();
//                }else{
////                    mCallBack.getCurrengPosition(sysAdapter.getData().get(position));
//                    Toast.makeText(mContext,"请添加到展示列表",Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder viewHolder, MotionEvent e, int position) {
//                if(!mIsEdite){
//                    mIsEdite=true;
//                    myAdapter.setEdit(mIsEdite);
//                    sysAdapter.setEdit(mIsEdite);
//                    myAdapter.notifyDataSetChanged();
//                    sysAdapter.notifyDataSetChanged();
//                    return;
//                }
            }
        });
    }

    public void setEditeCallBack(CallBack mCallBack){
        this.mCallBack=mCallBack;
    }

    private CallBack mCallBack;
    public interface CallBack{
        public void getCurrengPosition(String category);
        public void isEdite(boolean edite);
    }
}
