package com.ez08.compass.ui.trader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.compass.ui.trader.sort.CharacterParser;
import com.ez08.compass.ui.trader.sort.PinyinComparator;
import com.ez08.compass.ui.trader.sort.SideBar;
import com.ez08.compass.ui.trader.sort.SortAdapter;
import com.ez08.compass.ui.trader.sort.SortModel;
import com.ez08.compass.ui.view.MyDelEditetext;
import com.ez08.support.net.NetResponseHandler2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecurityListActivity extends BaseActivity implements View.OnClickListener {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private MyDelEditetext mClearEditText;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    private final int WHAT_REQUEST_TRADE_LIST = 1001;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    public  String[] codes = { "#" ,"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    private String mAddName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_TRADERS, "0", "",
                System.currentTimeMillis());
        setContentView(R.layout.activity_security_list);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.security_back).setOnClickListener(this);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                SortModel item=((SortModel) adapter.getItem(position));
                if(!item.isHasAdd()) {
                    String name=((SortModel) adapter.getItem(position)).getName();
                    name=name.replace("#","");
                    NetInterface.setMyTrade(mHandler, WHAT_REQUEST_TRADE_LIST, "+", name);
                    mAddName=name;
                }
            }
        });

//        SourceDateList = filledData(getResources().getStringArray(R.array.date));

        mClearEditText = (MyDelEditetext) findViewById(R.id.search_sort_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Intent intent = getIntent();
        String[] list = intent.getStringArrayExtra("list");
        String[] myData = intent.getStringArrayExtra("mylist");
        String[] recommendList = intent.getStringArrayExtra("recommendList");
        SourceDateList = filledData(list,myData);
        List<SortModel> lList=filledRecommend(list,recommendList,myData);
        SourceDateList.addAll(lList);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(SecurityListActivity.this, SourceDateList);
        sortListView.setAdapter(adapter);
//        NetInterface.getTradeList(mHandler,WHAT_REQUEST_TRADE_LIST);
    }


    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int i, boolean b, Intent intent) {
            switch (i) {
                case WHAT_REQUEST_TRADE_LIST:
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_TRADERS, "2", mAddName,
                            System.currentTimeMillis());
                    setResult(1002);
                    finish();
                    break;
            }
        }
    };

    private List<SortModel> filledRecommend(String[] date,String[] recommend,String []myData){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0;i<recommend.length;i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(recommend[i]);
            mSortList.add(sortModel);
            for(int j=0;j<date.length;j++){
                String[] item = date[j].split(";");
                if(recommend[i].contains(item[0])){
                    sortModel.setUrl(item[1]);
                    sortModel.setPackageName(item[2]);
                }
            }

            if(myData!=null) {

                for (int j = 0; j < myData.length; j++) {
                    if (recommend[i].contains(myData[j])) {
                        sortModel.setHasAdd(true);
                    }
                }
            }

            //汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
        }
        return mSortList;
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date,String[] myData) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        List<String> codesList=new ArrayList<>();
        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            String[] item = date[i].split(";");
            sortModel.setName(item[0]);
            sortModel.setUrl(item[1]);
            sortModel.setPackageName(item[2]);
            if(myData!=null){
                for(int j=0;j<myData.length;j++){
                    if(myData[j].equals(item[0])){
                        sortModel.setHasAdd(true);
                    }
                }
            }
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }


        for(int i=0;i<codes.length;i++){
            for(int j=0;j<mSortList.size();j++){
                if(codes[i].equals(mSortList.get(j).getSortLetters())){
                    codesList.add(codes[i]);
                    break;
                }
            }
        }

        sideBar.setCodes((ArrayList<String>) codesList);
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {

        List<SortModel> filterDateList = new ArrayList<SortModel>();
        String tarName="";
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                tarName=name;
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
//        CompassApp.addStatis(CompassApp.mgr.TRADE_TRADERS, "1", tarName,
//                System.currentTimeMillis());
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_back:
                finish();
                break;
        }
    }
}
