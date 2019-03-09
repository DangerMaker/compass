package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.FenShiDesEntity;
import com.ez08.compass.entity.FenShiHistoryEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.entity.FenShiAllEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockMlineParser;
import com.ez08.compass.tools.DDSID;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.view.FenShiView;
import com.ez08.compass.ui.view.FiveAndDetailView;
import com.ez08.support.net.NetResponseHandler2;

import java.util.ArrayList;
import java.util.List;

public class FenshiFragment extends BaseFragment {

    private final static int WHAT_MLINE_HISTORY = 10008;  //分时
    private int mFenShiMark = 0;   //分时索引值，默认为0
    private boolean isFirstLoad = true;
    private long beforeColumn = 0;
    private float beforeValue = 0;
    private int sTurnValue; //图中最大量
    private int beforeDate = 0;   //分时最后一条数据的时间

    StockDetailEntity detailEntity;
    FenShiAllEntity fenShiAllEntity;
    FenShiView fenShiView;
    FiveAndDetailView fiveAndDetailView;

    private List<Float> mFenshiList;

    private List<Float> mAverageList;
    private List<ColumnValuesDataModel> mBarList;
    private List<FenShiDesEntity> mEntityList;

    public static FenshiFragment newInstance(StockDetailEntity detailEntity) {
        FenshiFragment fragment = new FenshiFragment();
        Bundle args = new Bundle();
        args.putSerializable("detail", detailEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fenshi, null);
        fenShiView = (FenShiView) view.findViewById(R.id.fenshi_view);
        fiveAndDetailView = (FiveAndDetailView) view.findViewById(R.id.in_time);
        if (getArguments() != null) {
            detailEntity = (StockDetailEntity) getArguments().getSerializable("detail");
            if (DDSID.isZ(detailEntity.getSecucode())) {
                fiveAndDetailView.setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFenshiList = new ArrayList<Float>();
        mAverageList = new ArrayList<Float>();
        mBarList = new ArrayList<ColumnValuesDataModel>();
        mEntityList = new ArrayList<FenShiDesEntity>();

        if(!DDSID.isZ(detailEntity.getSecucode())) {
            fiveAndDetailView.setFiveData(detailEntity);
        }
        NetInterface.getStockmLineNew(mHandler, WHAT_MLINE_HISTORY, detailEntity.getSecucode(), mFenShiMark);
    }


    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean b, Intent arg2) {
            switch (arg0) {
                case WHAT_MLINE_HISTORY:
                    StockMlineParser parser = new StockMlineParser();
                    fenShiAllEntity = parser.parse(arg2, detailEntity, mFenShiMark == 0);
                    List<FenShiHistoryEntity> history = fenShiAllEntity.getHistory();

                    if (history == null || history.isEmpty()) {
                        return;
                    }

                    List<Float> fenShiList = new ArrayList<Float>();
                    List<ColumnValuesDataModel> barList = new ArrayList<ColumnValuesDataModel>();
                    List<FenShiDesEntity> desList = new ArrayList<FenShiDesEntity>();

                    for (int i = 0; i < history.size(); i++) {
                        FenShiDesEntity desEntity = new FenShiDesEntity();
                        float lColumn; //当前量
                        float currValue = (float) history.get(i).getValue();
                        fenShiList.add(currValue); //趋势线添加

                        long currColumn = history.get(i).getColumn(); //当前总量
                        if (isFirstLoad && i == 0) {
                            isFirstLoad = false;
                            lColumn = currColumn;
                            beforeColumn = currColumn;
                            sTurnValue = (int) currColumn;
                            beforeValue = detailEntity.getLastclose();
                        } else {
                            lColumn = currColumn - beforeColumn;
                            beforeColumn = currColumn;
                            if (lColumn > sTurnValue) {
                                sTurnValue = (int) lColumn;
                            }
                        }

                        //量柱
                        ColumnValuesDataModel column;
                        if (currValue - beforeValue > 0) {
                            column = new ColumnValuesDataModel(CompassApp.GLOBAL.RED, CompassApp.GLOBAL.RED, lColumn);
                        } else if (currValue - beforeValue == 0) {
                            column = new ColumnValuesDataModel(CompassApp.GLOBAL.LIGHT_GRAY, CompassApp.GLOBAL.LIGHT_GRAY,
                                    lColumn);
                        } else {
                            column = new ColumnValuesDataModel(CompassApp.GLOBAL.GREEN, CompassApp.GLOBAL.GREEN,
                                    lColumn);
                        }
                        barList.add(column); //量添加

                        beforeValue = currValue;

                        float valueRate = 100 * (currValue - detailEntity.getLastclose())
                                / detailEntity.getLastclose();
                        float countAll = (float) history.get(i).getTurn();
                        desEntity.setCount(countAll);
                        float columnAll = (float) history.get(i).getColumn();
                        desEntity.setAcolumn(columnAll);
                        desEntity.setHigh(valueRate >= 0);
                        desEntity.setValue(MathUtils.getFormatPrice(detailEntity.getCurrent(), detailEntity.getDecm()));
                        desEntity.setColumn(MathUtils.getFormatUnit(lColumn / 100) + "手");
                        desEntity.setColumnAll(MathUtils.getFormatUnit(columnAll / 100) + "手"); // 总量
                        desEntity.setCountAll(MathUtils.getFormatUnit(countAll) + "元");//总额
                        desEntity.setValueRate(valueRate + "%");
                        desList.add(desEntity);
                    }

                    int lAfterDate = history.get(0).getDate();
                    if (beforeDate > lAfterDate) {
                        return;
                    }

                    beforeDate = history.get(history.size() - 1).getDate();

                    mFenshiList.clear();
                    mBarList.clear();
                    mEntityList.clear();
                    mFenshiList.addAll(fenShiList);
                    mBarList.addAll(barList);
                    mEntityList.addAll(desList);

                    List<Float> lAverageList = StockUtils.computeAverageLine(detailEntity.getSecucode(), mFenshiList, mEntityList);

                    mAverageList.clear();
                    mAverageList.addAll(lAverageList);
                    for (int i = 0; i < mAverageList.size(); i++) {
                        float value = mAverageList.get(i);
                        mEntityList.get(i).setValueAverage(MathUtils.getFormatPrice((int) value, detailEntity.getDecm()));
                    }

                    fenShiView.setPrice(detailEntity);
                    fenShiView.setTurnDes(MathUtils.getFormatUnit(sTurnValue / 100f) + "手");
                    fenShiView.setRealTimePriceData(mFenshiList);
                    fenShiView.setAverageLineData(mAverageList);
                    fenShiView.setBarData(mBarList);
                    fenShiView.setDesEntityList(mEntityList);
                    fenShiView.setCanRefresh(true);
                    fenShiView.setHandler(mHandler);

                    mFenShiMark = mFenshiList.size() - 1;
                    break;
            }
        }
    };
}
