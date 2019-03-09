package com.ez08.compass.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.entity.KLineValuesDataModel;
import com.ez08.compass.entity.StockDesEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.view.drawutils.EzBarChart;
import com.ez08.compass.ui.view.drawutils.EzBarVolume;
import com.ez08.compass.ui.view.drawutils.EzFixedAreaLine;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine;
import com.ez08.compass.ui.view.drawutils.EzKLineChart;

import java.util.ArrayList;
import java.util.List;

public class KLineView extends View {

    public static final int ONE_SCREEN_KLINE_NUM = 52;
    public static final int VOLUME_UNIT = 10000;  //kChat 返回量单位是万，显示转换成亿
    private Context mContext;
    private Handler handler;
    private ScaleGestureDetector mScaleGestureDetector = null;

    private int redcolor;
    private int greencolor;

    private EzKLineChart kLineChart; // 画k线
    // 画线图
    private EzBarVolume ezbarChart; // 画柱状图
    private EzFixedGapLine ma5Line;
    private EzFixedGapLine ma10Line;
    private EzFixedGapLine ma20Line;
    private EzFixedGapLine ma60Line;

    // 成交量
    private EzFixedGapLine amountMa5Line;
    private EzFixedGapLine amountMa10Line;
    private EzFixedGapLine amountMa20Line;
    // macd
    private EzFixedGapLine macdDifLine;
    private EzFixedGapLine macdDeaLine;
    private EzBarChart macdBarLine;
    // kdj
    private EzFixedGapLine kdjKLine;
    private EzFixedGapLine kdjDLine;
    private EzFixedGapLine kdjJLine;
    // bias
    private EzFixedGapLine bias1Line;
    private EzFixedGapLine bias2Line;
    private EzFixedGapLine bias3Line;
    // roc
    private EzFixedGapLine rocLine;
    private EzFixedGapLine rocmaLine;
    // rsi
    private EzFixedGapLine rsi6Line;
    private EzFixedGapLine rsi12Line;
    // cci
    private EzFixedGapLine cciLine;
    //    // d股活跃度
//    private EzFixedHYDLine hydLine;
    // asi
    private EzFixedGapLine asiLine;
    // psy
    private EzFixedGapLine psyLine;
    //主力资金
    private EzBarChart zhuBarChart;
    private EzFixedAreaLine zhuLine;
    //多空资金
    private EzBarChart dkBarChart;
    private EzFixedAreaLine dkLine;

    //敢死队资金
    private EzBarChart gsdBarChart;
    private EzFixedAreaLine gsdLine;

    private CGPoint mKposition; // k线坐标
    private CGPoint mIndexposition; // 指数坐标
    private CGPoint mColumnposition; // 成交量坐标
    private Paint dashedPaint; // 虚线
    private Paint massPaint; // 实线
    private Paint rectPaint; // 矩形画笔
    private Paint ma5Paint; //
    private Paint ma10Paint; //
    private Paint ma20Paint; //
    private Paint ma60Paint; //
    private Paint datePaint; //
    private Paint numPaint; //
    private Paint scrollPaint;
    private Paint desPaint; //
    private Paint redPaint; //
    private Paint greenPaint; //
    private Paint grayPaint; //
    private Paint yellowPaint; //
    private Paint cursorPaint;
    private Paint blackPaint;
    private Paint shiziPaint;
    private Paint loadPaint; //
    private Paint navPaint;
    private Paint guideBitmapPaint; // 画笔 PC导引
    private Paint guideTextPaint; // 画笔 PC导引

    private float mHighestData;//最大数
    private float mLowestData;//最小数
    private float mHigher1Data;//最大数下面第一个数
    private float mHigher2Data;//最大数下面第二个数
    private float mHigher3Data;//最大数下面第三个数

    private String mAmountDes; // 成交量最大值
    private String mMacdDesHigh; // macd最大值
    private String mMacdDesNormal; // macd中间值
    private String mMacdDesLow; // macd最小值

    private String mZhuDesHigh = "0";//主力资金最大值
    private String mZhuDesNormal = "0";//主力资金中间值
    private String mZhuDesLow = "0";//主力资金最小值

    private String mDKDesHigh = "0";//多空资金最大值
    private String mDKDesNormal = "0";//多空资金中间值
    private String mDKDesLow = "0";//多空资金最小值

    private String mGSDDesHigh = "0";//敢死队资金最大值
    private String mGSDDesNormal = "0";//敢死队资金中间值
    private String mGSDDesLow = "0";//敢死队资金最小值

    private String mBiasDesHigh; // bias最大值
    private String mBiasDesNormal; // bias中间值
    private String mBiasDesLow; // bias最小值

    private String mCciDesHigh; //
    private String mCciDesNormal; //
    private String mCciDesLow; //

    private String mHydDesHigh; //
    private String mHydDesNormal; //
    private String mHydDesLow; //

    private String mRocDesHigh; //
    private String mRocDesNormal; //
    private String mRocDesLow; //

