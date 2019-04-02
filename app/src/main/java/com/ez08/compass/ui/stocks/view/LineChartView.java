package com.ez08.compass.ui.stocks.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.stocks.PieChartEntity;

public class LineChartView extends RelativeLayout {

	private Context mContext;

	public static final int Main_Capital=0;
	public static final int GSD_Capital=1;
	public static final int DK_Capital=2;

	int redcolor;
	int greencolor;

	public LineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}

	public LineChartView(Context context) {
		super(context);
		mContext=context;
		init();
	}

	private void init() {
		if(CompassApp.GLOBAL.THEME_STYLE == 0){
			redcolor = ContextCompat.getColor(mContext,R.color.red);
			greencolor = ContextCompat.getColor(mContext,R.color.green);
		}else{
			redcolor = ContextCompat.getColor(mContext,R.color.red_main_color_night);
			greencolor = ContextCompat.getColor(mContext,R.color.green_main_color_night);
		}
	}


	boolean stopFlag = false;
	public void setStopFlag(){
		stopFlag = true;
	}

	public void setData(PieChartEntity pieChartEntity, int flag) {
		if(stopFlag){
			removeAllViews();
			View view = View.inflate(mContext,R.layout.text_tip_layout,null);
			addView(view,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			return;
		}

		if(pieChartEntity!=null){
			switch (flag){
				case Main_Capital:
					double mainBuyAmount  = pieChartEntity.getMainBuyAmount();
					double mainSellAmount  = pieChartEntity.getMainSellAmount();
					double retailBuyAmount  = pieChartEntity.getRetailBuyAmount();
					double retailSellAmount  = pieChartEntity.getRetailSellAmount();

					double sum = mainBuyAmount + mainSellAmount + retailBuyAmount + retailSellAmount;
					removeAllViews();
					View view = View.inflate(mContext,R.layout.view_main_capital,null);
					CapitalRectView mainView = (CapitalRectView)view.findViewById(R.id.main_capital);
					CapitalRectView retailView = (CapitalRectView)view.findViewById(R.id.retail_capital);
					mainView.setData( mainBuyAmount/( sum / 2),mainSellAmount/ ( sum / 2));
					retailView.setData(retailBuyAmount/( sum / 2),retailSellAmount /( sum / 2));
//					mainView.setPercentVisible(false);
//					retailView.setPercentVisible(false);

					TextView mainJM = (TextView)view.findViewById(R.id.main_jingmai);
					TextView retailJM = (TextView)view.findViewById(R.id.retail_jingmai);
					TextView mainBuy = (TextView)view.findViewById(R.id.main_buy);
					TextView retailBuy = (TextView) view.findViewById(R.id.retail_buy);
					TextView mainSell = (TextView) view.findViewById(R.id.main_sell);
					TextView retailSell = (TextView)view.findViewById(R.id.retail_sell);

					mainBuy.setText(UtilTools.formatDouble(mainBuyAmount));
					mainSell.setText(UtilTools.formatDouble(mainSellAmount));
					retailBuy.setText(UtilTools.formatDouble(retailBuyAmount));
					retailSell.setText(UtilTools.formatDouble(retailSellAmount));
					mainJM.setText(UtilTools.formatDouble(mainBuyAmount - mainSellAmount));
					retailJM.setText(UtilTools.formatDouble(retailBuyAmount - retailSellAmount ));

					addView(view,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
					break;
				case GSD_Capital:

					double gsdBuyAmount = pieChartEntity.getSgBuyAmount();
					double gsdSellAmount = pieChartEntity.getSgSellAmount();

					removeAllViews();
					View viewGSD = View.inflate(mContext,R.layout.view_dk_capital,null);
					CapitalRectView gsdCapitalView = (CapitalRectView)viewGSD.findViewById(R.id.capital);
					TextView dt = (TextView)viewGSD.findViewById(R.id.dcje_amount);
					TextView kt = (TextView)viewGSD.findViewById(R.id.kcje_amount);
					TextView jm = (TextView)viewGSD.findViewById(R.id.jmre_amount);
					TextView jmTitle = (TextView)viewGSD.findViewById(R.id.jmre);
					TextView buyTitle = (TextView)viewGSD.findViewById(R.id.dcje);
					TextView sellTitle = (TextView)viewGSD.findViewById(R.id.kcje);
					jmTitle.setText("净流入　");
					buyTitle.setText("资金流入");
					sellTitle.setText("资金流出");


					gsdCapitalView.setData(gsdBuyAmount/(gsdBuyAmount + gsdSellAmount),gsdSellAmount/(gsdBuyAmount + gsdSellAmount));;
					dt.setText(UtilTools.formatDouble(gsdBuyAmount));
					kt.setText(UtilTools.formatDouble(gsdSellAmount));

					if(gsdBuyAmount - gsdSellAmount < 0) {
						jm.setText(UtilTools.formatDouble(gsdBuyAmount - gsdSellAmount));
						jm.setTextColor(greencolor);
					}else if(gsdBuyAmount - gsdSellAmount > 0){
						jm.setText(UtilTools.formatDouble(gsdBuyAmount - gsdSellAmount));
						jm.setTextColor(redcolor);
					}else{
						jm.setText(UtilTools.formatDouble(gsdBuyAmount - gsdSellAmount));
						jm.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
					}
					addView(viewGSD,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
					break;
				case DK_Capital:
					double dkBuyAmount = pieChartEntity.getDkBuyMoney();
					double dkSellAmount = pieChartEntity.getDkSellMoney();

					removeAllViews();
					View viewDK = View.inflate(mContext,R.layout.view_dk_capital,null);
					CapitalRectView dkCapitalView = (CapitalRectView)viewDK.findViewById(R.id.capital);
					TextView dt1 = (TextView)viewDK.findViewById(R.id.dcje_amount);
					TextView kt1 = (TextView)viewDK.findViewById(R.id.kcje_amount);
					TextView jm1 = (TextView)viewDK.findViewById(R.id.jmre_amount);
					TextView jmTitle1 = (TextView)viewDK.findViewById(R.id.jmre);
					TextView buyTitle1 = (TextView)viewDK.findViewById(R.id.dcje);
					TextView sellTitle1 = (TextView)viewDK.findViewById(R.id.kcje);
					jmTitle1.setText("净流入　");
					buyTitle1.setText("资金流入");
					sellTitle1.setText("资金流出");

					dkCapitalView.setData(dkBuyAmount/(dkBuyAmount + dkSellAmount),dkSellAmount/(dkBuyAmount + dkSellAmount));
					dt1.setText(UtilTools.formatDouble(dkBuyAmount));
					kt1.setText(UtilTools.formatDouble(dkSellAmount));
					jm1.setText(UtilTools.formatDouble(dkBuyAmount - dkSellAmount));
					if(dkBuyAmount - dkSellAmount < 0) {
						jm1.setText(UtilTools.formatDouble(dkBuyAmount - dkSellAmount));
						jm1.setTextColor(greencolor);
					}else if(dkBuyAmount - dkSellAmount > 0){
						jm1.setText(UtilTools.formatDouble(dkBuyAmount - dkSellAmount));
						jm1.setTextColor(redcolor);
					}else{
						jm1.setText(UtilTools.formatDouble(dkBuyAmount - dkSellAmount));
						jm1.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
					}
					addView(viewDK,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
					break;
				default:
					break;
			}

		}
	}

}
