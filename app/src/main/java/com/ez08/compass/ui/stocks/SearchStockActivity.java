package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.SearchItem;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.JumpHelper;
import com.ez08.compass.tools.SelfCodesManager;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.support.net.NetResponseHandler2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class SearchStockActivity extends BaseActivity implements View.OnClickListener {

    private static final int WHAT_GET_SEARCH_STOCK = 1005;
    private static final int NEW_WHAT_ADD = 102;

    ImageButton backBtn;
    EditText editText;
    private List<SearchItem> mItems = new ArrayList<>();
    private List<String> mStockCodes = new ArrayList<>();
    private SearchAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_search);

        backBtn = findViewById(R.id.back_btn);
        editText = findViewById(R.id.input_editor);
        backBtn.setOnClickListener(this);
        mListView = findViewById(R.id.lv);
        mAdapter = new SearchAdapter();
        mListView.setAdapter(mAdapter);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                search(editText.getText().toString());
                String abs = editText.getText().toString();
                if (TextUtils.equals(abs, "#888")) {
                    SharedPreferences sp = mContext.getSharedPreferences("exceptionsp", 0);
                    boolean exceptionSwitch = sp.getBoolean("switch", false);
                    if (!exceptionSwitch) {
                        SharedPreferences.Editor editor = sp
                                .edit();
                        editor.putBoolean("switch", true);
                        editor.putLong("time", System.currentTimeMillis());
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "您已开启调试模式", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SearchItem stock = (SearchItem) arg0.getItemAtPosition(arg2);
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < mItems.size(); i++) {
                    arrayList.add(mItems.get(i).getCode());
                }

                JumpHelper.startStockVerticalActivity(mContext,stock.getCode(),arrayList);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_SEARCH, "1",editText.getText().toString(),
                        System.currentTimeMillis());
            }
        });

        mStockCodes.clear();
        if (SelfCodesManager.finalSelfCodesList != null && SelfCodesManager.finalSelfCodesList.size() > 0) {
            for (int i = 0; i < SelfCodesManager.finalSelfCodesList.size(); i++) {
                mStockCodes.add(StockUtils.getStockCode(SelfCodesManager.finalSelfCodesList.get(i).getCode()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backBtn) {
            finish();
        }
    }

    private class SearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mItems.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mItems.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (arg1 == null) {
                arg1 = LayoutInflater.from(SearchStockActivity.this).inflate(
                        R.layout.item_sesrch_stock_result, null);
            }
            final SearchItem item = (SearchItem) getItem(arg0);
            TextView txtCode = (TextView) arg1.findViewById(R.id.txt_code);
            TextView txtName = (TextView) arg1.findViewById(R.id.txt_name);
            ImageView imgAdd = (ImageView) arg1.findViewById(R.id.img_add);
            TextView txtState = (TextView) arg1.findViewById(R.id.txt_state);

            if (mStockCodes.contains(item.getCode())) {
                txtState.setVisibility(View.VISIBLE);
                imgAdd.setVisibility(View.INVISIBLE);

            } else {
                txtState.setVisibility(View.INVISIBLE);
                imgAdd.setVisibility(View.VISIBLE);
            }

            imgAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    SelfCodesManager.addSelfCode(item.getCode());
                    refreshData();
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_SEARCH, "2", item.getCode(),
                            System.currentTimeMillis());
                }
            });
            txtCode.setText(item.getCode().substring(4));
            try {
                txtName.setText(URLDecoder.decode(item.getName(), "utf-8"));

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return arg1;
        }
    }

    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean arg1, Intent arg2) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case WHAT_GET_SEARCH_STOCK:
                    mItems.clear();
                    String[] resultList = arg2.getStringArrayExtra("list2");
                    if (resultList != null) {
                        for (int i = 0; i < resultList.length; i++) {
                            String result[] = resultList[i].split(",");
                            SearchItem item = new SearchItem();
                            item.setCode(result[0]);
                            item.setName(result[1]);
                            mItems.add(item);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void search(String key) {
        if (TextUtils.isEmpty(key.trim())) {
            mItems.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }
        NetInterface.requestSearchStock(mHandler, WHAT_GET_SEARCH_STOCK, key);
    }

    private void refreshData() {
        if (SelfCodesManager.finalSelfCodesList == null || SelfCodesManager.finalSelfCodesList.size() == 0) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        mStockCodes.clear();
        if (SelfCodesManager.finalSelfCodesList != null && SelfCodesManager.finalSelfCodesList.size() > 0) {
            for (int i = 0; i < SelfCodesManager.finalSelfCodesList.size(); i++) {
                mStockCodes.add(StockUtils.getStockCode(SelfCodesManager.finalSelfCodesList.get(i).getCode()));
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