    private String mAsiDesHigh; //
    private String mAsiDesNormal; //
    private String mAsiDesLow; //

    private String mPsyDesHigh; //
    private String mPsyDesNormal; //
    private String mPsyDesLow; //

    Bitmap bitmap_dk_day;
    Bitmap bitmap_gsd_day;
    Bitmap bitmap_hyd_day;
    Bitmap bitmapScale;
    Bitmap r2rBitmap; //资金异向柱、面积图切换
    Bitmap navRectBitmap; //资金剪头

    RectF chartRectF; //指标区域
    RectF scaleRectF; //切换到竖屏区域
    RectF scaleRealRectF; //上边的画图用，点击区域要大一些
    RectF r2rRectF; //资金展示切换区域
    RectF zjRectF;//资金切换区域

    // --
    private boolean canRefresh = false;
    private boolean ifcanMove = true;
    private boolean mIndex = false; // 是否指数
    private boolean isScrollDetail = false; //画布是否进入十字线详细状态
    private boolean isVer = true;

    private int width, height; // 控件宽高
    private int dashedGroup[]; // 虚线数组
    private float[][] uprightGroup; // 矩形数组
    private float[][] guideGroup; // 引导图边界数组
    float bitmapWidth = 0;
    float imageWidth;
    float imageHeight;
    float h1;
    float h2;
    float barLeftGap = 2;
    private int lBeforeIndex = 0;

    private float mKdjMarginHeight = 0; // 80,20的高度
    private float mRsiMarginHeight = 0; // 80,20的高度
    private float middleTextOriginY = 0;

    private int mBarType = -1;  //31 主力三日资金 32 敢死队三日金 33 多空资金
    private int indexNum = 0;
    private int lengthNum = 0;
    private float widthAverage = 0;
    private float lineLeftGap = 0;
    private float interval = 0;
    private int decm = 4;


    StockDetailEntity detailEntity;
    private List<StockDesEntity> mDesEntityList; //详细的数据，原始值+计算后的指标
    private List<KChartEntity> mTotalList; // 传入总的数据源，原始值

    private List<Float> ma5List;
    private List<Float> ma10List;
    private List<Float> ma20List;
    private List<Float> ma60List;
    private List<ColumnValuesDataModel> column;

    private List<ColumnValuesDataModel> zhuColumn;//主力资金数据集
    private List<ColumnValuesDataModel> dkColumn;//多空资金数据集
    private List<ColumnValuesDataModel> gsdColumn;//敢死队资金数据集


    public KLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mContext = context;
        redcolor = CompassApp.GLOBAL.RED;
        greencolor = CompassApp.GLOBAL.GREEN;
//        mScaleGestureDetector = new ScaleGestureDetector(context, listener);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        initData();
    }

    private void init() {
        kLineChart = new EzKLineChart();
        kLineChart.setRiseColor(redcolor);
        kLineChart.setFallColor(greencolor);

        ezbarChart = new EzBarVolume();

        ma5Line = new EzFixedGapLine();
        ma5Line.setLineColor(getResources().getColor(R.color.orange));
        ma5Line.setLineWidth(2.0f);

        ma10Line = new EzFixedGapLine();
        ma10Line.setLineColor(getResources().getColor(R.color.fuchsia));
        ma10Line.setLineWidth(2.0f);

        ma20Line = new EzFixedGapLine();
        ma20Line.setLineColor(greencolor);
        ma20Line.setLineWidth(2.0f);

        ma60Line = new EzFixedGapLine();
        ma60Line.setLineColor(getResources().getColor(R.color.blue));
        ma60Line.setLineWidth(2.0f);

        amountMa5Line = new EzFixedGapLine();
        amountMa5Line.setLineColor(getResources().getColor(R.color.orange));
        amountMa5Line.setLineWidth(2.0f);

        amountMa10Line = new EzFixedGapLine();
        amountMa10Line.setLineColor(getResources().getColor(R.color.fuchsia));
        amountMa10Line.setLineWidth(2.0f);

        amountMa20Line = new EzFixedGapLine();
        amountMa20Line.setLineColor(greencolor);
        amountMa20Line.setLineWidth(2.0f);

        macdDifLine = new EzFixedGapLine();
        macdDifLine.setLineColor(getResources().getColor(R.color.orange));
        macdDifLine.setLineWidth(2.0f);

        macdDeaLine = new EzFixedGapLine();
        macdDeaLine.setLineColor(greencolor);
        macdDeaLine.setLineWidth(2.0f);

        macdBarLine = new EzBarChart();

        zhuBarChart = new EzBarChart();

        zhuLine = new EzFixedAreaLine();
        zhuLine.setLineWidth(2.0f);

        dkBarChart = new EzBarChart();

        dkLine = new EzFixedAreaLine();
        dkLine.setLineWidth(2.0f);

        gsdBarChart = new EzBarChart();

        gsdLine = new EzFixedAreaLine();
        gsdLine.setLineWidth(2.0f);

        psyLine = new EzFixedGapLine();
        psyLine.setLineColor(getResources().getColor(R.color.orange));
        psyLine.setLineWidth(2.0f);

        asiLine = new EzFixedGapLine();
        asiLine.setLineColor(getResources().getColor(R.color.orange));
        asiLine.setLineWidth(2.0f);

        cciLine = new EzFixedGapLine();
        cciLine.setLineColor(getResources().getColor(R.color.orange));
        ;

//        hydLine = new EzFixedHYDLine();
//        hydLine.setLineWidth(2.0f);

        rsi6Line = new EzFixedGapLine();
        rsi6Line.setLineColor(getResources().getColor(R.color.orange));
        rsi6Line.setLineWidth(2.0f);

        rocLine = new EzFixedGapLine();
        rocLine.setLineColor(getResources().getColor(R.color.fuchsia));
        rocLine.setLineWidth(2.0f);

        rocmaLine = new EzFixedGapLine();
        rocmaLine.setLineColor(getResources().getColor(R.color.blue));
        rocmaLine.setLineWidth(2.0f);

        rsi12Line = new EzFixedGapLine();
        rsi12Line.setLineColor(getResources().getColor(R.color.blue));
        rsi12Line.setLineWidth(2.0f);

        kdjKLine = new EzFixedGapLine();
        kdjKLine.setLineColor(getResources().getColor(R.color.orange));
        kdjKLine.setLineWidth(2.0f);

        bias1Line = new EzFixedGapLine();
        bias1Line.setLineColor(getResources().getColor(R.color.orange));
        bias1Line.setLineWidth(2.0f);

        bias2Line = new EzFixedGapLine();
        bias2Line.setLineColor(getResources().getColor(R.color.blue));
        bias2Line.setLineWidth(2.0f);

        bias3Line = new EzFixedGapLine();
        bias3Line.setLineColor(getResources().getColor(R.color.fuchsia));
        bias3Line.setLineWidth(2.0f);

        kdjDLine = new EzFixedGapLine();
        kdjDLine.setLineColor(getResources().getColor(R.color.blue));
        kdjDLine.setLineWidth(2.0f);

        kdjJLine = new EzFixedGapLine();
        kdjJLine.setLineColor(getResources().getColor(R.color.fuchsia));
        kdjJLine.setLineWidth(2.0f);

        dashedPaint = new Paint();//虚线画笔
        dashedPaint.setStrokeWidth(3);
        dashedPaint.setColor(getResources().getColor(R.color.shadow1));

        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dashedPaint.setStyle(Paint.Style.STROKE);
        dashedPaint.setPathEffect(effects);
        dashedPaint.setAntiAlias(true);

        massPaint = new Paint();//实线画笔
        massPaint.setStrokeWidth(3);
        massPaint.setColor(getResources().getColor(R.color.shadow1));

        rectPaint = new Paint();
        rectPaint.setStrokeWidth(3);
        rectPaint.setColor(getResources().getColor(R.color.shadow1));
        rectPaint.setStyle(Paint.Style.STROKE);
        dashedGroup = new int[4];
        uprightGroup = new float[2][4];
        guideGroup = new float[1][4];

        ma5Paint = new Paint();
        ma5Paint.setTextSize(UtilTools.dip2px(mContext, 10));
        ma5Paint.setAntiAlias(true);
        ma5Paint.setColor(mContext.getResources().getColor(R.color.orange));
        ma10Paint = new Paint();
        ma10Paint.setAntiAlias(true);
        ma10Paint.setTextSize(UtilTools.dip2px(mContext, 10));
        ma10Paint.setColor(mContext.getResources().getColor(R.color.fuchsia));
        ma20Paint = new Paint();
        ma20Paint.setAntiAlias(true);
        ma20Paint.setTextSize(UtilTools.dip2px(mContext, 10));
        ma20Paint.setColor(greencolor);
        ma60Paint = new Paint();
        ma60Paint.setAntiAlias(true);
        ma60Paint.setTextSize(UtilTools.dip2px(mContext, 10));
        ma60Paint.setColor(mContext.getResources().getColor(R.color.blue));

        datePaint = new Paint();
        datePaint.setAntiAlias(true);
        datePaint.setTextSize(UtilTools.dip2px(mContext, 10));
        datePaint.setColor(mContext.getResources().getColor(
                R.color.lable_item_style));

        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        numPaint.setColor(mContext.getResources().getColor(R.color.shadow));
//        hydLine.setNumColor(datePaint);

        desPaint = new Paint();
        desPaint.setAntiAlias(true);
        desPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        desPaint.setColor(mContext.getResources().getColor(R.color.gray));

        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        redPaint.setColor(redcolor);

        greenPaint = new Paint();
        greenPaint.setAntiAlias(true);
        greenPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        greenPaint.setColor(greencolor);


        grayPaint = new Paint();
        grayPaint.setAntiAlias(true);
        grayPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        grayPaint.setColor(mContext.getResources().getColor(R.color.lable_list_style));

        yellowPaint = new Paint();
        yellowPaint.setAntiAlias(true);
        yellowPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        yellowPaint.setColor(mContext.getResources().getColor(R.color.yellow));

        cursorPaint = new Paint();
        cursorPaint.setColor(Color.parseColor("#147EFE"));
        cursorPaint.setStyle(Paint.Style.FILL);

        blackPaint = new Paint();
        blackPaint.setColor(Color.WHITE);
        blackPaint.setStrokeWidth(2);
        blackPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        loadPaint = new Paint();
        loadPaint.setTextSize(50);
        loadPaint.setColor(mContext.getResources().getColor(R.color.gray));

        navPaint = new Paint();
        navPaint.setColor(Color.GRAY);

        shiziPaint = new Paint();
        shiziPaint.setColor(Color.BLACK);

        scrollPaint = new Paint();
        scrollPaint.setColor(mContext.getResources().getColor(R.color.black));

        guideBitmapPaint = new Paint();
        guideBitmapPaint.setAntiAlias(true);
        guideTextPaint = new Paint();
        guideTextPaint.setTextSize(40);

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            guideTextPaint.setColor(Color.WHITE);
            bitmap_dk_day = BitmapFactory.decodeResource(getResources(), R.drawable.dk_day);
            bitmap_gsd_day = BitmapFactory.decodeResource(getResources(), R.drawable.gsd_day);
            bitmap_hyd_day = BitmapFactory.decodeResource(getResources(), R.drawable.dhyd_night1);

        } else {
            guideTextPaint.setColor(Color.parseColor("#021017"));
            bitmap_dk_day = BitmapFactory.decodeResource(getResources(), R.drawable.dk_night);
            bitmap_gsd_day = BitmapFactory.decodeResource(getResources(), R.drawable.gsd_night);
            bitmap_hyd_day = BitmapFactory.decodeResource(getResources(), R.drawable.dhyd_day1);
        }

        bitmapScale = BitmapFactory.decodeResource(getResources(), R.drawable.switchover);
        r2rBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_style);
        navRectBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jiantou_up);
    }

    private void initData() {
        kLineChart.setWidth(width);
//        kLineChart.setHeight(height);
//
//        ezbarChart.setWidth(width);
//        ezbarChart.setHeight(height);
//
//        ma5Line.setWidth(width);
//        ma5Line.setHeight(height);
//
//        ma10Line.setWidth(width);
//        ma10Line.setHeight(height);
//
//        ma20Line.setWidth(width);
//        ma20Line.setHeight(height);
//
//        ma60Line.setWidth(width);
//        ma60Line.setHeight(height);
//
//        amountMa5Line.setWidth(width);
//        amountMa5Line.setHeight(height);
//
//        amountMa10Line.setWidth(width);
//        amountMa10Line.setHeight(height);
//
//        amountMa20Line.setWidth(width);
//        amountMa20Line.setHeight(height);
//
//        macdDifLine.setWidth(width);
//        macdDifLine.setHeight(height);
//        macdDeaLine.setWidth(width);
//        macdDeaLine.setHeight(height);
//        macdBarLine.setWidth(width);
//        macdBarLine.setHeight(height);
//
//        zhuBarChart.setWidth(width);
//        zhuBarChart.setHeight(height);
//        zhuLine.setWidth(width);
//        zhuLine.setHeight(height);
//
//        dkBarChart.setWidth(width);
//        dkBarChart.setHeight(height);
//        dkLine.setWidth(width);
//        dkLine.setHeight(height);
//
//        gsdBarChart.setWidth(width);
//        gsdBarChart.setHeight(height);
//        gsdLine.setWidth(width);
//        gsdLine.setHeight(height);
//
//        psyLine.setWidth(width);
//        psyLine.setHeight(height);
//
//        asiLine.setWidth(width);
//        asiLine.setHeight(height);
//
//        cciLine.setWidth(width);
//        cciLine.setHeight(height);
//
////        hydLine.setWidth(width);
//
//        rsi6Line.setWidth(width);
//        rsi6Line.setHeight(height);
//        rsi12Line.setWidth(width);
//        rsi12Line.setHeight(height);
//
//        rocLine.setWidth(width);
//        rocLine.setHeight(height);
//        rocmaLine.setWidth(width);
//        rocmaLine.setHeight(height);
//
//        kdjKLine.setWidth(width);
//        kdjKLine.setHeight(height);
//        kdjDLine.setWidth(width);
//        kdjDLine.setHeight(height);
//        kdjJLine.setWidth(width);
//        kdjJLine.setHeight(height);
//
//        bias1Line.setWidth(width);
//        bias1Line.setHeight(height);
//        bias2Line.setWidth(width);
//        bias2Line.setHeight(height);
//        bias3Line.setWidth(width);
//        bias3Line.setHeight(height);

        isScrollDetail = false;
        // 将高度均分为6等分
        //画布分为三个部分，12/18、1/18、5/18
        dashedGroup[0] = height / 6;
        dashedGroup[1] = 2 * height / 6;
        dashedGroup[2] = 3 * height / 6;
        dashedGroup[3] = height - height * 5 / 36;

        uprightGroup[0][0] = 1;
        uprightGroup[0][1] = 1;
        uprightGroup[0][2] = width - 1;
        uprightGroup[0][3] = 4 * height / 6;

        uprightGroup[1][0] = 1;
        uprightGroup[1][1] = 4 * height / 6 + height / 18;
        uprightGroup[1][2] = width - 1;
        uprightGroup[1][3] = height - 1;

        guideGroup[0][1] = 4 * height / 6 + height / 18;
        guideGroup[0][2] = width - 1;
        guideGroup[0][3] = height - 1;

        guideGroup[0][0] = width - (guideGroup[0][3] - guideGroup[0][1]) * 1.538f;
        middleTextOriginY = uprightGroup[1][1] - 10;

        scaleRectF = new RectF(uprightGroup[0][2] - UtilTools.dip2px(mContext, 22) - 10,
                uprightGroup[0][3]
                        - UtilTools.dip2px(mContext, 22) - 10,
                uprightGroup[0][2] - 10,
                uprightGroup[0][3]
                        - 10);
        scaleRealRectF = new RectF(scaleRectF.left - 20, scaleRectF.top - 20,
                scaleRectF.right + 20, scaleRectF.bottom + 20);
        r2rRectF = new RectF(scaleRectF.left, guideGroup[0][1] + 1, scaleRectF.right, guideGroup[0][1] + 1 + (scaleRectF.right - scaleRectF.left));
        chartRectF = new RectF(uprightGroup[1][0], uprightGroup[1][1],
                uprightGroup[1][2], uprightGroup[1][3]);

        bitmapWidth = 20 * width / 52; //覆盖20根K线
        guideGroup[0][0] = width - bitmapWidth;

        if (!isVer) {
            imageWidth = (guideGroup[0][2] - guideGroup[0][0]) / 2;
            imageHeight = imageWidth * 0.2857f;
            h1 = guideGroup[0][1] + ((guideGroup[0][3] - guideGroup[0][1]) - imageHeight) / 2;
            h2 = h1 + imageHeight;
        } else {
            imageWidth = (guideGroup[0][2] - guideGroup[0][0]) * 3 / 4;
            imageHeight = imageWidth * 0.2857f;
            h1 = guideGroup[0][1] + ((guideGroup[0][3] - guideGroup[0][1]) - imageHeight) / 2;
            h2 = h1 + imageHeight;
        }

        //危险区域

        String middelText = " 3日累计敢死队资金 ";
        float textw = datePaint.measureText(middelText);
        zjRectF = new RectF(width / 2 - textw / 2 - 80, height * 12 / 18 - 30, width / 2 + textw / 2 + 80, height * 13 / 18 + 30);

        mTotalList = new ArrayList<>();
        mDesEntityList = new ArrayList<>();

        column = new ArrayList<>();
        ma5List = new ArrayList<>();
        ma10List = new ArrayList<>();
        ma20List = new ArrayList<>();
        ma60List = new ArrayList<>();
    }


    /**
     * 上级传入参数,findView便可调用
     *
     * @param entity
     */
    public void setDesEntity(StockDetailEntity entity) {
        this.detailEntity = entity;
        decm = detailEntity.getDecm();
    }

    public void setData(List<KChartEntity> lists) {


        if (lists == null || lists.isEmpty()) {
            invalidate();
            return;
        }
//
//        if (!mTotalList.isEmpty()) {
//            lBeforeIndex = lists.size() - mTotalList.size();
//        }

        mTotalList.clear();
        mTotalList.addAll(lists);

        for (int i = 0; i < mTotalList.size(); i++) {
            KChartEntity entity = mTotalList.get(i);
            ColumnValuesDataModel data;
            if (entity.getOpen() > entity.getClose()) {
                data = new ColumnValuesDataModel(greencolor, greencolor,
                        entity.getVolume());
            } else if (entity.getOpen() < entity.getClose()) {
                data = new ColumnValuesDataModel(redcolor, redcolor,
                        entity.getVolume());
            } else {
                //没看懂
                if (i != 0) {
                    float lastClose = mTotalList.get(i - 1).getClose();
                    if (entity.getOpen() < lastClose) {
                        data = new ColumnValuesDataModel(greencolor, greencolor,
                                entity.getVolume());
                    } else {
                        data = new ColumnValuesDataModel(redcolor, redcolor,
                                entity.getVolume());
                    }
                } else {
                    data = new ColumnValuesDataModel(redcolor, redcolor,
                            entity.getVolume());
                }
            }
            column.add(data);
        }

        ma5List.clear();
        ma5List.addAll(StockUtils.getMaListByNum(5, mTotalList));
        ma10List.clear();
        ma10List.addAll(StockUtils.getMaListByNum(10, mTotalList));
        ma20List.clear();
        ma20List.addAll(StockUtils.getMaListByNum(20, mTotalList));
        ma60List.clear();
        ma60List.addAll(StockUtils.getMaListByNum(60, mTotalList));


//        if (lBeforeIndex != 0) {
//            indexNum = lBeforeIndex + indexNum;
//            lengthNum = lBeforeIndex + lengthNum;
//        } else {
        indexNum = lists.size() - ONE_SCREEN_KLINE_NUM > 0 ? lists.size() - ONE_SCREEN_KLINE_NUM : 0;
        lengthNum = lists.size();
//        }

        refreshUI(indexNum, lists.size());
    }

    private void refreshUI(int start, int end) {
        mDesEntityList.clear();

        List<KLineValuesDataModel> kvalues = new ArrayList<>();
        List<Float> lma5List = new ArrayList<>();
        List<Float> lma10List = new ArrayList<>();
        List<Float> lma20List = new ArrayList<>();
        List<Float> lma60List = new ArrayList<>();
        for (int i = start; i < end; i++) {
            KLineValuesDataModel value = new KLineValuesDataModel(mTotalList.get(i).getOpen(),
                    mTotalList.get(i).getHigh(), mTotalList.get(i).getLow(),
                    mTotalList.get(i).getClose(), (int) mTotalList.get(i).getlTime());
            kvalues.add(value);
        }

        for (int i = start; i < end; i++) {
            lma5List.add(ma5List.get(i));
            lma10List.add(ma10List.get(i));
            lma20List.add(ma20List.get(i));
            lma60List.add(ma60List.get(i));
        }

        kLineChart.setkLineList(kvalues);
        ma5Line.setValues(lma5List);
        ma10Line.setValues(lma10List);
        ma20Line.setValues(lma20List);
        ma60Line.setValues(lma60List);

        mLowestData = 0;
        mHighestData = 0;
        for (int i = 0; i < kvalues.size(); i++) {
            float max = kvalues.get(i).getMaxValue();
            float min = kvalues.get(i).getMinValue();
            float ma5MaxMin = lma5List.get(i);
            float ma10MaxMin = lma10List.get(i);
            float ma20MaxMin = lma20List.get(i);
            float ma60MaxMin = lma60List.get(i);
            if (i == 0) {
                mLowestData = min;
            }
            if (mHighestData < max) {
                mHighestData = max;
            }
            if (mLowestData > min) {
                mLowestData = min;
            }
            if (mHighestData < ma5MaxMin) {
                mHighestData = ma5MaxMin;
            }
            if (mLowestData > ma5MaxMin) {
                mLowestData = ma5MaxMin;
            }
            if (mHighestData < ma10MaxMin) {
                mHighestData = ma10MaxMin;
            }
            if (mLowestData > ma10MaxMin) {
                mLowestData = ma10MaxMin;
            }
            if (mHighestData < ma20MaxMin) {
                mHighestData = ma20MaxMin;
            }
            if (mLowestData > ma20MaxMin) {
                mLowestData = ma20MaxMin;
            }
            if (mHighestData < ma60MaxMin) {
                mHighestData = ma60MaxMin;
            }
            if (mLowestData > ma60MaxMin) {
                mLowestData = ma60MaxMin;
            }
        }

        float tempSpan = mHighestData - mLowestData;
        mHigher1Data = mLowestData + tempSpan * 3 / 4;
        mHigher2Data = mLowestData + tempSpan * 2 / 4;
        mHigher3Data = mLowestData + tempSpan * 1 / 4;

        float heightScale = 4 * height / ((mHighestData - mLowestData) * 6);
        kLineChart.setHeightScale(heightScale);
        kLineChart.setmLowestData(mLowestData);
        kLineChart.setmHighestData(mHighestData);

        int lwithaver = kvalues.size() < 38 ? 39 : (kvalues.size());
        widthAverage = (width - barLeftGap) / lwithaver;
        lineLeftGap = barLeftGap + 7 * widthAverage / (10 * 2);


        kLineChart.setGapWidth(3 * widthAverage / 10);
        kLineChart.setChartwidth(7 * widthAverage / 10);

        ma5Line.setHeightScale(heightScale);
        ma5Line.setmLowestData(mLowestData);
        ma5Line.setmHighestData(mHighestData);

        ma10Line.setHeightScale(heightScale);
        ma10Line.setmLowestData(mLowestData);
        ma10Line.setmHighestData(mHighestData);

        ma20Line.setHeightScale(heightScale);
        ma20Line.setmLowestData(mLowestData);
        ma20Line.setmHighestData(mHighestData);

        ma60Line.setHeightScale(heightScale);
        ma60Line.setmLowestData(mLowestData);
        ma60Line.setmHighestData(mHighestData);

        mKposition = new CGPoint(barLeftGap, 0);
        mIndexposition = new CGPoint(lineLeftGap, 0);

        ma5Line.setOriginPoint(mIndexposition);
        ma10Line.setOriginPoint(mIndexposition);
        ma20Line.setOriginPoint(mIndexposition);
        ma60Line.setOriginPoint(mIndexposition);
        kLineChart.setOriginPoint(mKposition);

        float intervalTotal = width - lineLeftGap - widthAverage - 7
                * widthAverage / 10;
        int lAver = lma5List.size() < 38 ? 37 : (lma5List.size() - 2);
        interval = intervalTotal / lAver;
        ma5Line.setInterval(interval);
        ma10Line.setInterval(interval);
        ma20Line.setInterval(interval);
        ma60Line.setInterval(interval);

        // 设置悬浮框
        for (int i = 0; i < kvalues.size(); i++) {
            KLineValuesDataModel value = kvalues.get(i);
            StockDesEntity entity = new StockDesEntity();
            entity.setOpenValue(value.getOpenValue());
            entity.setCloseValue(value.getCloseValue());
            entity.setHighValue(value.getMaxValue());
            entity.setLowValue(value.getMinValue());
            entity.setMA5(lma5List.get(i));
            entity.setMA10(lma10List.get(i));
            entity.setMA20(lma20List.get(i));
            entity.setMA60(lma60List.get(i));
            entity.setDate(mTotalList.get(indexNum + i).getTime() + "");
            mDesEntityList.add(entity);
        }


        Log.e("witch index", mBarType + "");
        switch (mBarType) {
//                case 0:
//                    refreshAmountUI(start, end);
//                    break;
//                case 1:
//                    refreshMACD(start, end);
//                    break;
//                case 2:
//                    refreshKDJ(start, end);
//                    break;
//                case 3:
//                    refreshRSI(start, end);
//                    break;
//                case 4:
//                    refreshBIAS(start, end);
//                    break;
//                case 5:
//                    refreshCCI(start, end);
//                    break;
//                case 6:
//                    refreshROC(start, end);
//                    break;
//                case 7:
//                    refreshASI(start, end);
//                    break;
//                case 8:
//                    refreshPSY(start, end);
//                    break;
//                case 9:
//                case 10:
//                case 11:
//                case 18:
//                case 21:
//                case 31:
//                    refreshZhuday1(start, end);
//                    break;
//                case 12:
//                case 13:
//                case 14:
//                case 19:
//                case 22:
//                case 33:
//                    refreshDKday1(start, end);
//                    break;
//                case 15:
//                case 16:
//                case 17:
//                case 20:
//                case 23:
//                case 32:
//                    refreshGSDday1(start, end);
//                    break;
//                case 25:
//                    refreshHYD(start, end);
//                    break;
            default:
                // ---刷新
                refreshAmountUI(start,end);
                invalidate();
                break;
        }

        if (start == 0) {
            //list头为0，range
            handler.sendEmptyMessage(0);
        }


//        if (mChipsBack != null)
//
//        {
//            mChipsBack.setBoarded(mHighestData, mLowestData);
//        }

    }

    private void refreshAmountUI(int start, int end) {
        double columnHighestData = 0;
        double columnLowestData = 0;
        List<ColumnValuesDataModel> columns = new ArrayList<ColumnValuesDataModel>();
        for (int i = start; i < end; i++) {
            columns.add(column.get(i));
        }
        // -------------柱状图

        List<Float> vollMa5List = StockUtils.getVolMaListByNum(5,column);
        List<Float> vollMa10List = StockUtils.getVolMaListByNum(10,column);
        List<Float> vollMa20List = StockUtils.getVolMaListByNum(20,column);

        List<Float> svollMa5List = new ArrayList<Float>();
        List<Float> svollMa10List = new ArrayList<Float>();
        List<Float> svollMa20List = new ArrayList<Float>();

        for (int i = start; i < end; i++) {
            svollMa5List.add(vollMa5List.get(i));
        }

        for (int i = start; i < end; i++) {
            svollMa10List.add(vollMa10List.get(i));
        }

        for (int i = start; i < end; i++) {
            svollMa20List.add(vollMa20List.get(i));
        }

        for (int i = 0; i < columns.size(); i++) {
            if (i == 0) {
                columnLowestData = columns.get(i).getValue();
            }
            float max = columns.get(i).getValue();
            if (columnHighestData < max) {
                columnHighestData = max;
            }
            if (columnLowestData > max) {
                columnLowestData = max;
            }
            float max5 = svollMa5List.get(i);
            if (columnHighestData < max5) {
                columnHighestData = max5;
            }
            if (columnLowestData > max5) {
                columnLowestData = max5;
            }

            float max10 = svollMa10List.get(i);
            if (columnHighestData < max10) {
                columnHighestData = max10;
            }
            if (columnLowestData > max10) {
                columnLowestData = max10;
            }

            float max20 = svollMa20List.get(i);
            if (columnHighestData < max20) {
                columnHighestData = max20;
            }
            if (columnLowestData > max20) {
                columnLowestData = max20;
            }
        }

        mAmountDes = MathUtils.formatNum((float) columnHighestData /VOLUME_UNIT,4) + "亿"; //保留两位小数

        double columnHeightScale = (5 * height / 18) / columnHighestData;
        ezbarChart.setGapWidth(3 * widthAverage / 10);
        ezbarChart.setColumnWidth(7 * widthAverage / 10);
        ezbarChart.setHeightScale(columnHeightScale);
        ezbarChart.setmHighestData(columnHighestData);
        ezbarChart.setValues(columns);

        mColumnposition = new CGPoint(barLeftGap, height);
        ezbarChart.setOriginPoint(mColumnposition);

        for (int i = 0; i < svollMa5List.size(); i++) {
            mDesEntityList.get(i).setColumn(columns.get(i).getValue());
            mDesEntityList.get(i).setVollMA5(svollMa5List.get(i));
            mDesEntityList.get(i).setVollMA10(svollMa10List.get(i));
            mDesEntityList.get(i).setVollMA20(svollMa20List.get(i));
        }

        amountMa5Line.setValues(svollMa5List);
        amountMa10Line.setValues(svollMa10List);
        amountMa20Line.setValues(svollMa20List);

        amountMa5Line.setHeightScale(columnHeightScale);
        amountMa5Line.setmLowestData(columnLowestData);
        amountMa5Line.setmHighestData(columnHighestData);
        amountMa10Line.setHeightScale(columnHeightScale);
        amountMa10Line.setmLowestData(columnLowestData);
        amountMa10Line.setmHighestData(columnHighestData);
        amountMa20Line.setHeightScale(columnHeightScale);
        amountMa20Line.setmLowestData(columnLowestData);
        amountMa20Line.setmHighestData(columnHighestData);

        amountMa5Line.setInterval(interval);
        amountMa10Line.setInterval(interval);
        amountMa20Line.setInterval(interval);

        CGPoint lAmountPosition = new CGPoint(lineLeftGap, 13 * height / 18);

        amountMa5Line.setOriginPoint(lAmountPosition);
        amountMa10Line.setOriginPoint(lAmountPosition);
        amountMa20Line.setOriginPoint(lAmountPosition);
        // ---刷新
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canRefresh) {
            ifcanMove = true;
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1],
                    uprightGroup[0][2], uprightGroup[0][3], rectPaint);
            canvas.drawRect(uprightGroup[1][0], uprightGroup[1][1],
                    uprightGroup[1][2], uprightGroup[1][3], rectPaint);

            for (int i = 0; i < dashedGroup.length; i++) {
                if (mBarType == 25 && i == dashedGroup.length - 1) {
                    continue;
                }
                canvas.drawLine(mIndexposition.getX(), dashedGroup[i],
                        mIndexposition.getX() + width, dashedGroup[i],
                        dashedPaint);
            }

            kLineChart.draw(canvas, kLineChart.getOriginPoint());
            ma5Line.draw(canvas, ma5Line.getOriginPoint());
            ma10Line.draw(canvas, ma10Line.getOriginPoint());
            ma20Line.draw(canvas, ma20Line.getOriginPoint());
            ma60Line.draw(canvas, ma60Line.getOriginPoint());

            canvas.drawText(
                    MathUtils.formatNum(mHighestData, decm) + "", 5,
                    25, datePaint);
            canvas.drawText(MathUtils.formatNum(mHigher1Data, decm), 5, height / 6 + 25, datePaint);
            canvas.drawText(MathUtils.formatNum(mHigher2Data, decm), 5, height / 6 * 2 + 25, datePaint);
            canvas.drawText(MathUtils.formatNum(mHigher3Data, decm), 5, height / 6 * 3 + 25, datePaint);
            canvas.drawText(MathUtils.formatNum(mLowestData, decm), 5, height / 6 * 4, datePaint);

            ezbarChart.draw(canvas, ezbarChart.getOriginPoint());
            amountMa5Line.draw(canvas, amountMa5Line.getOriginPoint());
            amountMa10Line.draw(canvas, amountMa10Line.getOriginPoint());
            amountMa20Line.draw(canvas, amountMa20Line.getOriginPoint());
            canvas.drawText(
                    mAmountDes, 5,
                    height / 18 + height / 6 * 4 + UtilTools.dip2px(mContext, 10),
                    datePaint);
        } else {
            ifcanMove = false;
            float textLength = loadPaint.measureText("数据加载中");
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1],
                    uprightGroup[0][2], uprightGroup[0][3], rectPaint);
            canvas.drawRect(uprightGroup[1][0], uprightGroup[1][1],
                    uprightGroup[1][2], uprightGroup[1][3], rectPaint);
            canvas.drawText("数据加载中", width / 2 - textLength / 2, height / 2,
                    loadPaint);
            canRefresh = true;
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
